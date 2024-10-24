package unip.universityInParty.domain.party.dto.response.gpt;

import unip.universityInParty.domain.party.dto.request.gpt.Message;

public record Choice(
    Message message
) {
}
