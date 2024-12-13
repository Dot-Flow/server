package com.samsungjeomja.dotflow.braille.dto.request.to_server;

import java.util.List;
import lombok.Builder;

@Builder
public record UnicodeRequest(
        List<String> unicodeArray
) {
}
