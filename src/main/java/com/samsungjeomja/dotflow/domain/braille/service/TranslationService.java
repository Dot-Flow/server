package com.samsungjeomja.dotflow.domain.braille.service;

import com.samsungjeomja.dotflow.domain.braille.BrailleImage;
import com.samsungjeomja.dotflow.domain.braille.dto.TranslationRequest;
import com.samsungjeomja.dotflow.domain.braille.dto.TranslationResponse;
import com.samsungjeomja.dotflow.domain.braille.repository.BrailleImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {
    private final BrailleImageRepository brailleImageRepository;

    public TranslationResponse.toStringDTO translate(TranslationRequest.BrailleImageDTO brailleImageDTO) {
        String testString = "Hello world";

        return TranslationResponse.toStringDTO.builder()
                .result(testString)
                .build();
    }

    public void saveTest(TranslationRequest.TestDTO testDTO) {
        BrailleImage brailleImage = BrailleImage.builder()
                .imageUrl(testDTO.getImageUrl())
                .boxes(testDTO.getBoxes())
                .labels(testDTO.getLabels())
                .build();

        brailleImageRepository.save(brailleImage);
    }
}
