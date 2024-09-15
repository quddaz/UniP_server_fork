package unip.universityInParty.domain.party.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.course.dto.CourseDto;
import unip.universityInParty.domain.course.service.CourseService;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.party.dto.request.PartyDto;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.repository.PartyRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;
import unip.universityInParty.global.exception.errorCode.PartyErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PartyService {
    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final CourseService courseService;
    public PartyDetailDto getPartyDetailById(Long id){
        return partyRepository.findPartyDetailById(id);
    }



    @Transactional
    public void create(PartyDto partyDto, String username, List<CourseDto> courseDtos){
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Party party = Party.builder()
            .title(partyDto.getTitle())
            .content(partyDto.getContent())
            .limit(partyDto.getLimit())
            .peopleCount(0)
            .startTime(partyDto.getStartTime())
            .endTime(partyDto.getEndTime())
            .member(member)
            .build();
        Party savedParty = partyRepository.save(party);

        // Courses 저장
        courseService.create(courseDtos, savedParty);
    }
    @Transactional
    public void delete(Long partyId, String username){
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 해당 파티가 현재 사용자에 의해 소유되고 있는지 확인
        if (party.getMember().equals(member)) {
            partyRepository.deleteById(partyId);
            courseService.delete(partyId);
        } else {
            throw new CustomException(PartyErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
    @Transactional
    public void update(Long partyId, PartyDto partyDto, String username, List<CourseDto> courseDtos) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        // 해당 파티가 현재 사용자에 의해 소유되고 있는지 확인
        if (party.getMember().equals(member)) {
            party.setTitle(partyDto.getTitle());
            party.setContent(partyDto.getContent());
            party.setLimit(partyDto.getLimit());
            party.setStartTime(partyDto.getStartTime());
            party.setEndTime(partyDto.getEndTime());
            partyRepository.save(party);
            // Courses 업데이트
            courseService.update(courseDtos, party);
        } else {
            throw new CustomException(PartyErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
