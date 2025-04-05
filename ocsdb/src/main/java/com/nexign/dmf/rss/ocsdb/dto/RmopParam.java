package com.nexign.dmf.rss.ocsdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RmopParam {
    private Long rmopId;
    private Long rflwId;
    private String tapCode;
    private String imsiStart;
    private String imsiEnd;
    private Long rtplId;
}
