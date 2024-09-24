package unip.universityInParty.domain.universityVerification.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UvRequestDTO {
    @NotEmpty
    private String email;
}
