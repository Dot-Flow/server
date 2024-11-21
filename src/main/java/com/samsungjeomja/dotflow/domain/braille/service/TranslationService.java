package com.samsungjeomja.dotflow.domain.braille.service;

import com.samsungjeomja.dotflow.domain.braille.dto.request.UnicodeRequest;
import com.samsungjeomja.dotflow.domain.braille.dto.response.StringResponse;
import com.samsungjeomja.dotflow.domain.braille.repository.BrailleImageRepository;
import com.samsungjeomja.dotflow.domain.braille.utils.FileConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {
    private final BrailleImageRepository brailleImageRepository;

    private final GptService gptService;
    private static String url = "";

    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB

    public StringResponse translateFromUnicodeToText(UnicodeRequest request) {
        return StringResponse.builder()
                .str("hello world")
                .build();

    }

    public StringResponse translateFromBrfToText(MultipartFile brfFile) throws IOException {
        String[] str = FileConverter.toUnicodeFromBrf(brfFile);

        UnicodeRequest request =
                UnicodeRequest.builder().unicodeArray(List.of(str)).build();

//        String response = sendRequest(request).getBody();


        return StringResponse.builder()
                .str("hello world")
                .build();
    }

    private String summarizeFileContent(MultipartFile brfFile) throws IOException {
        String content = new String(brfFile.getBytes(), StandardCharsets.UTF_8);

        return gptService.getGptResponse(content);

    }

    public <T> ResponseEntity<String> sendRequest(T request) {
        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정 (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HttpEntity에 본문(request)과 헤더 설정
        HttpEntity<T> entity = new HttpEntity<>(request, headers);

        // 서버로 POST 요청 보내기
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

}
