package com.nexign.dmf.rss.rssmb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallsCheck {
    private Long origCallId;
    private Date origStartTime;
    private String origImsi;
    private Integer origCallType;
    private Long origDuration;
    private Long origDataVolume;
    private Long modCallId;
    private Date modStartTime;
    private Integer modCallType;
    private Long modDuration;
    private Long modDataVolume;
    private boolean flgRap;
}
