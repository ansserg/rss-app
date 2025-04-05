package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.RoamOperHist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RoamOperHistRepo extends CrudRepository<RoamOperHist, Long> {

    @Procedure(name = "setHrsRtplId")
    void setHrsRtplId(
            @Param("p_rflw_id") Long rflwId,
            @Param("p_rtpl_id") Long rtplId,
            @Param("p_start_date") Date startDate,
            @Param("p_user_name") String userName,
            @Param("p_task") Integer task
    );

    RoamOperHist findByRflwIdAndStartDateAfter(Long rflwId, Date parse);

    RoamOperHist findByRflwIdAndEndDateAfter(Long rflwId, Date parse);


    @Modifying
    @Transactional
    @Query(value = "update roam_oper_hist set end_date=:newDate where rmht_id=:rmhtId", nativeQuery = true)
    void UdateEndDate(Long rmhtId, Date newDate);
}
