package com.samsungjeomja.dotflow.braille.dto.request.to_server;

import org.springframework.web.multipart.MultipartFile;

public record TextFileRequest(
        MultipartFile textFile
) {
}
