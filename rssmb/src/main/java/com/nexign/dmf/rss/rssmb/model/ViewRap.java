package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="wrk_web_serv_rap255rep_vw")
@Data
public class ViewRap {
    @Id
    @Column(name="call_id")
    private Long CallId;
    @Column(name="start_time")
    private Date StartTime;
    @Column(name="imsi")
    private String imsi;
    @Column(name="duration")
    private Long duration;
    @Column(name="data_volume")
    private Long DataVolume;
    @Column(name="error_code")
    private Integer errorCode;
    @Column(name="error_msg")
    private String errorMsg;
    @Column(name="file_name")
    private String fileName;
    @Column(name="vpmn_name")
    private String vpmnName;
    @Column(name="hpmn_name")
    private String hpmnName;
    @Column(name="del_date")
    private Date delDate;
    @Column(name="new_call_id")
    private Long newCallId;
}
