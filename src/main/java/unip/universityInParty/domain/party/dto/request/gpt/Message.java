package unip.universityInParty.domain.party.dto.request.gpt;

import lombok.Builder;

@Builder
public record Message(
    String role,
    String content
) {
}
