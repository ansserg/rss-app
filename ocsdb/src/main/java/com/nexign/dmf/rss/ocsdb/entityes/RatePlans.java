package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="rate_plans")
public class RatePlans {
    @Id
    @Column(name="rtpl_id")
    private Long rtplId;
    @Column(name="name_r")
    private String rtplName;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;
}
