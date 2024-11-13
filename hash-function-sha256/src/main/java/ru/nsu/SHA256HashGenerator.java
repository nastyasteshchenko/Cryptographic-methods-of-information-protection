package ru.nsu;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс для генерации хешей с использованием алгоритма SHA-256.
 * Предназначен для хеширования строки с добавлением соли.
 */
public class SHA256HashGenerator {
    private static final String HASH_ALGORITHM = "SHA-256";
    private final MessageDigest digest;

    private SHA256HashGenerator(MessageDigest digest) {
        this.digest = digest;
    }

    public static SHA256HashGenerator create() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        return new SHA256HashGenerator(digest);
    }

    /**
     * Хеширует переданную строку с добавлением соли.
     * Соль добавляется в начало строки перед хешированием.
     * Хеширование выполняется с использованием алгоритма SHA-256.
     * Результат возвращается в виде строки в шестнадцатеричном формате.
     *
     * @param input Строка, которую необходимо хешировать.
     * @param salt  Массив байт, представляющий соль.
     * @return Хешированная строка в шестнадцатеричном формате.
     */
    public String hash(String input, byte[] salt) {
        String saltString = new String(salt);

        byte[] hashBytes = digest.digest((saltString + input).getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
