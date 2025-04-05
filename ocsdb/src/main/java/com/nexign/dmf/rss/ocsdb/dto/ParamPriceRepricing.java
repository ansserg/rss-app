package com.nexign.dmf.rss.ocsdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamPriceRepricing {
    private Long rtplId;
    private String rtplName;
    private Long rndtvId;
    private Long rndtdId;
    private Date startDate;
    private Integer task;
    private String startDateStr;
}
