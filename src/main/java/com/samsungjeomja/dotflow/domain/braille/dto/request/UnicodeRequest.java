package com.samsungjeomja.dotflow.domain.braille.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record UnicodeRequest(
        List<String> unicodeArray
) {
}
