package com.nexign.dmf.rss.ocsdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RtplList {
    private Long rtplId;
    private String rtplName;
}
