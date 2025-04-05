package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="wrk_ms_rtpl_vw")
public class RtplView {
    @Id
    @Column(name="row_num")
    private Long rowId;
    @Column(name="rtpl_name")
    private String rtplName;
    @Column(name="TARIF_NAME")
    private String tariffName;
    @Column(name="PRICE")
    private BigDecimal price;
    @Column(name="RNDT_NAME")
    private String rndtName;
    @Column(name="START_DATE")
    private Date startDate;
    @Column(name="END_DATE")
    private Date endDate;
    @Column(name="RTPL_RTPL_ID")
    private Long rtplId;
    @Column(name="LCAL_ID")
    private Long lcalId;
}
