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
import unip.universityInParty.domain.party.dto.response.PartyResponseDto;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.repository.PartyRepository;
import unip.universityInParty.domain.pmList.service.PMListService;
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

    // 주어진 파티 ID에 대한 세부 정보를 조회합니다.
    public PartyDetailDto getPartyDetailById(Long id){
        return partyRepository.findPartyDetailById(id)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
    }

    // 주어진 파티 ID에 대한 파티 정보를 조회합니다.
    public Party getPartyById(Long id){
        return partyRepository.findById(id)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
    }

    // 메인 페이지에 표시할 파티 리스트를 조회합니다.
    public List<PartyResponseDto> getPartyMainPage(){
        return partyRepository.getMainPartyPage();
    }

    // 새로운 파티를 생성하고, 관련된 코스를 저장합니다.
    @Transactional
    public Party create(PartyDto partyDto, Long memberId, List<CourseDto> courseDtos){
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Party party = Party.builder()
            .title(partyDto.title())
            .content(partyDto.content())
            .partyLimit(partyDto.limit())
            .peopleCount(0)
            .startTime(partyDto.startTime())
            .endTime(partyDto.endTime())
            .member(member)
            .isClosed(false)
            .build();
        Party savedParty = partyRepository.save(party);

        // Courses 저장
        courseService.create(courseDtos, savedParty);

        return savedParty;
    }

    // 파티를 삭제하고, 관련된 멤버 및 코스를 함께 삭제합니다.
    @Transactional
    public void delete(Long partyId, Long memberId){
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 해당 파티가 현재 사용자에 의해 소유되고 있는지 확인
        if (party.getMember().equals(member)) {
            courseService.delete(partyId);
            party.setClosed(true);
            partyRepository.save(party);
        } else {
            throw new CustomException(PartyErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

    // 파티 정보를 업데이트하고, 관련된 코스를 업데이트합니다.
    @Transactional
    public void update(Long partyId, PartyDto partyDto, Long memberId, List<CourseDto> courseDtos) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 해당 파티가 현재 사용자에 의해 소유되고 있는지 확인
        if (party.getMember().equals(member) && !party.isClosed()) {
            party.setTitle(partyDto.title());
            party.setContent(partyDto.content());
            party.setPartyLimit(partyDto.limit());
            party.setStartTime(partyDto.startTime());
            party.setEndTime(partyDto.endTime());
            partyRepository.save(party); // 파티 업데이트
            // Courses 업데이트
            courseService.update(courseDtos, party);
        } else {
            throw new CustomException(PartyErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
