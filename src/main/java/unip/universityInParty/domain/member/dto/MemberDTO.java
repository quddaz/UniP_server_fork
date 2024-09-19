package unip.universityInParty.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.party.repository.PartyRepository;

import javax.naming.ldap.PagedResultsControl;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private Long id;
    private String role;
    private String username;
    private String name;
    private boolean auth;
    private String profile_image;
    private Status status;
}