package com.samsungjeomja.dotflow.domain.braille.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {
    // FromUnicodeToTextRequest 객체를 GZIP으로 압축하는 함수
    public static <T> byte[] compressRequest(T request) throws IOException {
        // ObjectMapper를 사용하여 FromUnicodeToTextRequest 객체를 JSON으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(json.getBytes());
            gzipOutputStream.close();
            return byteArrayOutputStream.toByteArray(); // 압축된 바이트 배열 반환
        }
    }
}