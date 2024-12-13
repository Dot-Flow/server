package com.samsungjeomja.dotflow.braille.dto.response.from_server;

import lombok.Builder;

@Builder
public record TextFileResponse(
        String summary,
        String result,
        byte[] textFile
) {
}
