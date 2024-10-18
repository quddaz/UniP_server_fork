package unip.universityInParty;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;
import unip.universityInParty.domain.universityVerification.repository.UniversityVerificationRepository;
import unip.universityInParty.domain.universityVerification.service.UniversityVerificationService;
import unip.universityInParty.global.config.MailConfig;
import unip.universityInParty.global.exception.custom.CustomException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UniversityVerificationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private UniversityVerificationRepository universityVerificationRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UniversityVerificationService universityVerificationService;

    private static final String EMAIL = "quddnddl35@naver.com";
    private static final Long MEMBER_ID = 1L;

    @Test
    public void testSendVerificationEmail() {
        // Arrange
        MimeMessage message = Mockito.mock(MimeMessage.class); // MimeMessage Mock 생성
        when(javaMailSender.createMimeMessage()).thenReturn(message); // createMimeMessage 메서드 Mock 설정
        doNothing().when(javaMailSender).send(any(MimeMessage.class)); // 이메일 전송 메서드 Mock 설정

        // Act
        universityVerificationService.sendVerificationEmail(EMAIL);

        // Assert
        verify(javaMailSender, times(1)).send(any(MimeMessage.class)); // 이메일 전송이 호출되었는지 검증
        verify(universityVerificationRepository, times(1)).save(any(UniversityVerification.class)); // Redis 저장이 호출되었는지 검증
    }
    @Test
    public void testVerifyAuthCode_Success() {
        // Arrange
        String authCode = "123456";
        UniversityVerification verification = new UniversityVerification();
        verification.setEmail(EMAIL);
        verification.setAuthCode(authCode);

        when(universityVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(verification));
        Member member = new Member();
        member.setId(MEMBER_ID);
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));

        // Act
        universityVerificationService.verifyAuthCode(EMAIL, authCode, MEMBER_ID);

        // Assert
        assertThat(member.isAuth()).isTrue(); // 회원 인증 상태 검증
        verify(memberRepository, times(1)).save(member); // 멤버 저장이 호출되었는지 검증
    }

    @Test
    public void testVerifyAuthCode_Failure() {
        // Arrange
        UniversityVerification verification = new UniversityVerification();
        verification.setEmail(EMAIL);
        verification.setAuthCode("wrongCode");

        when(universityVerificationRepository.findByEmail(EMAIL)).thenReturn(Optional.of(verification));

        // Act & Assert
        assertThatThrownBy(() -> universityVerificationService.verifyAuthCode(EMAIL, "123456", MEMBER_ID))
            .isInstanceOf(CustomException.class);
    }
}
