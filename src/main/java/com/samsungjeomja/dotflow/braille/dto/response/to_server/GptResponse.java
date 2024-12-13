package com.samsungjeomja.dotflow.braille.dto.response.to_server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // unknown field들을 무시하도록 설정

public record GptResponse(String object, long created, String model, List<Choice> choices, Usage usage) {

    public record Choice(int index, Message message, String finish_reason, Object logprobs) {
        public record Message(String role, String content, Object refusal) {}
    }

    public record Usage(int prompt_tokens, int completion_tokens, int total_tokens, PromptTokensDetails prompt_tokens_details, CompletionTokensDetails completion_tokens_details) {
        public record PromptTokensDetails(int cached_tokens, int audio_tokens) {}
        public record CompletionTokensDetails(int reasoning_tokens, int audio_tokens, int accepted_prediction_tokens, int rejected_prediction_tokens) {}
    }
}