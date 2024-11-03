package unip.universityInParty.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PartyMyDto(
    Long id,

    String title,

    int limit,

    int peopleCount

) {
}
