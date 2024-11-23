package com.samsungjeomja.dotflow.braille.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record BrfFileResponse(
        String summary,
        String[] unicodeArray,
        byte[] brfFile
) {
}
