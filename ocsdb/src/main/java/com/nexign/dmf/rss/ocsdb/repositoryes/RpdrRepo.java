package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.Rpdr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RpdrRepo extends CrudRepository<Rpdr,Long> {
    @Query(value="select * from wrk_web_srv_rpdr_pset_vw where rtpl_rtpl_id=:id and to_date(:dt,'yyyymmdd') between start_date and end_Date and pset_pset_id is not null",nativeQuery = true)
    List<Rpdr> getRpdr(@Param("id") Long id, @Param("dt") String dt);

    @Query(value="select * from wrk_web_srv_rpdr_pset_vw where rtpl_rtpl_id=:id and to_date(:dt,'yyyymmdd') between start_date and end_Date and pset_pset_id in (:psList)",nativeQuery = true)
    List<Rpdr> getPsetDirParam(@Param("psList") List<Long> psList,@Param("id") Long id, @Param("dt") String dt);
}
