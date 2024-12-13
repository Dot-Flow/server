package com.samsungjeomja.dotflow.braille.repository.service;

import com.samsungjeomja.dotflow.braille.dto.response.to_server.OcrResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrService {

    @Value("${api.ocr.url}")
    private String OCR_URL;

    public List<String> sendBrlImageTranslateRequest(MultipartFile image) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        // MultipartFile을 전송하기 위한 HttpEntity 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // The field name should match what the server expects: 'image'
        body.add("image", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename(); // Set the file name
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<OcrResponse> response = restTemplate.postForEntity(OCR_URL + "/ocr", requestEntity,
                OcrResponse.class);

        return Objects.requireNonNull(response.getBody()).brl().stream()
                .map(innerList -> String.join("", innerList))
                .collect(Collectors.toList());
    }
}
