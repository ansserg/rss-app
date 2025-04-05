package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_web_srv_rate_plan_tariffs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RatePlanTariffs {
    @Column(name="rtpl_rtpl_id")
    private Long rtplId;
    @Column(name="trf_id")
    private Long trfId;
    @Id
    @Column(name="trf_name")
    private String trfName;
    private BigDecimal price;
    @Column(name="rndt_id")
    private Long rndtId;
    @Column(name="rndt_name")
    private String rndtName;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
}
