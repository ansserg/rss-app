package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="wrk_billing_calls_rap")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillCallsRap {
    @Id
    private Long call_call_id;
    private Date start_time;
}
