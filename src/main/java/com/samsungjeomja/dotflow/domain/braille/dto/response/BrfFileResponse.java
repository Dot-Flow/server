package com.samsungjeomja.dotflow.domain.braille.dto.response;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record BrfFileResponse(
        String summary,
        byte[] brfFile
) {
}
