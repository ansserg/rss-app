package com.nexign.dmf.rss.ocsdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LacRegionItem {
    private String branch;
    private String region;
    private String lac;
}
