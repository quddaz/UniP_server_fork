package unip.universityInParty.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.party.repository.PartyRepository;

import javax.naming.ldap.PagedResultsControl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "회원 정보 DTO")
public class MemberDTO {
    @Schema(description = "회원 ID", example = "1")
    private Long id;

    @Schema(description = "회원 역할", example = "USER")
    private String role;

    @Schema(description = "회원 사용자 이름", example = "john_doe")
    private String username;

    @Schema(description = "회원 이름", example = "John Doe")
    private String name;

    @Schema(description = "인증 여부", example = "true")
    private boolean auth;

    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
    private String profile_image;

    @Schema(description = "회원 상태", example = "BORED")
    private Status status;
}
