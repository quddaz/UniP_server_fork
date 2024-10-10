package unip.universityInParty.domain.pmList.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.repository.PartyRepository;
import unip.universityInParty.domain.pmList.entity.Enum.PartyRole;
import unip.universityInParty.domain.pmList.entity.PMList;
import unip.universityInParty.domain.pmList.repository.PMListRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;
import unip.universityInParty.global.exception.errorCode.PartyErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PMListService {
    private final PMListRepository pmListRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    @Transactional
    public void createJoinParty(PartyRole partyRole, Long memberId, Long partyId){
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (pmListRepository.existsByPartyAndMember(party, member)) {
            throw new CustomException(PartyErrorCode.ALREADY_JOINED);
        }
        party.joinParty();
        PMList pmList = PMList.builder()
            .party(party)
            .member(member)
            .role(partyRole)
            .build();
        pmListRepository.save(pmList);
    }

    @Transactional
    public void deletePartyMember(Long memberId, Long partyId){
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new CustomException(PartyErrorCode.PARTY_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!pmListRepository.existsByPartyAndMember(party, member)) {
            throw new CustomException(PartyErrorCode.MEMBER_NOT_IN_PARTY);
        }
        party.leaveParty();
        pmListRepository.deleteByPartyAndMember(party, member);
    }

    @Transactional
    public void deleteByParty(Party party){
        pmListRepository.deleteByParty(party);
    }
}
