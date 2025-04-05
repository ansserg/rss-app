package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.WrkOperators;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WrkOperatorsRepo extends CrudRepository<WrkOperators,Long> {
    @Procedure(name="addRmop")
    void addRmop(
            @Param("p_tap_code") String tapCode,
            @Param("p_operator_name") String operatorName,
            @Param("p_mcc_mnc") String mccMnc,
            @Param("p_hrs_rtpl_id") Long hrsRtplId,
            @Param("p_start_date") Date startDate,
            @Param("p_tmpl_tap_code") String tmplTapCode,
            @Param("p_user") String user,
            @Param("p_task") Integer task
    );
}
