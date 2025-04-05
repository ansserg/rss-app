package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="wrk_web_srv_call_err_vw")
public class ViewCallError {
    @Id
    @Column(name="call_id")
    private Long callId;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="error_msg")
    private String errorMsg;
    @Column(name="error_code")
    private Integer errorCode;
    @Column(name="imsi")
    private String imsi;
    @Column(name="cltp_cltp_id")
    private Integer callType;
    @Column(name="service_code")
    private String serviceCode;
    @Column(name="destination")
    private String destination;
    @Column(name="duration")
    private Long duration;
    @Column(name="data_volume")
    private Long dataVolume;
    @Column(name="err_type")
    private Integer errType;
}
