package unip.universityInParty.domain.universityVerification.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
public record UvRequestDTO(
    @NotEmpty String email // 이메일 필드
) {
}