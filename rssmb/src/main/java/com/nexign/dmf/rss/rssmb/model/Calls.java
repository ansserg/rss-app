package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="calls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calls {
    @Id
    @Column(name="call_id")
    private Long callid;
    @Column(name="start_time")
    private Date starttime;
    private String imsi;
    @Column(name="cltp_cltp_id")
    private Integer cltpid;
    @Column(name="service_code")
    private String serviceCode;
    @Column(name="destination")
    private String destination;
    @Column(name="call_event_memo")
    private String cdr;
    @Column(name="duration")
    private Long duration;
    @Column(name="data_volume")
    private Long dataVolume;
    @Column(name="navi_date")
    private Date navidate;
    @Column(name="del_date")
    private Date deldate;
    @Column(name="navi_user")
    private String naviuser;
    @Column(name="hpmn_file_id")
    private Long hpmnfileid;
    @Column(name="vpmn_file_id")
    private Long vpmnfileid;
    @Column(name="in_charge")
    private BigDecimal incharge;
    @Column(name="in_tax")
    private BigDecimal intax;
    @Column(name="out_charge")
    private BigDecimal outcharge;
    @Column(name="out_tax")
    private BigDecimal outtax;
    @Column(name="utc_offset")
    private String utcOffset;
}
