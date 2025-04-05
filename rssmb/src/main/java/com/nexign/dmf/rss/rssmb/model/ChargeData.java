package com.nexign.dmf.rss.rssmb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeData {
    private BigDecimal charge;
    private BigDecimal tax;
}
