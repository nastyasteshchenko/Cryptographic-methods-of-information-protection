package ru.nsu;

import java.security.NoSuchAlgorithmException;

public class Test {

    public static void test() throws NoSuchAlgorithmException {
        SaltGenerator saltGenerator = new SaltGenerator();
        SHA256HashGenerator sha256HashGenerator = SHA256HashGenerator.create();
        System.out.println("Демонстрация лавинного эффекта: ");
        String text = "Hello World!";
        byte[] salt = saltGenerator.generateSalt();
        System.out.println("Текст, для которого будет применяться хеш-функция: " + text);
        System.out.println("Сгенерированная соль: " + new String(salt));
        String hashFuncResult = sha256HashGenerator.hash(text, salt);
        System.out.println("Получаем следующий результат хеш функции: " + hashFuncResult);
        text = "Hello Worlk!";
        System.out.println("Теперь меняем одну букву в тексте: " + text);
        System.out.println("Соль оставим такой же: " + new String(salt));
        hashFuncResult = sha256HashGenerator.hash(text, salt);
        System.out.println("Получаем следующий результат хеш функции: " + hashFuncResult);

        System.out.println("--------------------------");

        System.out.println("Демонстрация предсказуемости: ");
        text = "Hello World!";
        salt = saltGenerator.generateSalt();
        System.out.println("Текст, для которого будет применяться хеш-функция: " + text);
        System.out.println("Сгенерированная соль: " + new String(salt));
        hashFuncResult = sha256HashGenerator.hash(text, salt);
        System.out.println("Получаем следующий результат хеш функции: " + hashFuncResult);
        System.out.println("Теперь вызовем хеш функцию с теми же данными.");
        hashFuncResult = sha256HashGenerator.hash(text, salt);
        System.out.println("Получаем следующий результат хеш функции: " + hashFuncResult);
    }
}
