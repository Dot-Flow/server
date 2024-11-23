package com.samsungjeomja.dotflow.braille.dto.response;

import lombok.Builder;

@Builder
public record TextFileResponse(
        String summary,
        String result,
        byte[] textFile
) {
}
