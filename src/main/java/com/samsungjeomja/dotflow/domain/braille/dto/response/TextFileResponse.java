package com.samsungjeomja.dotflow.domain.braille.dto.response;

public record TextFileResponse(
        String summary,
        byte[] textFile
) {
}
