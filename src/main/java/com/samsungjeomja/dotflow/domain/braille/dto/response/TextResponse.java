package com.samsungjeomja.dotflow.domain.braille.dto.response;

import lombok.Builder;

@Builder
public record TextResponse(
        String str
) {
}
