package com.nexign.dmf.rss.rssmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamNewRmop {
    String tapCode;
    String name;
    String country;
    String imsi;
    String userName;
    Long task;
    Date startDate;
    String tmplTapCode;
    Long hrsRtplId;
}
