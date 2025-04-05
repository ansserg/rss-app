package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Entity
@NamedStoredProcedureQuery(name = "addRmop", procedureName = "WRK_OPERATORS.add_rmop", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tap_code", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_operator_name", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_mcc_mnc", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_hrs_rtpl_id", type = Long.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_start_date", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_tmpl_tap_code", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Table(name="ROAM_OPER_HISTORIES")
public class WrkOperators {
    @Id
    @Column(name="rmop_rmop_id")
    private Long rmopId;
    @Column(name="rtpl_rtpl_id")
    private Long rtplId;
    @Column(name="short_name")
    private String shortName;
    @Column(name="imsi_6")
    private String imsi6;
    @Column(name="operator_name")
    private String operatorName;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;

}
