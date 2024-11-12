package com.samsungjeomja.dotflow.domain.braille.controller;

import com.samsungjeomja.dotflow.domain.braille.dto.request.FromUnicodeToTextRequest;
import com.samsungjeomja.dotflow.domain.braille.dto.request.TextRequest;
import com.samsungjeomja.dotflow.domain.braille.dto.response.BrfFileResponse;
import com.samsungjeomja.dotflow.domain.braille.dto.response.TextResponse;
import com.samsungjeomja.dotflow.domain.braille.service.GptService;
import com.samsungjeomja.dotflow.domain.braille.service.TranslationService;
import com.samsungjeomja.dotflow.domain.braille.utils.FileConverter;


import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<TextResponse> translateToString(
            @RequestBody FromUnicodeToTextRequest request) {
        return ResponseEntity.ok(translationService.translateFromUnicodeToText(request));

    }

    @PostMapping(
            value = "/to-text/brf",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TextResponse> translateToString(
            @Parameter(description = "BRF 파일을 업로드하세요.", required = true)
            @RequestPart(value = "file")
            MultipartFile brfFile) throws IOException {

        TextResponse resultDto = translationService.translateFromBrfToText(brfFile);
        return ResponseEntity.ok(resultDto);

    }

    @PostMapping("/to-brf/text")
    public ResponseEntity<BrfFileResponse> convertToBrfFile(
            @RequestBody TextRequest text
            ) throws IOException {
        byte[] file = FileConverter.convertResourceToByteArray("test.brf");

        BrfFileResponse brfFileResponse = BrfFileResponse.builder()
                .brfFile(file)
                .build();

        return ResponseEntity.ok(brfFileResponse);
    }
    @GetMapping("/test")
    public void testApi() throws IOException {
        String pathFile = "test.brf";
        FileConverter.readFileFromClasspath(pathFile);
    }

    @PostMapping("/gpt-test")
    public ResponseEntity<byte[]> gptTest(@RequestParam String string) throws IOException {
        // GPT 요약 처리하여 MultipartFile 반환
        byte[] summary = gptService.getGptSummary(string);

        // 파일의 이름과 Content-Type을 설정하여 ResponseEntity로 반환
        return ResponseEntity.ok(summary);
    }

}
