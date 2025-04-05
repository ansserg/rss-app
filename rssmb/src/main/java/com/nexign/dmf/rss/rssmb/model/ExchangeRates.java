package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NamedStoredProcedureQuery(name = "addExchRate", procedureName = "RSSMB.WRK_OPERATORS.ADD_EXCHANGE_RATES", parameters = {
        @StoredProcedureParameter(mode=ParameterMode.IN,name="p_exch_rate_type",type=Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cur_code", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_value", type = BigDecimal.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_date", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_name", type = String.class)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRates {
    @Id
    @Column(name="xrat_id")
    private Long xratId;
    @Column(name="rate_date")
    private Date rateDate;
    @Column(name="rate")
    private BigDecimal rate;
    @Column(name="cur_cur_id")
    private Long curCurId;
    @Column(name="navi_user")
    private String naviUser;
    @Column(name="navi_date")
    private Date naviDate;
    @Column(name="cur_cur_id_cr")
    private Long curCurIdCr;
    @Column(name="expires_at")
    private Date exspiresAt;
}
