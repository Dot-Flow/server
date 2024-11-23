package com.samsungjeomja.dotflow.braille.controller;

import com.samsungjeomja.dotflow.braille.dto.response.BrfFileResponse;
import com.samsungjeomja.dotflow.braille.service.TranslationService;
import com.samsungjeomja.dotflow.braille.dto.request.UnicodeRequest;
import com.samsungjeomja.dotflow.braille.dto.response.TextFileResponse;
import com.samsungjeomja.dotflow.braille.service.GptService;


import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/translate")
public class TranslationController {
    private final TranslationService translationService;
    private final GptService gptService;

    @PostMapping("/to-text/unicode")
    public ResponseEntity<TextFileResponse> translateToString(
            @RequestBody UnicodeRequest request) throws IOException {
        return ResponseEntity.ok(translationService.translateFromUnicodeToText(request));

    }

    @PostMapping(
            value = "/to-text/brf",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TextFileResponse> translateToString(
            @Parameter(description = "BRF 파일을 업로드하세요.", required = true)
            @RequestPart(value = "file")
            MultipartFile brfFile) throws IOException {

        TextFileResponse resultDto = translationService.translateFromBrfToText(brfFile);
        return ResponseEntity.ok(resultDto);

    }

    @PostMapping(
            value = "/to-text/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TextFileResponse> translateToTextFromImage(
            @Parameter(description = "image 파일을 업로드하세요.", required = true)
            @RequestParam
            String imageUrl) throws IOException {

        TextFileResponse resultDto = translationService.translateFromImageToText(imageUrl);
        return ResponseEntity.ok(resultDto);

    }

    @PostMapping("/to-brf/text")
    public ResponseEntity<BrfFileResponse> translateToBrfFromText(
            @RequestBody String request
    ) throws IOException {
        return ResponseEntity.ok(translationService.translateFromTextToBrf(request));
    }

    @PostMapping(
            value = "/to-brf/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BrfFileResponse> translateToBrfFromImage(
            @RequestBody MultipartFile multipartFile
    ) throws IOException {
        return ResponseEntity.ok(translationService.translateFromImageToBrf(multipartFile));
    }


    @PostMapping(
            value = "/to-text/gpt-test",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<byte[]> gptTest(
            @Parameter(description = "Text 파일을 업로드하세요.", required = true)
            @RequestPart(value = "file")
            MultipartFile textFile) throws IOException {
        // GPT 요약 처리하여 MultipartFile 반환
        byte[] summary = gptService.getGptSummary(textFile);

        // 파일의 이름과 Content-Type을 설정하여 ResponseEntity로 반환
        return ResponseEntity.ok(summary);
    }


}
