package unip.universityInParty.domain.pmList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.pmList.entity.PMList;

import java.util.List;

@Repository
public interface PMListRepository extends JpaRepository<PMList, Long> {
    @Query("SELECT pm.member.id FROM PMList pm WHERE pm.party.id = :partyId")
    List<Long> findMemberIdsByPartyId(@Param("partyId") Long partyId);
    void deleteByPartyAndMember(Party party, Member member);

    boolean existsByPartyAndMember(Party party, Member member);

    void deleteByParty(Party party);
}
