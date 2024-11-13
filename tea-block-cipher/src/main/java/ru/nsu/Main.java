package ru.nsu;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KeyGenerator keyGen = new KeyGenerator();
        TeaEncryptor teaEncryptor = new TeaEncryptor();
        byte[] text = "Hello World!".getBytes();
        byte[] key = keyGen.generate16BytesKey();
        byte[] encrypted = teaEncryptor.encryptTextBlock(key, text);
        byte[] decrypted = teaEncryptor.decryptTextBlock(key, encrypted);
        System.out.println(new String(key));
        System.out.println(new String(encrypted));
        System.out.println(new String(decrypted));
        teaEncryptor.encryptFile(Main.class.getClassLoader().getResource("./text1.txt").getFile(),
               "./encrypted_text1.txt", key);
        teaEncryptor.decryptFile(
                "./encrypted_text1.txt",
               "./decrypted_text1.txt", key);
    }
}