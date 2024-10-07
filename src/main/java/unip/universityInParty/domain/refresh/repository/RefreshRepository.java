package unip.universityInParty.domain.refresh.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.refresh.entity.Refresh;

@Repository
public interface RefreshRepository extends KeyValueRepository<Refresh, Long> {

    Boolean existsByToken(String refresh);
    Boolean existsByUsername(String username);
    void deleteByToken(String refresh);
    void deleteByUsername(String username);
}