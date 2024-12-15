package com.samsungjeomja.dotflow.braille.dto.response.to_server;

import java.util.List;

public record OcrResponse(
        List<String> brl,
        int image_url)
{}
