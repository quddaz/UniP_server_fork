package unip.universityInParty.domain.universityVerification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;

import java.util.Optional;

@Repository
public interface UniversityVerificationRepository extends KeyValueRepository<UniversityVerification, String> {
    Optional<UniversityVerification> findByEmail(String email);
    void deleteByEmail(String email);
}