package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="wrk_rap_255")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrkRap255 {
    @Id
    private Long call_id;
//    private Long hpmn_file_id;
    private String imsi;
    private Date start_time;
    private Long duration;
    private Long data_volume;
}
