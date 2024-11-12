package com.samsungjeomja.dotflow.domain.braille.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record TextFileRequest(
        MultipartFile textFile
) {
}
