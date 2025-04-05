package com.nexign.dmf.rss.rssvw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListData {
    private String servName;
    private String tarifName;
    private BigDecimal price;
    private String rndtName;
    private String startDate;
    private String endDate;
    private Long lcalId;
    private Long srlsId;
    private Long rtplId;
}
