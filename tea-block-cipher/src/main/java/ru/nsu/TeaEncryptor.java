package ru.nsu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class TeaEncryptor {
    private final static int DELTA = 0x9E3779B9;
    private final static int ROUNDS = 32;
    private final static int START_SUM_FOR_DECRYPTION = 0xC6EF3720;

    public void decryptFile(String inputFile, String outputFile, byte[] key) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[8];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                if (bytesRead < 8) {
                    // Дополнение недостающих байтов до 8 байт
                    byte[] padded = new byte[8];
                    System.arraycopy(buffer, 0, padded, 0, bytesRead);
                    buffer = padded;
                }
                byte[] processedBlock = decryptTextBlock(key, buffer);
                fos.write(processedBlock);
            }
        }
    }

    public void encryptFile(String inputFile, String outputFile, byte[] key) throws IOException {
        Files.createFile(Path.of(outputFile));
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[8];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                if (bytesRead < 8) {
                    // Дополнение недостающих байтов до 8 байт
                    byte[] padded = {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
                    System.arraycopy(buffer, 0, padded, 0, bytesRead);
                    buffer = padded;
                }
                byte[] processedBlock = encryptTextBlock(key, buffer);
                fos.write(processedBlock);
            }
        }
    }

    public byte[] encryptTextBlock(byte[] key, byte[] textBlock) {
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

    public byte[] decryptTextBlock(byte[] key, byte[] textBlock) {
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

    private int[] getKeyParts(byte[] key) {
        int[] keyParts = new int[4];
        for (int i = 0; i < 4; i++) {
            keyParts[i] = ByteBuffer.wrap(key, i * 4, 4).getInt();
        }
        return keyParts;
    }

    private int[] divideTextBlock(byte[] textBlock) {
        int[] textBlockParts = new int[2];
        textBlockParts[0] = ByteBuffer.wrap(textBlock, 0, 4).getInt();
        textBlockParts[1] = ByteBuffer.wrap(textBlock, 4, 4).getInt();
        return textBlockParts;
    }

    private byte[] intArrayToByteArray(int[] intArray) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putInt(intArray[0]);
        buffer.putInt(intArray[1]);
        return buffer.array();
    }
}
