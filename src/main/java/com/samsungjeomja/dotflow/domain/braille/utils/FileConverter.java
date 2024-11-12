package com.samsungjeomja.dotflow.domain.braille.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class FileConverter {
    public static String[] toUnicodeFromBrf(MultipartFile brfFile) throws IOException {
        List<String> unicodeArrayList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(brfFile.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder unicodeArray = new StringBuilder();
                for (char ch : line.toCharArray()) {
                    unicodeArray.append(BrailleMappingTable.getBrailleUnicode(ch)).append(" ");
                }
                unicodeArrayList.add(unicodeArray.toString());
            }
        }

        return unicodeArrayList.toArray(new String[0]); // 각 행이 하나의 문자열로 결합된 String 배열로 반환
    }


    public static List<String> readFileFromClasspath(String resourcePath) throws IOException {
        List<String> lines = new ArrayList<>();

        // ClassLoader를 사용하여 클래스패스에서 파일을 찾음
        try (InputStream inputStream = FileConverter.class.getClassLoader().getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                throw new IOException("파일을 찾을 수 없습니다: " + resourcePath);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder asciiLine = new StringBuilder();
                StringBuilder tmp = new StringBuilder();
                for (char ch : line.toCharArray()) {
                    asciiLine.append((int) ch).append(" ");
                    tmp.append(BrailleMappingTable.getBrailleUnicode((int) ch)).append(" ");
                }
                System.out.println(asciiLine.toString().trim());
                System.out.println(tmp);
                System.out.println();
                lines.add(line);
            }
        }

        return lines;
    }

    public static byte[] convertResourceToByteArray(String resourcePath) throws IOException {
        try (InputStream inputStream = FileConverter.class.getClassLoader().getResourceAsStream(resourcePath);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }
}
