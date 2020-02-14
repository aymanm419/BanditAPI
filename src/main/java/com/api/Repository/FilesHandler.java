package com.api.Repository;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class FilesHandler {
    private final static String homePath = "C:\\FTPServer\\";
    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }
    public static Byte[] convertToByteArray(String directory) throws IOException {
        File file = new File(homePath + directory);
        if (!file.exists())
            throw new FileNotFoundException("File was not found.");
        return toObjects(Files.readAllBytes(file.toPath()));
    }
}
