package unip.universityInParty.domain.pmList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.pmList.entity.PMList;

@Repository
public interface PMListRepository extends JpaRepository<PMList, Long> {

    void deleteByPartyAndMember(Party party, Member member);

    boolean existsByPartyAndMember(Party party, Member member);

    void deleteByParty(Party party);
}
