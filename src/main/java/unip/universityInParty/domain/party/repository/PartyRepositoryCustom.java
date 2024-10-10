package unip.universityInParty.domain.party.repository;

import org.springframework.data.repository.query.Param;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.dto.response.PartyResponseDto;

import java.util.List;
import java.util.Optional;

public interface PartyRepositoryCustom {
    List<PartyResponseDto> getMainPartyPage();
    Optional<PartyDetailDto> findPartyDetailById(Long id);
}
