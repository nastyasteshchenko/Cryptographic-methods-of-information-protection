package ru.nsu;

/**
 * Класс VernamCipherEncryptor предоставляет метод для шифрования текста с использованием шифра Вернама.
 * Шифр Вернама — это симметричный шифр, где каждый символ исходного текста шифруется побитовой операцией XOR
 * с соответствующим символом из ключа. Ключ должен быть такой же длины, как и текст.
 */
public class VernamCipherEncryptor {

    /**
     * Метод для шифрования текста с использованием ключа, используя побитовую операцию XOR.
     *
     * @param text исходный текст, который требуется зашифровать
     * @param key  ключ, который должен быть той же длины, что и текст
     * @return зашифрованная строка
     * @throws IllegalArgumentException если длина ключа не совпадает с длиной текста
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
