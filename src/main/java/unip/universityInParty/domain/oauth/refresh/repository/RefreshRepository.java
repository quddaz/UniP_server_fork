package unip.universityInParty.domain.oauth.refresh.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.oauth.refresh.entity.Refresh;

@Repository
public interface RefreshRepository extends CrudRepository<Refresh, Long> {
}