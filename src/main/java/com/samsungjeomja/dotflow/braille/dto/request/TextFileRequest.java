package com.samsungjeomja.dotflow.braille.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record TextFileRequest(
        MultipartFile textFile
) {
}
