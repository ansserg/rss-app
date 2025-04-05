package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedStoredProcedureQuery(name = "setHrsRtplId", procedureName = "rssmb.wrk_operators.setHrsRtplId", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rflw_id", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rtpl_id", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_start_date", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN,name="p_user_name",type=String.class),
        @StoredProcedureParameter(mode=ParameterMode.IN,name="p_task",type = Integer.class)
})

@Table(name = "roam_oper_hist")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RoamOperHist {
    @Id
    @SequenceGenerator(name = "rmht_seq", sequenceName = "rmht_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rmht_seq")
    @Column(name = "rmht_id")
    private Long rmhtId;
    @Column(name = "rflw_rflw_id")
    private Long rflwId;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "rmst_rmst_id")
    private Integer rmstId;
    @Column(name = "contract_num")
    private String contractNum;
    @Column(name = "tpfr_tpfr_id")
    private Integer tpfrId;
    @Column(name = "ctof_ctof_id")
    private Integer ctofId;
    @Column(name = "rrpm_rrpm_id")
    private Integer rrpmId;
    @Column(name = "ntfm_ntfm_id")
    private Integer ntfmId;
    @Column(name = "frrp_ntfm_id")
    private Integer frrpId;
    @Column(name = "in_air")
    private String inAir;
    @Column(name = "hplm_mark_up")
    private String hplmMarkUp;
    @Column(name = "in_free_sec")
    private Integer inFreeSec;
    @Column(name = "mark_up_hpmn")
    private String markUpHpmn;
    @Column(name = "not_chargeable_test")
    private BigDecimal notChargeableTest;
    @Column(name = "cur_cur_id_tap")
    private Long curIdTap;
    @Column(name = "cur_cur_id_invc")
    private Long curIdInvc;
    @Column(name = "overdue_pct")
    private BigDecimal overduePct;
    private BigDecimal discount;
    @Column(name = "term_in_days")
    private Integer termInDays;
    @Column(name = "personal_account")
    private String personalAccount;
    @Column(name = "cur_cur_id_frrp")
    private Long curIdFrrp;
    @Column(name = "fax_treshold")
    private BigDecimal faxTreshold;
    @Column(name = "auto_hur")
    private Integer autoHur;
    @Column(name = "tax_rate")
    private BigDecimal taxRate;
    @Column(name = "vtmt_vtmt_id")
    private Integer vtmtId;
    @Column(name = "hrs_rtpl_rtpl_id")
    private Long hrsRtplId;
    @Column(name = "HRS_HPLM_MARK_UP_#")
    private BigDecimal hrsHplmMarkUp;
    @Column(name = "lcur_cur_id")
    private Long lcurId;
    @Column(name = "rdff_rdff_id")
    private Long rdffId;
    @Column(name = "bis_upload_fmt")
    private Integer bisUploadFmt;
    @Column(name = "rating_check")
    private String ratingCheck;
    @Column(name = "need_repricing")
    private Character needRepricing;
    private String udf;
    @Column(name = "doubleSubs")
    private Character double_subs;
    @Column(name = "navi_user")
    private String naviUser;
    @Column(name = "navi_date")
    private Date naviDate;
    @Column(name = "hrs_rtpl_scnd_id")
    private Long hrsRtplScndId;
    @Column(name = "comission_function")
    private String comissionFunction;
    @Column(name = "invc_calc_conn_sums_mode")
    private Integer invcCalcConnSumsMode;
    @Column(name = "tap_upload_fmt")
    private Integer tapUploadFmt;
    @Column(name = "ipdr_ipdr_id")
    private Integer ipdrId;
    @Column(name = "brok_serv_percent")
    private Integer brokServPercent;
    @Column(name = "reprice_upload_fmt")
    private Integer repriceUploadFmt;

    public RoamOperHist(RoamOperHist roh) {
        this.rflwId = roh.rflwId;
        this.startDate = startDate;
        this.endDate = roh.endDate;
        this.rmstId = roh.rmstId;
        this.contractNum = roh.contractNum;
        this.tpfrId = roh.tpfrId;
        this.ctofId = roh.ctofId;
        this.rrpmId = roh.rrpmId;
        this.ntfmId = roh.ntfmId;
        this.frrpId = roh.frrpId;
        this.inAir = roh.inAir;
        this.hplmMarkUp = roh.hplmMarkUp;
        this.inFreeSec = roh.inFreeSec;
        this.markUpHpmn = roh.markUpHpmn;
        this.notChargeableTest = roh.notChargeableTest;
        this.curIdTap = roh.curIdTap;
        this.curIdInvc = roh.curIdInvc;
        this.overduePct = roh.overduePct;
        this.discount = roh.discount;
        this.termInDays = roh.termInDays;
        this.personalAccount = roh.personalAccount;
        this.curIdFrrp = roh.curIdFrrp;
        this.faxTreshold = roh.faxTreshold;
        this.autoHur = roh.autoHur;
        this.taxRate = roh.taxRate;
        this.vtmtId = roh.vtmtId;
        this.hrsRtplId = roh.hrsRtplId;
        this.hrsHplmMarkUp = roh.hrsHplmMarkUp;
        this.lcurId = roh.lcurId;
        this.rdffId = roh.rdffId;
        this.bisUploadFmt = roh.bisUploadFmt;
        this.ratingCheck = roh.ratingCheck;
        this.needRepricing = roh.needRepricing;
        this.udf = roh.udf;
        this.double_subs = roh.double_subs;
        this.naviUser = roh.naviUser;
        this.naviDate = roh.naviDate;
        this.hrsRtplScndId = roh.hrsRtplScndId;
        this.comissionFunction = roh.comissionFunction;
        this.invcCalcConnSumsMode = roh.invcCalcConnSumsMode;
        this.tapUploadFmt = roh.tapUploadFmt;
        this.ipdrId = roh.ipdrId;
        this.brokServPercent = roh.brokServPercent;
        this.repriceUploadFmt = roh.repriceUploadFmt;
    }

}
