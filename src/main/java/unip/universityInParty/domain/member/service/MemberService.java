package unip.universityInParty.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.domain.member.exception.MemberErrorCode;
import unip.universityInParty.global.service.AWSStorageService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final AWSStorageService awsStorageService;

    @Transactional
    public void setMemberProfileImage(MultipartFile multipartFile, long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        //프로필 이미지 삭제 및 저장
        awsStorageService.deleteFile(member.getProfileImage());
        String url = awsStorageService.uploadFile(multipartFile);

        member.setProfileImage(url);

        memberRepository.save(member);
    }

    @Transactional
    public void setMemberName(String name, long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.setName(name);

        memberRepository.save(member);
    }
}
