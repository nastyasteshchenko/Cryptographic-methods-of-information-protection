package ru.nsu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс TeaEncryptor предоставляет методы для шифрования и дешифрования файлов с использованием алгоритма TEA.
 * Алгоритм TEA работает с 128-битным ключом и блоками данных размером 64 бита. Класс поддерживает как
 * шифрование, так и дешифрование данных на уровне блоков, а также чтение и запись зашифрованных данных в файлы.
 */
public class TeaEncryptor {
    private final static int DELTA = 0x9E3779B9;
    private final static int ROUNDS = 32;
    private final static int START_SUM_FOR_DECRYPTION = 0xC6EF3720;
    private static final int BLOCK_SIZE = 8;


    /**
     * Метод для шифрования файла.
     *
     * @param inputFile  путь к входному файлу с исходными данными
     * @param outputFile путь к выходному файлу для сохранения зашифрованных данных
     * @param key        128-битный ключ для шифрования (массив байтов длиной 16)
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void encryptFile(String inputFile, String outputFile, byte[] key) throws IOException {
        createFile(outputFile);
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[BLOCK_SIZE];
            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                totalBytesRead += bytesRead;
                if (bytesRead < BLOCK_SIZE) {
                    buffer = addMissingBytes(buffer, bytesRead);
                }
                byte[] processedBlock = encryptTextBlock(key, buffer);
                fos.write(processedBlock);
            }

            // Создаем новый блок, где хранится
            int paddingLength = totalBytesRead % BLOCK_SIZE == 0 ? 0 : BLOCK_SIZE - totalBytesRead % BLOCK_SIZE;
            byte[] paddingBlock = new byte[BLOCK_SIZE];
            paddingBlock[BLOCK_SIZE - 1] = (byte) paddingLength;
            fos.write(encryptTextBlock(key, paddingBlock));
        }
    }

    /**
     * Метод для дешифрования файла.
     *
     * @param inputFile  путь к входному зашифрованному файлу
     * @param outputFile путь к выходному файлу для сохранения расшифрованных данных
     * @param key        128-битный ключ для дешифрования (массив байтов длиной 16)
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void decryptFile(String inputFile, String outputFile, byte[] key) throws IOException {
        createFile(outputFile);
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[BLOCK_SIZE];
            byte[] lastBlock = null;

            while (fis.read(buffer) != -1) {
                byte[] processedBlock = decryptTextBlock(key, buffer);
                lastBlock = processedBlock;
                fos.write(processedBlock);
            }

            assert lastBlock != null;
            int paddingLength = lastBlock[7];
            long currentLength = fos.getChannel().position();
            long targetLength = currentLength - paddingLength - BLOCK_SIZE;
            fos.getChannel().truncate(targetLength);
        }
    }

    /**
     * Дополняет блок данных до 8 байт, если блок оказался короче.
     * Метод принимает исходный массив байтов и копирует его содержимое в новый массив, дополняя
     * недостающие байты значением, соответствующим количеству недостающих байтов.
     *
     * @param buffer    исходный массив байтов, который может быть короче 8 байт
     * @param bytesRead количество байтов, фактически прочитанных в блоке
     * @return новый массив байтов длиной 8, содержащий исходные данные и дополненные байты
     */
    private static byte[] addMissingBytes(byte[] buffer, int bytesRead) {
        byte[] padded = new byte[BLOCK_SIZE];
        System.arraycopy(buffer, 0, padded, 0, bytesRead);
        for (int i = bytesRead; i < BLOCK_SIZE; i++) {
            padded[i] = 0x01;
        }
        return padded;
    }

    /**
     * Метод для шифрования одного блока текста (8 байт).
     *
     * @param key       128-битный ключ для шифрования (массив байтов длиной 16)
     * @param textBlock блок текста для шифрования (8 байт)
     * @return зашифрованный блок текста (8 байт)
     */
    private byte[] encryptTextBlock(byte[] key, byte[] textBlock) {
        int[] keyParts = getKeyParts(key);
        int[] textBlockParts = divideTextBlock(textBlock);

        int sum = 0;

        for (int i = 0; i < ROUNDS; i++) {
            sum += DELTA;
            textBlockParts[0] += ((textBlockParts[1] << 4) + keyParts[0]) ^ (textBlockParts[1] + sum)
                    ^ ((textBlockParts[1] >>> 5) + keyParts[1]);
            textBlockParts[1] += ((textBlockParts[0] << 4) + keyParts[2]) ^ (textBlockParts[0] + sum)
                    ^ ((textBlockParts[0] >>> 5) + keyParts[3]);
        }

        return intArrayToByteArray(textBlockParts);
    }

    /**
     * Метод для дешифрования одного блока текста (8 байт).
     *
     * @param key       128-битный ключ для дешифрования (массив байтов длиной 16)
     * @param textBlock зашифрованный блок текста (8 байт)
     * @return расшифрованный блок текста (8 байт)
     */
    private byte[] decryptTextBlock(byte[] key, byte[] textBlock) {
        int[] keyParts = getKeyParts(key);
        int[] textBlockParts = divideTextBlock(textBlock);
        int sum = START_SUM_FOR_DECRYPTION;

        for (int i = 0; i < ROUNDS; i++) {
            textBlockParts[1] -= ((textBlockParts[0] << 4) + keyParts[2]) ^ (textBlockParts[0] + sum)
                    ^ ((textBlockParts[0] >>> 5) + keyParts[3]);
            textBlockParts[0] -= ((textBlockParts[1] << 4) + keyParts[0]) ^ (textBlockParts[1] + sum)
                    ^ ((textBlockParts[1] >>> 5) + keyParts[1]);
            sum -= DELTA;
        }

        return intArrayToByteArray(textBlockParts);
    }

    /**
     * Преобразует 128-битный ключ в массив четырех 32-битных частей.
     *
     * @param key 128-битный ключ (массив байтов длиной 16)
     * @return массив частей ключа (массив из четырех целых чисел)
     */
    private int[] getKeyParts(byte[] key) {
        int[] keyParts = new int[4];
        for (int i = 0; i < 4; i++) {
            keyParts[i] = ByteBuffer.wrap(key, i * 4, 4).getInt();
        }
        return keyParts;
    }

    /**
     * Делит 8-байтный блок текста на две 4-байтные части.
     *
     * @param textBlock блок текста (8 байт)
     * @return массив, содержащий две 4-байтные части блока текста
     */
    private int[] divideTextBlock(byte[] textBlock) {
        int[] textBlockParts = new int[2];
        textBlockParts[0] = ByteBuffer.wrap(textBlock, 0, 4).getInt();
        textBlockParts[1] = ByteBuffer.wrap(textBlock, 4, 4).getInt();
        return textBlockParts;
    }

    /**
     * Преобразует массив целых чисел в массив байтов.
     *
     * @param intArray массив целых чисел (длина 2)
     * @return массив байтов, представляющий целые числа
     */
    private byte[] intArrayToByteArray(int[] intArray) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(intArray[0]);
        buffer.putInt(intArray[1]);
        return buffer.array();
    }

    private static void createFile(String outputFile) throws IOException {
        Path path = Path.of(outputFile);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
}
