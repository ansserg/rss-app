package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.WrkRtplPriceRepricing;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WrkRtplPriceRepricingRepo extends CrudRepository<WrkRtplPriceRepricing, Long> {
    @Procedure(name = "validPLData")
    void validPLData(@Param("p_task") Integer task);

    @Procedure(name = "changePrices")
    void changePrices(
            @Param("p_rtpl_id") Long rtplId,
            @Param("p_rndt_v") Long rndtv,
            @Param("p_rndt_d") Long rndtd,
            @Param("p_start_date") Date startDate,
            @Param("p_task") Integer task,
            @Param("p_user_name") String userName
    );
    @Procedure(name = "addNewRtpl")
    void addNewRtpl(
            @Param("p_tap_code") String tapCode,
            @Param("p_rtpl_name") String rtplName,
            @Param("p_rndt_v") Long rndtv,
            @Param("p_rndt_d") Long rndtd,
            @Param("p_start_date") Date startDate,
            @Param("p_task") Integer task,
            @Param("p_user_name") String userName
    );
    @Modifying
    @Transactional
    @Query(value = "update WRK_RTPL_PRICE_REPRICING set del_date=sysdate where del_date is null and task=:pTask", nativeQuery = true)
    void deleteByTask(String pTask);

    List<WrkRtplPriceRepricing> findByTask(Long task);
}
