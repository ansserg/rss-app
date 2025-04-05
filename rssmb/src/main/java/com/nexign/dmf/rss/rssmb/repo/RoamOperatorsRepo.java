package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.RoamOperators;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@Repository
@CrossOrigin()
public interface RoamOperatorsRepo extends JpaRepository<RoamOperators, Long> {
    @Transactional
    @Procedure(name = "agrDataReprocessing")
//    @Query(name="rss_aggregator_pack.mass_aggregator(" +
//            "ip_start_date=>:ip_start_date," +
//            "ip_end_date=>:ip_end_date," +
//            "ip_chunk_sz=>:ip_chunk_sz,"+
//            "ip_num=>:ip_num," +
//            "ip_directions=>:ip_directions," +
//            "ip_vpmn_opers=>:ip_vpmn_opers," +
//            "ip_hpmn_opers=>:ip_hpmn_opers)",
//            nativeQuery = true)
    void massAggregator(
            @Param("ip_start_date") Date ip_start_date,
            @Param("ip_end_date") Date ip_end_date,
            @Param("ip_chunk_sz") Integer ip_chunk_sz//,
//            @Param("ip_directions") String[] ip_directions//,
//            @Param("ip_vpmn_opers") String[] ip_vpmn_opers,
//            @Param("ip_hpmn_opers") String[] ip_hpmn_opers
    );

    @Transactional
    @Procedure(name = "addNewRoamOperator")
    void addTestRmop(
            @Param("p_newOperTapCode") String tapCode,
            @Param("p_newOperName") String operName,
            @Param("p_counrties") String couName,
            @Param("p_imsi") String imsi,
            @Param("p_start_date") Date startDate,
            @Param("p_user_name") String userName,
            @Param("p_task") Long task,
            @Param("p_tmplTapCode") String tmplTapCode,
            @Param("p_hrs_rtpl_id") Long hrsRtplId
    );

    @Transactional
    @Procedure(name = "setS4Hattr")
    void setS4Hattr(
            @Param("p_tap_code") String tapCode,
            @Param("p_contract_num") String contrNum,
            @Param("p_vendor") String vendor,
            @Param("p_debitor") String debitor,
            @Param("p_sd_contract") String sd,
            @Param("p_mm_contract") String mm,
            @Param("p_cur_inv_out") String curInvOut,
            @Param("p_cur_inv_in") String curInvIn,
            @Param("p_term_in_days") Integer termInDays,
            @Param("p_start_date") Date startDate,
            @Param("p_task") Long task,
            @Param("p_user_name") String userName
    );

    @Transactional
    @Procedure(name = "roamOpen")
    void roamOpen(
            @Param("p_roam_type") Integer roamType,
            @Param("p_tap_code") String tapCode,
            @Param("p_rmst") Integer roamStates,
            @Param("p_gprs") String gprs,
            @Param("p_kk") String kk,
            @Param("p_start_date") Date startDate,
            @Param("p_user_name") String userName,
            @Param("p_rfc") Integer task,
            @Param("p_cur_file") Integer curFile,
            @Param("contract_num") String contractNum
    );

    @Transactional
    @Procedure(name = "addLacRegions")
    void addLacRegions(
            @Param("p_task") Integer task,
            @Param("p_user") String userName,
            @Param("p_lac") String lac,
            @Param("p_region") String region
    );

}
