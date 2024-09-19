package unip.universityInParty.domain.universityVerification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;
import unip.universityInParty.domain.universityVerification.repository.UniversityVerificationRepository;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;

import java.security.SecureRandom;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UniversityVerificationService {
    private final UniversityVerificationRepository universityVerificationRepository;
    private final MemberRepository memberRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 5;
    private static final SecureRandom random = new SecureRandom();
    @Transactional
    public void create(Long memberId){
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        UniversityVerification universityVerification = UniversityVerification.builder()
            .member(member)
            .authCode(generateRandomString())
            .build();
        universityVerificationRepository.save(universityVerification);
    }
    @Transactional
    public void reRequest(Long memberId){
        UniversityVerification universityVerification = universityVerificationRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.UNIVERSITY_VERIFICATION_NOT_FOUND));

        universityVerification.setAuthCode(generateRandomString());
        universityVerificationRepository.save(universityVerification);
    }
    @Transactional
    public void verificationRequest(String code, Long memberId) {
        UniversityVerification universityVerification = universityVerificationRepository.findByMemberId(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.UNIVERSITY_VERIFICATION_NOT_FOUND));

        if (!Objects.equals(code, universityVerification.getAuthCode())) {
            throw new CustomException(MemberErrorCode.INVALID_AUTH_CODE);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.setAuth(true);
        memberRepository.save(member);

        log.info("Verification request successful for memberId: {}", memberId);
    }

    // 랜덤 문자열 생성 메서드
    private static String generateRandomString() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
