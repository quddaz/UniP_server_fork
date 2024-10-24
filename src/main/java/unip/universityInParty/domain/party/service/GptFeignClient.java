package unip.universityInParty.domain.party.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import unip.universityInParty.domain.party.dto.request.gpt.ChatRequest;
import unip.universityInParty.domain.party.dto.response.gpt.ChatResponse;

@FeignClient(name = "chatgpt", url = "https://api.openai.com/v1")
public interface GptFeignClient {

    @PostMapping("/chat/completions")
    ChatResponse createChatCompletion(
        @RequestHeader("Authorization") String authorization,
        @RequestBody ChatRequest chatRequest);
}