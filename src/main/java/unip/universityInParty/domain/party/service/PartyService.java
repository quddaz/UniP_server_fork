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
import unip.universityInParty.domain.party.dto.request.PartyGptDto;
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


    @Transactional
    public Party create(PartyDto partyDto, Long memberId) {
        Member member = validateMember(memberId);
        Party party = Party.createParty(partyDto, member);
        Party savedParty = partyRepository.save(party);

        // Courses 저장
        courseService.create(partyDto.courses(), savedParty);

        return savedParty;
    }

    @Transactional
    public void update(Long partyId, PartyDto partyDto, Long memberId, List<CourseDto> courseDtos) {
        Party party = validatePartyOwnership(partyId, memberId);
        party.updateParty(partyDto); // 엔티티의 업데이트 메서드 사용
        partyRepository.save(party);

        // Courses 업데이트
        courseService.update(courseDtos, party);
    }

    // 공통 로직: 멤버 검증
    private Member validateMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Party validatePartyOwnership(Long partyId, Long memberId) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = validateMember(memberId);

        if (!party.getMember().equals(member) || party.isClosed()) {
            throw new CustomException(PartyErrorCode.UNAUTHORIZED_ACCESS);
        }
        return party;
    }
}
