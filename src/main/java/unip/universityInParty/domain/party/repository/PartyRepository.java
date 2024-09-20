package unip.universityInParty.domain.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.entity.Party;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    Optional<Party> findById(Long id);
    @Query("SELECT new unip.universityInParty.domain.party.dto.response.PartyDetailDto(p.id ,p.title, p.content, p.partyLimit, p.peopleCount, p.startTime, p.endTime) " +
        "FROM Party p WHERE p.id = :id")
    PartyDetailDto findPartyDetailById(@Param("id") Long id);
}
