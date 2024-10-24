package unip.universityInParty.domain.party.dto.response.gpt;

import lombok.Builder;

import java.awt.*;

@Builder
public record ChatResponse(
    Choice[] choices
) {
}
