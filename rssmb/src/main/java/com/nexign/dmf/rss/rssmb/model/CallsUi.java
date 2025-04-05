package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "calls_ui")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallsUi {
    @Id
    @Column(name = "call_call_id")
    private Long callCallId;
    @Column(name = "start_time_utc")
    private Date startTimeUtc;
    @Column(name = "imsi")
    private String imsi;
    @Column(name = "cltp_cltp_id")
    private Long CltpCltpId;
    @Column(name = "unique_check_4ui")
    private String uniqueCheck4ui;
    @Column(name = "destination_4ui")
    private String destination4ui;
    @Column(name = "duration_4ui")
    private Long duration4ui;
    @Column(name = "service_code_4ui")
    private String serviceCode4ui;
    @Column(name = "action_code_4ui")
    private Long actionCode4ui;
    @Column(name = "msc_4ui")
    private String msc4ui;
    @Column(name = "del_date_4ui")
    private Date delDate4ui;
}
