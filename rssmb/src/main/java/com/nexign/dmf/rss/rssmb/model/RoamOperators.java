package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "roam_operators")
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedStoredProcedureQuery(
        name = "agrDataReprocessing",
        procedureName = "rss_aggregator_pack.mass_aggregator",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "ip_start_date", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "ip_end_date", type = Date.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "ip_chunk_sz", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "ip_num", type = Integer.class)
        }
)
@NamedStoredProcedureQuery(
        name = "addNewRoamOperator",
        procedureName = "wrk_operators.addTestRoamOperator",
        parameters = {
                @StoredProcedureParameter(name = "p_newOperTapCode", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_newOperName", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_counrties", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_imsi", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_start_date", mode = ParameterMode.IN, type = Date.class),
                @StoredProcedureParameter(name = "p_user_name", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_task", mode = ParameterMode.IN, type = Long.class),
                @StoredProcedureParameter(name = "p_tmplTapCode", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_hrs_rtpl_id", mode = ParameterMode.IN, type = Long.class)
        }
)
@NamedStoredProcedureQuery(
        name = "setS4Hattr",
        procedureName = "wrk_operators.set_s4h_attr",
        parameters = {
                @StoredProcedureParameter(name = "p_tap_code", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_contract_num", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_vendor", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_debitor", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_sd_contract", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_mm_contract", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_cur_inv_out", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_cur_inv_in", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_term_in_days", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "p_start_date", mode = ParameterMode.IN, type = Date.class),
                @StoredProcedureParameter(name = "p_task", mode = ParameterMode.IN, type = Long.class),
                @StoredProcedureParameter(name = "p_user_name", mode = ParameterMode.IN, type = String.class)
        }
)
@NamedStoredProcedureQuery(
        name = "roamOpen",
        procedureName = "wrk_operators.roam_open",
        parameters = {
                @StoredProcedureParameter(name = "p_roam_type", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "p_tap_code", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_rmst", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "p_gprs", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_kk", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_start_date", mode = ParameterMode.IN, type = Date.class),
                @StoredProcedureParameter(name = "p_user_name", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_rfc", mode = ParameterMode.IN, type = Long.class),
                @StoredProcedureParameter(name="p_cur_file",mode=ParameterMode.IN,type=Integer.class),
                @StoredProcedureParameter(name="contract_num",mode=ParameterMode.IN,type=String.class)
        }
)

@NamedStoredProcedureQuery(
        name = "addLacRegions",
        procedureName = "wrk_operators.add_lac_region",
        parameters = {
                @StoredProcedureParameter(name = "p_task", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "p_user", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_lac", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_region", mode = ParameterMode.IN, type = String.class)
        }
)


public class RoamOperators {
    @Id
    @Column(name = "rmop_id")
    private Long roamId;
    @Column(name = "short_name")
    private String shortName;
}

/*
  rss_aggregator_pack.mass_aggregator(ip_start_date => to_date('2022-09-27',
                                                               'yyyy-mm-dd'), ---   дата вызова
                                      ip_end_date   => to_date('2022-09-30',
                                                               'yyyy-mm-dd'),
                                      ip_chunk_sz   => 10000,
                                      ip_num        => ip_num,
                                      ip_directions => t_rss_tlb_str50_list('S'),
                                      ip_vpmn_opers => t_rss_tlb_str50_list('RUSNW'),
                                      ip_hpmn_opers => t_rss_tlb_str50_list('CHNTD','SWEEP',
                                                                            'RUST2') --операторы HPMN
                                      );
 */