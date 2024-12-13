package com.samsungjeomja.dotflow.braille.repository.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungjeomja.dotflow.braille.dto.request.from_server.BrlTranslationRequest;
import com.samsungjeomja.dotflow.braille.dto.request.from_server.TextTranslationRequest;
import com.samsungjeomja.dotflow.braille.dto.response.to_server.BrlResponse;
import com.samsungjeomja.dotflow.braille.dto.response.to_server.OcrResponse;
import com.samsungjeomja.dotflow.braille.dto.response.to_server.TextResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {

    @Value("${api.translate.url}")
    private String TRANSLATE_URL;

    public BrlResponse sendTextTranslateRequest(TextTranslationRequest textTranslationRequest) {
        return sendPostRequest(TRANSLATE_URL + "/translate/to-brl", textTranslationRequest, BrlResponse.class,
                MediaType.APPLICATION_JSON);
    }

    public TextResponse sendBrlTranslateRequest(BrlTranslationRequest brlTranslationRequest) {
        return sendPostRequest(TRANSLATE_URL + "/translate/to-text", brlTranslationRequest, TextResponse.class,
                MediaType.APPLICATION_JSON);
    }


    private <T, R> R sendPostRequest(String url, T requestObject, Class<R> responseType, MediaType requestType) {
        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(requestType);

        // HttpEntity에 요청 데이터와 헤더 설정
        HttpEntity<T> entity = new HttpEntity<>(requestObject, headers);

        // POST 요청 보내기
        ResponseEntity<R> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                responseType
        );

        // 로그 출력
        log.info("Response status: {}", response.getStatusCode());
        log.info("Response body: {}", response.getBody());

        return response.getBody();
    }


}
