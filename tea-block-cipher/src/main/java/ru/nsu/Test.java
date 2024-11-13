package ru.nsu;

import java.io.IOException;
import java.net.URL;

public class Test {
    public static void test() throws IOException {
        KeyGenerator keyGen = new KeyGenerator();
        TeaEncryptor teaEncryptor = new TeaEncryptor();
        byte[] key = keyGen.generate16BytesKey();
        URL url = Main.class.getClassLoader().getResource("./text1.txt");
        if (url == null) {
            return;
        }
        teaEncryptor.encryptFile(url.getFile(), "./encrypted_text1.txt", key);
        teaEncryptor.decryptFile("./encrypted_text1.txt", "./decrypted_text1.txt", key);

        url = Main.class.getClassLoader().getResource("./text2.txt");
        if (url == null) {
            return;
        }
        teaEncryptor.encryptFile(url.getFile(), "./encrypted_text2.txt", key);
        teaEncryptor.decryptFile("./encrypted_text2.txt", "./decrypted_text2.txt", key);
    }
}
