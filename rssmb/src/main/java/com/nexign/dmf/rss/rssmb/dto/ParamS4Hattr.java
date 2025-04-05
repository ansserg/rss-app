package com.nexign.dmf.rss.rssmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamS4Hattr {
    String tapCode;
    String contrNum;
    String vendor;
    String debitor;
    String sd;
    String mm;
    String curInvOut;
    String curInvIn;
    Integer termInDays;
    Date startDate;
    Long task;
    String userName;
}
