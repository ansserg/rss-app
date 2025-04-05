package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.WrkRtplDef;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WrkRtplDefRepo extends CrudRepository<WrkRtplDef, Long> {
    @Procedure(name = "wrkRtplParsData")
    void wrkRtplParsData(@Param("p_task") Integer task);

    @Procedure(name = "wrkRtplCheckMapping")
    void wrkRtplDefCheckMapping(@Param("p_task") Integer task);

    @Procedure(name = "wrkSetRatePlans")
    void wrkSetRatePlans(@Param("p_RFC") Integer task);

    @Procedure(name = "addLacRegions")
    void addLacRegions(
            @Param("p_lac") String lac,
            @Param("p_region") String region,
            @Param("p_user") String user,
            @Param("p_task") Integer task
    );

    @Modifying
    @Transactional
    @Query(value = "update wrk_rtpl_definition set del_date=sysdate where del_date is null and task=:pTask", nativeQuery = true)
    void deleteByTask(String pTask);

    List<WrkRtplDef> findByTaskAndDeldateIsNull(Long task);
}
