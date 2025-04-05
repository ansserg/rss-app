package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.RoundTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundTypesRep extends CrudRepository<RoundTypes,Long> {
}
