package unip.universityInParty.domain.party.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.party.dto.request.PartyGptDto;
import unip.universityInParty.domain.party.dto.request.gpt.ChatRequest;
import unip.universityInParty.domain.party.dto.response.gpt.ChatResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptService {

    private final GptFeignClient gptFeignClient;
    private final ObjectMapper objectMapper;
    @Value("${gpt.key}")
    private String KEY;


    public PartyGptDto getChatResponse(String userMessage) {
        String apiKey = "Bearer " + KEY;
        ChatRequest chatRequest = ChatRequest.defaultGpt35Turbo(userMessage).build();

        // GPT API 호출 및 응답 파싱
        ChatResponse chatResponse = gptFeignClient.createChatCompletion(apiKey, chatRequest);
        return parseJsonToPartyDto(chatResponse);
    }

    private PartyGptDto parseJsonToPartyDto(ChatResponse chatResponse) {
        String content = chatResponse.choices()[0].message().content();
        String jsonString = extractJsonString(content);

        try {
            return objectMapper.readValue(jsonString, PartyGptDto.class);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 오류: {}", e.getMessage());
            return null; // 파싱 실패 시 null 반환
        }
    }

    private String extractJsonString(String content) {
        // JSON 문자열 추출
        int startIndex = content.indexOf("{");
        int endIndex = content.lastIndexOf("}") + 1;
        return content.substring(startIndex, endIndex);
    }
}
