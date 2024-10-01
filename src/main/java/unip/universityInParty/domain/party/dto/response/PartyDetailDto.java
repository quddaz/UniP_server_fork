package unip.universityInParty.domain.party.dto.response;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PartyDetailDto(
    Long id,
    String title,
    String content,
    int limit,
    int peopleCount,
    LocalDateTime startTime,
    LocalDateTime endTime
) {
}