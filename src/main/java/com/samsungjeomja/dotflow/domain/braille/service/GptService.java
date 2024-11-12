package com.samsungjeomja.dotflow.domain.braille.service;

import com.samsungjeomja.dotflow.global.response.code.status.ErrorStatus;
import com.samsungjeomja.dotflow.global.response.exception.GeneralException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsungjeomja.dotflow.domain.braille.dto.request.GptRequest;
import com.samsungjeomja.dotflow.domain.braille.dto.response.GptResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptService {

    @Value("${api.open_ai.key}")
    private String apiKey;

    @Value("${api.open_ai.url}")
    private String apiUrl;

    public byte[] getGptSummary(String context) throws IOException {
        String prompt = "3줄로 요약해줘.";

        // GPT 응답 받기
        String response = getGptResponse(prompt + context);  // GPT 응답 가져오기

        // .txt 파일 생성 및 저장
        String fileName = "translated_summary.txt";
        Path path = Paths.get(fileName);
        Files.write(path, response.getBytes());  // 응답 내용을 파일에 기록

        // 파일을 바이트 배열로 읽어서 반환
        byte[] fileContent = Files.readAllBytes(path);

        // 생성된 파일 삭제 (선택 사항)
        Files.deleteIfExists(path);

        return fileContent;
    }
    public String getGptResponse(String prompt){
        GptRequest.Message message = new GptRequest.Message("user", prompt);
        GptRequest gptRequest = new GptRequest(
                "gpt-4o-mini",
                Collections.singletonList(message),
                150,
                0.7,
                1.0
        );



        // ObjectMapper를 사용하여 GptRequest 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try{
            requestBody = objectMapper.writeValueAsString(gptRequest);
        }catch (JsonProcessingException exception){
            throw new GeneralException(ErrorStatus._JSON_MAPPING_FAIL);
        }


        log.info("Request JSON: {}", requestBody);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");


        // HTTP 엔티티 설정
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // RestTemplate을 사용하여 POST 요청을 보냄
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        String responseBody = response.getBody();
        GptResponse gptResponse;

        try{
            gptResponse = objectMapper.readValue(responseBody, GptResponse.class);
        }catch (JsonProcessingException exception){
            throw new GeneralException(ErrorStatus._JSON_MAPPING_FAIL);
        }


        // 'choices' 배열에서 첫 번째 항목의 content 값 추출
        String content = gptResponse.choices().get(0).message().content();

        // GPT 응답 처리
        return content;
    }
}
