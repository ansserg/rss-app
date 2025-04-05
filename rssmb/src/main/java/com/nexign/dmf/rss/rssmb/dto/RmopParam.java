package com.nexign.dmf.rss.rssmb.dto;

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
@Table(name="wrk_web_serv_rssmb_roam_param_vw")
public class RmopParam {
    @Id
    @Column(name="rmop_id")
    private Long rmopId;
    @Column(name="rflw_id")
    private Long rflwId;
    @Column(name="short_name")
    private String tapCode;
    @Column(name="hrs_rtpl_rtpl_id")
    private Long rtplId;
    @Column(name="first_imsi")
    private String imsiStart;
    @Column(name="last_imsi")
    private String imsiEnd;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
 }
