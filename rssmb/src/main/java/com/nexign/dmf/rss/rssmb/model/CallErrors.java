package com.nexign.dmf.rss.rssmb.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="call_errors")
@Data
public class CallErrors {
    @Id
    @Column(name="call_call_id")
    private Long callId;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="del_date")
    private Date delDate;
}
