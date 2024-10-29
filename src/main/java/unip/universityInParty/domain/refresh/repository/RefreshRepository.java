package unip.universityInParty.domain.refresh.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.refresh.entity.Refresh;

@Repository
public interface RefreshRepository extends CrudRepository<Refresh, String> {

    Boolean existsByToken(String refresh);
    Boolean existsByUsername(String username);


}