package com.nexign.dmf.rss.rssmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeParam {
    private List<ExchangeData> exchData;
    private String userName;
    private Integer task;
    private Date startDate;
}
