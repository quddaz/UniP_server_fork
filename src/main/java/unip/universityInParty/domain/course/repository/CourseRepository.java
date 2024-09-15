package unip.universityInParty.domain.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.course.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    void deleteByPartyId(Long partyId);
}
