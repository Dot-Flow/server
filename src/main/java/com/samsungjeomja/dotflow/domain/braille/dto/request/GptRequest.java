package com.samsungjeomja.dotflow.domain.braille.dto.request;


import java.util.List;

public record GptRequest(
        String model,
        List<Message> messages,
        int max_tokens,
        double temperature,
        double top_p
) {
    public record Message(String role, String content) {
    }
}