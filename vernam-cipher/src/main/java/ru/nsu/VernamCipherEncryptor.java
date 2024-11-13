package ru.nsu;

public class VernamCipherEncryptor {

    /**
     * Шифрует переданный текст шифром Вернама, используя переданный ключ.
     * Этим же методом можно произвести дешифрование, так как XOR — обратимая операция.
     * @param text текст, который необходимо зашифровать шифром Вернама.
     * @param key ключ, с помощью которого будет происходить шифрование шифром Вернама.
     * @return зашифрованный текст.
     */
    public String encrypt(String text, String key) {
        if (text.length() != key.length()) {
            throw new IllegalArgumentException("Длина ключа должна совпадать с длиной сообщения.");
        }

        char[] result = new char[text.length()];
        for (int i = 0; i < text.length(); i++) {
            char textChar = text.charAt(i);
            char keyChar = key.charAt(i);
            // Применяем XOR и преобразуем обратно в символ
            result[i] = (char) (textChar ^ keyChar);
        }

        return new String(result);
    }
}
