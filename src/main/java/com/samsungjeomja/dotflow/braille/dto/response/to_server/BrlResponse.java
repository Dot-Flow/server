package com.samsungjeomja.dotflow.braille.dto.response.to_server;

import java.util.List;
import lombok.Builder;

@Builder
public record BrlResponse(
        List<String> text
) {
}
