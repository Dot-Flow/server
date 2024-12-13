package com.samsungjeomja.dotflow.braille.dto.request.from_server;

import java.util.List;
import lombok.Builder;

@Builder
public record TextTranslationRequest (
        List<String> text
){
}
