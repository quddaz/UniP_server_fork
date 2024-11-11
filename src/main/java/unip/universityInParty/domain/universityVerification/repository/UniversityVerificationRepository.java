package unip.universityInParty.domain.universityVerification.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;

import java.util.Optional;


@Repository
public interface UniversityVerificationRepository extends CrudRepository<UniversityVerification, Long> {

    void deleteByEmail(String email);

    Optional<UniversityVerification> findByEmail(String email);
}