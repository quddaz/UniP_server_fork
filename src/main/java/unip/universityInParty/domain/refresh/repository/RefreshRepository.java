package unip.universityInParty.domain.refresh.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.refresh.entity.Refresh;
@Repository
public interface RefreshRepository extends CrudRepository<Refresh, String> {
}