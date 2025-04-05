package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.WrkRtplHighDir;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WrkRtplHighDirRepo extends CrudRepository<WrkRtplHighDir,Long> {
    @Modifying
    @Transactional
    @Query(value="update WRK_RTPL_HICOST_DIRECTIONS set del_date=sysdate where del_date is null and task=:pTask",nativeQuery = true)
    void deleteByTask(String pTask);
}
