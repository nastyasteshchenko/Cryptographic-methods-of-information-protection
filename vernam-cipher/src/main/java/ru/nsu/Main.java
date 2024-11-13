package ru.nsu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Test.test();
        encryptEnteredLines();
    }

    public static void encryptEnteredLines() {
        System.out.println("-------------------------");
        KeyGenerator keyGenerator = new KeyGenerator();
        VernamCipherEncryptor vernamCipherEncryptor = new VernamCipherEncryptor();

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("Введите строку (для выхода введите 'exit'):");

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Выход из программы.");
                break;
            }

            String key = keyGenerator.generateKey(input.length());
            String encryptedText = vernamCipherEncryptor.encrypt(input, key);
            String decryptedText = vernamCipherEncryptor.encrypt(encryptedText, key);
            printResult(key, input, encryptedText, decryptedText);
        }
    }

    private static void printResult(String key, String text, String encryptedText, String decryptedText) {
        System.out.println("Ключ: " + key);
        System.out.println("Текст для шифрования: " + text);
        System.out.println("Зашифрованный текст: " + encryptedText);
        System.out.println("Дешифрованный текст: " + decryptedText);
    }
}