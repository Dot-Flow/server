package com.samsungjeomja.dotflow.common.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class S3ResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3UploadResponseDto {

        private String imgUrl;

        public static S3UploadResponseDto toDto(String imgUrl) {
            return S3UploadResponseDto.builder()
                .imgUrl(imgUrl)
                .build();
        }
    }
}