package ru.nsu;

import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        SaltGenerator saltGenerator = new SaltGenerator();
        System.out.println(SHA256HashGenerator.create().hash("Hello world!", saltGenerator.generateSalt()));
    }
}