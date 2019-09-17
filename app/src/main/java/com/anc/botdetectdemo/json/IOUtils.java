package com.anc.botdetectdemo.json;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class IOUtils {
    private IOUtils() {}

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] toBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;

        while ((n = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, n);
        }

        return baos.toByteArray();
    }

    public static String toString(InputStream inputStream) throws IOException {
        return new String(toBytes(inputStream), "utf-8");
    }


    public static void writeToFile(File fileToWrite, byte[] buffer) throws IOException {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(fileToWrite);
            fos.write(buffer);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public static void visitEachLine(InputStream inputStream,
                                     LineVisitor visitor) throws IOException {
        LineNumberReader reader =
                new LineNumberReader(new InputStreamReader(inputStream));
        String line;
        boolean stop = false;
        while (!stop && (line = reader.readLine()) != null) {
            stop = visitor.visit(line);
        }
    }

    public interface LineVisitor {
        boolean visit(String line);
    }

    public static abstract class ResultLineVisitor<T> implements LineVisitor {
        public abstract T get();
    }
}
