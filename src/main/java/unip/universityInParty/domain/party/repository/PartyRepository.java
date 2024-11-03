package unip.universityInParty.domain.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.party.entity.Party;

import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {

    Optional<Party> findById(Long id);

}
