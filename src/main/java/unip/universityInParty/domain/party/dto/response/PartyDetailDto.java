package unip.universityInParty.domain.party.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unip.universityInParty.domain.party.entity.Party;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyDetailDto {
    private Long id;
    private String title;
    private String content;
    private int limit;
    private int peopleCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
