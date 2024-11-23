package com.samsungjeomja.dotflow.braille.dto.response;

import lombok.Builder;

@Builder
public record StringResponse(
        String str
) {
}
