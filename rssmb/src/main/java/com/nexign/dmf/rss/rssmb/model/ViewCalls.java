package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="wrk_web_serv_calls_vw")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCalls {
    @Id
    @Column(name="call_id")
    private Long callId;
    @Column(name="lcal_id")
    private String lcalId;
    @Column(name="srls_id")
    private String srlsId;
    @Column(name="pset_id")
    private String psetId;
    @Column(name="duration")
    private Long duration;
    @Column(name="data_volume")
    private Long dataVolume;
    @Column(name="in_charge")
    private BigDecimal inCharge;
    @Column(name="in_tax")
    private BigDecimal inTax;
    @Column(name="destination")
    private String destination;
    @Column(name="branch")
    private String branch;
    @Column(name="service")
    private String service;
    @Column(name="lac")
    private String lac;
    @Column(name="rec_type")
    private String recType;
    @Column(name="rtpl_rtpl_id")
    private Long rtplId;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="cltp_cltp_id")
    private Integer cltpCltpId;
    @Transient
    private String rpdrName;
    @Transient
    private String couName;
}
