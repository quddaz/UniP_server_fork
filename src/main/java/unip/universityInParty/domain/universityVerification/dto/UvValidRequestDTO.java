package unip.universityInParty.domain.universityVerification.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UvValidRequestDTO {
    @NotEmpty
    private String email;
    @NotEmpty
    private String authCode;
}
