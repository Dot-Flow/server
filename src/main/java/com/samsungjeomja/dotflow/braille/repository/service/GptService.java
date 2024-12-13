package com.samsungjeomja.dotflow.braille.repository.service;

import com.samsungjeomja.dotflow.braille.dto.request.from_server.GptRequest;
import com.samsungjeomja.dotflow.braille.dto.request.from_server.GptRequest.Message;
import com.samsungjeomja.dotflow.braille.dto.response.to_server.GptResponse;
import com.samsungjeomja.dotflow.braille.utils.FileConverter;
import com.samsungjeomja.dotflow.common.response.code.status.ErrorStatus;
import com.samsungjeomja.dotflow.common.response.exception.GeneralException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class GptService {

    @Value("${api.open_ai.key}")
    private String apiKey;

    @Value("${api.open_ai.url}")
    private String apiUrl;


    public String makePrettyString(String inputSentence) {
        List<String> splitSentences = splitByLines(inputSentence);

        StringBuilder stringBuilder = new StringBuilder();
        for (String str : splitSentences) {
            List<Message> messages = Arrays.asList(new Message("system",
                            "You are a helpful assistant that corrects contextual errors in sentences. "
                                    + "Exclude special characters that are not important to the content. "
                                    + "Always respond with only the corrected sentence, without any additional explanation. "
                                    + "But there must be no part of the content omitted."),
                    new Message("user", "다음 내용을보고 문맥 오류를 수정하고, 개행문자를 포함해서 자연스럽게 내용을 구성해서 수정된 문장만 출력해줘. "
                            + "다만 전체적인 내용이 빠져선 안돼. 나의 부탁이 포함되면 안돼."
                            + "'" + str + "'")

            );
            stringBuilder.append(getGptResponse(messages)).append('\n');

        }
        return stringBuilder.toString();

    }


    public byte[] getGptSummary(MultipartFile context) throws IOException {
        List<Message> messages = Arrays.asList(new Message("system",
                        "You are a helpful assistant to summarize in sentences. " +
                                "Exclude special characters that are not important to the content. "
                                + "Always respond with only the summarized sentence, without any additional explanation."),
                new Message("user", "3문장으로 요약해줘'" + FileConverter.convertFileToString(context) + "'")
        );

        // GPT 응답 받기
        String response = getGptResponse(messages);  // GPT 응답 가져오기

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

    public String makeSummary(String content) {
        List<Message> messages = Arrays.asList(new Message("system",
                        "You are a helpful assistant to summarize in sentences. "
                                + "Always respond with only the summarized sentence, without any additional explanation."),
                new Message("user", "3문장으로 요약해줘'" + content + "'")
        );
        String response = getGptResponse(messages);  // GPT 응답 가져오기
        return response;

    }

    public String getGptResponse(List<Message> messages) {

        GptRequest gptRequest = new GptRequest("gpt-4o", messages, 2048, 0.3, 1.0);

        // ObjectMapper를 사용하여 GptRequest 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(gptRequest);
        } catch (JsonProcessingException exception) {
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
        log.info(responseBody);

        try {
            gptResponse = objectMapper.readValue(responseBody, GptResponse.class);
        } catch (JsonProcessingException exception) {
            throw new GeneralException(ErrorStatus._JSON_MAPPING_FAIL);
        }

        // 'choices' 배열에서 첫 번째 항목의 content 값 추출
        String content = gptResponse.choices().get(0).message().content();

        // GPT 응답 처리
        return content;
    }
    public List<String> splitByLines(String inputString) {
        List<String> result = new ArrayList<>();
        String[] lines = inputString.split("\n");
        StringBuilder chunk = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            chunk.append(lines[i]).append("\n");
            if ((i + 1) % 60 == 0 || i == lines.length - 1) {
                result.add(chunk.toString().trim());
                chunk.setLength(0);
            }
        }
        return result;
    }
}
