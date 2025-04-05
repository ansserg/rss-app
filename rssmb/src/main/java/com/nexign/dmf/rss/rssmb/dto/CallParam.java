package com.nexign.dmf.rss.rssmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallParam {
    private Long callId;
    private Date startTime;
    private String imsi;
    private Long duration;
    private Integer callType;
    private Integer callErrType;
    private Date callErrDelDate;
    private String naviUser;
}
