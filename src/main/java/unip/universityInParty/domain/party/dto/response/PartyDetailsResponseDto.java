package unip.universityInParty.domain.party.dto.response;


import lombok.Builder;
import unip.universityInParty.domain.course.dto.CourseDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PartyDetailsResponseDto(

    Long id,

    String title,

    String content,

    int limit,

    int peopleCount,

    LocalDateTime startTime,

    LocalDateTime endTime,

    List<CourseDto> courses
) {
    public static PartyDetailsResponseDto createPartyDto(PartyDetailDto partyDetailDto,
                                                         List<CourseDto> courses) {
        return PartyDetailsResponseDto.builder()
            .id(partyDetailDto.id())
            .title(partyDetailDto.title())
            .content(partyDetailDto.content())
            .limit(partyDetailDto.limit())
            .peopleCount(partyDetailDto.peopleCount())
            .startTime(partyDetailDto.startTime())
            .endTime(partyDetailDto.endTime())
            .courses(courses)
            .build();
    }

}
