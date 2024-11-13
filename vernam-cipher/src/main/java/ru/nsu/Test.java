package ru.nsu;

public class Test {

    public static void test() {
        KeyGenerator keyGenerator = new KeyGenerator();
        VernamCipherEncryptor vernamCipherEncryptor = new VernamCipherEncryptor();
        String text = "Шифр Вернама построен на принципе «исключающего ИЛИ», он же XOR. " +
                "Он смотрит на каждую пару битов и пытается понять, они одинаковые или разные.";
        String key = keyGenerator.generateKey(text.length());
        String encryptedText = vernamCipherEncryptor.encrypt(text, key);
        String decryptedText = vernamCipherEncryptor.encrypt(encryptedText, key);
        printResult(key, text, encryptedText, decryptedText);

        System.out.println("-------------------------------");

        text = "Thomas Edison was an American inventor and businessman who is best known for his contributions " +
                "to the development of the electric light bulb and the phonograph.";
        key = keyGenerator.generateKey(text.length());
        encryptedText = vernamCipherEncryptor.encrypt(text, key);
        decryptedText = vernamCipherEncryptor.encrypt(encryptedText, key);
        printResult(key, text, encryptedText, decryptedText);
    }

    private static void printResult(String key, String text, String encryptedText, String decryptedText) {
        System.out.println("Ключ: " + key);
        System.out.println("Текст для шифрования: " + text);
        System.out.println("Зашифрованный текст: " + encryptedText);
        System.out.println("Дешифрованный текст: " + decryptedText);
    }
}
