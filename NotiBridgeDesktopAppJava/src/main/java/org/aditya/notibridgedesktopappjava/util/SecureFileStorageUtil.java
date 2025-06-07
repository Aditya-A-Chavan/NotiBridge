package org.aditya.notibridgedesktopappjava.util;


import java.io.*;
import java.nio.file.*;
import org.aditya.notibridgedesktopappjava.security.AESEncryption;

public class SecureFileStorageUtil {
    private static final String FILE_PATH = System.getenv("APPDATA") + "\\NotiBridge\\pairing.enc";

    public static void saveEncryptedData(String plainTest) throws Exception{
        String encrypted = AESEncryption.encrypt(plainTest);
        Path path = Paths.get(FILE_PATH);

        Files.createDirectories(path.getParent());
        Files.write(path, encrypted.getBytes());
    }

    public static String loadDecryptedData() throws Exception {
        Path path = Paths.get(FILE_PATH);

        if(!Files.exists(path)){
            return null;
        }

        String encrypted = new String(Files.readAllBytes(path));
        return AESEncryption.decrypt(encrypted);
    }
    
    public static String loadEncryptedData() throws Exception {
        Path path = Paths.get(FILE_PATH);

        if(!Files.exists(path)){
            return null;
        }

        String encrypted = new String(Files.readAllBytes(path));
        return encrypted;
    }


    public static boolean isDataStored() {
        return Files.exists(Paths.get(FILE_PATH));
    }

    public static void clearData() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_PATH));
    }

}
