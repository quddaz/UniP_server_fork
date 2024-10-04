package unip.universityInParty.domain.universityVerification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Schema(description = "이메일 인증 요청을 위한 DTO")
public record UvRequestDTO(
    @NotEmpty
    @Schema(description = "이메일 필드", example = "example@example.com")
    String email // 이메일 필드
) {
}