package unip.universityInParty.domain.universityVerification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;

import java.util.Optional;

@Repository
public interface UniversityVerificationRepository extends JpaRepository<UniversityVerification, Long> {
    Optional<UniversityVerification> findByMemberId(Long memberId);
}
