package com.samsungjeomja.dotflow.braille.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileConverter {

    /**
     * src : brf
     * dst : unicode
     */
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

    /**
     * src : unicode
     * dst : brf
     */
    public static byte[] toBrfFromUnicode(String[] unicodeArray) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for (String line : unicodeArray) {
            StringBuilder brfLine = new StringBuilder();
            String[] unicodeChars = line.trim().split("\\s+"); // 유니코드 값 공백 기준으로 분리

            for (String unicode : unicodeChars) {
                if (unicode.isEmpty()) { // 빈 문자열 건너뛰기
                    continue;
                }

                int asciiCode = BrailleMappingTable.getAscii(unicode); // 유니코드에서 ASCII 코드 가져오기
                if (asciiCode != -1) { // 유효한 매핑인 경우
                    brfLine.append((char) (asciiCode)); // ASCII 코드로 변환 후 char로 추가
                } else {
                    log.error("매핑되지 않은 유니코드: {}", unicode); // 디버깅 정보 추가
                    throw new IOException("매핑되지 않은 유니코드: " + unicode);
                }
            }

            // 한 줄을 바이트로 변환해서 출력 스트림에 추가 (행 끝에 줄바꿈 추가)
            byteArrayOutputStream.write(brfLine.toString().getBytes(StandardCharsets.UTF_8));
            byteArrayOutputStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        }

        return byteArrayOutputStream.toByteArray(); // BRF 파일의 byte[] 반환
    }

    /**
    /**
     * src : file path
     * dst : brf
     */
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

    /***
     * src : text file
     * dst : string
     */
    public static String convertFileToString(MultipartFile textFile){
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(textFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n"); // 줄바꿈 유지
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.", e);
        }

        return fileContent.toString().trim(); // 마지막에 불필요한 공백 제거
    }

    /**
     * src : string
     * dst : text file
     */
    public static byte[] convertStringToFile(String content, String fileName) throws IOException {
        // String을 바이트 배열로 변환
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        // ByteArrayOutputStream을 사용하여 파일로 변환
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 파일 이름에 ".txt" 확장자 추가
        String fileWithExtension = fileName.endsWith(".txt") ? fileName : fileName + ".txt";

        // 바이트 배열을 .txt 형식으로 ByteArrayOutputStream에 기록
        byteArrayOutputStream.write(bytes);

        return byteArrayOutputStream.toByteArray();
    }

    public static MultipartFile convertByteArrayToMultipartFile(byte[] file) {
        // MockMultipartFile을 사용하여 byte[]를 MultipartFile로 변환
        return new MockMultipartFile("test-file", "test-file", "application/octet-stream", file);
    }
}
