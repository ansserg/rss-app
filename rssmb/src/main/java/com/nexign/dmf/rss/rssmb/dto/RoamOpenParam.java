package com.nexign.dmf.rss.rssmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RoamOpenParam {
    private Integer roamTp;
    private String tapCode;
    private Integer roamStates;
    private Boolean gprs;
    private Boolean kk;
    private Date startDate;
    private String userName;
    private Integer task;
    private Integer curFile;
    private String contractNum;
}
