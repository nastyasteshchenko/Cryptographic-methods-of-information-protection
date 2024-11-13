package ru.nsu;

import java.security.SecureRandom;

public class KeyGenerator {
    private static final int KEY_LENGTH = 16;
    private static final byte[] ALPHABET = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ").getBytes();
    private final SecureRandom random = new SecureRandom();

    public byte[] generate16BytesKey() {
        byte[] key = new byte[KEY_LENGTH];
        for (int i = 0; i < KEY_LENGTH; i++) {
            key[i] = ALPHABET[(random.nextInt(ALPHABET.length))];
        }
        return key;
    }
}