package com.nexign.dmf.rss.ocsdb.repositoryes;


import com.nexign.dmf.rss.ocsdb.entityes.PrefixSets;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PsetRepo extends CrudRepository<PrefixSets, Long> {
}
