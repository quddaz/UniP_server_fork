package unip.universityInParty.domain.emailBlackList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.universityVerification.entity.EmailBlackList;

@Repository
public interface EmailBlackListRepository extends JpaRepository<EmailBlackList, Long> {
    Boolean existsByEmail(String email);
}
