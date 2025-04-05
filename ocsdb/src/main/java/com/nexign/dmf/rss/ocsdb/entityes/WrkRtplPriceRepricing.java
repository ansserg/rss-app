package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "WRK_RTPL_PRICE_REPRICING")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@NamedStoredProcedureQuery(name = "validPLData", procedureName = "WRK_REPRICING.VALID_PRICE_LIST_DATA", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class)
})
@NamedStoredProcedureQuery(name = "changePrices", procedureName = "WRK_REPRICING.CHANGE_PRICE_LIST", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rtpl_id", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rndt_v", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rndt_d", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_start_date", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_name", type = String.class)
})
@NamedStoredProcedureQuery(name = "addNewRtpl", procedureName = "WRK_REPRICING.ADD_PRICE_LIST", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tap_code", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rtpl_name", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rndt_v", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_rndt_d", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_start_date", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_name", type = String.class)
})
public class WrkRtplPriceRepricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="price_id")
    private Long priceid;
    @Column(name="cou_name")
    private String couname;
    @Column(name="moc_l")
    private BigDecimal mocl;
    @Column(name="moc_r")
    private BigDecimal mocr;
    @Column(name="moc_o")
    private BigDecimal moco;
    @Column(name="moc_s")
    private BigDecimal mocs;
    @Column(name="mtc")
    private BigDecimal mtc;
    @Column(name="smsmo")
    private BigDecimal smsmo;
    @Column(name="gprs")
    private BigDecimal gprs;
    @Column(name="del_date")
    private Date deldate;
//    @Column(name="navi_date")
//    private Date navidate;
    @Column(name="navi_user")
    private String naviuser;
    @Column(name="task")
    private Long task;
}
