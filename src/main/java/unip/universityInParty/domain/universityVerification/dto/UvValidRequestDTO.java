package unip.universityInParty.domain.universityVerification.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record UvValidRequestDTO(
    @NotEmpty String email,    // 이메일 필드
    @NotEmpty String authCode  // 인증 코드 필드
) {
}