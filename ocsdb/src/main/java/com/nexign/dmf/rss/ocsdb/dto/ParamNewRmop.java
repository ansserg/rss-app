package com.nexign.dmf.rss.ocsdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ParamNewRmop {
    String tapCode;
    String name;
    String country;
    String imsi;
    String userName;
    Integer task;
    Date startDate;
    String tmplTapCode;
    Long hrsRtplId;
}
