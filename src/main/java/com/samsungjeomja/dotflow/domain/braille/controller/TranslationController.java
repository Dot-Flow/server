package com.samsungjeomja.dotflow.domain.braille.controller;

import com.samsungjeomja.dotflow.domain.braille.dto.TranslationRequest;
import com.samsungjeomja.dotflow.domain.braille.dto.TranslationResponse;
import com.samsungjeomja.dotflow.domain.braille.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/translation")
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping("/toString")
    public ResponseEntity<TranslationResponse.toStringDTO> translateToString(
            @RequestBody TranslationRequest.BrailleImageDTO brailleImageDTO) {

        TranslationResponse.toStringDTO resultDto = translationService.translate(brailleImageDTO);
        return ResponseEntity.ok(resultDto);

    }

    @PostMapping("/test")
    public ResponseEntity<Void> test(
            @RequestBody TranslationRequest.TestDTO testDTO) {

        translationService.saveTest(testDTO);
        return ResponseEntity.ok().build();

    }

}
