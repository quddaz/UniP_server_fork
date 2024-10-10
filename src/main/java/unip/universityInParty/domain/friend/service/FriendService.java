package unip.universityInParty.domain.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.entity.Friend;
import unip.universityInParty.domain.friend.repository.FriendRepository;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.FriendErrorCode;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    /**
     * 친구 요청 수락
     */
    @Transactional
    public void acceptRequest(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findById(fromMemberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member toMember = memberRepository.findById(toMemberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 이미 친구 관계가 존재하는지 체크
        if (friendRepository.existsByFromMemberAndToMember(fromMember, toMember)) {
            throw new CustomException(FriendErrorCode.ALREADY_FRIENDS);
        }

        Friend fromFriend = Friend.builder()
            .fromMember(fromMember)
            .toMember(toMember)
            .build();

        Friend toFriend = Friend.builder()
            .fromMember(toMember)
            .toMember(fromMember)
            .build();

        friendRepository.save(fromFriend);
        friendRepository.save(toFriend);
    }

    /**
     * 친구 요청 삭제
     */
    @Transactional
    public void deleteRequest(Long fromMemberId, Long toMemberId) {
        friendRepository.deleteByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
    }

    public List<FriendDTO> getMyFriend(Long id){
        return friendRepository.getMyFriend(id);
    }

    public List<FriendDTO> getBoredFriend(Long id) { return  friendRepository.getBoredFriend(id);}


}
