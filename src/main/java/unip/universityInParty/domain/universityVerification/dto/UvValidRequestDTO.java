package unip.universityInParty.domain.universityVerification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
@Schema(description = "이메일 인증 코드 유효성 검사를 위한 DTO")
public record UvValidRequestDTO(
    @NotEmpty
    @Schema(description = "이메일 필드", example = "example@example.com")
    String email,    // 이메일 필드

    @NotEmpty
    @Schema(description = "인증 코드 필드", example = "123456")
    String authCode  // 인증 코드 필드
) {
}