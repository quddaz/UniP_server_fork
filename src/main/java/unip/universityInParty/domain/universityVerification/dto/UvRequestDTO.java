package unip.universityInParty.domain.universityVerification.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UvRequestDTO {
    @NotEmpty
    private String email;
}
