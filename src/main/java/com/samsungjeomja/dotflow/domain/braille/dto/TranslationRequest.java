package com.samsungjeomja.dotflow.domain.braille.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TranslationRequest {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrailleImageDTO{
        String testReq;
//        MultipartFile file;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestDTO {
        private String imageUrl;
        private List<List<Double>> boxes; // Bounding boxes
        private List<String> labels;
    }
}
