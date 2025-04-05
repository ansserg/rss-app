package com.nexign.dmf.rss.ocsdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PsetDirParam {
    private Long psetId;
    private String dirName;
    private String couName;
}
