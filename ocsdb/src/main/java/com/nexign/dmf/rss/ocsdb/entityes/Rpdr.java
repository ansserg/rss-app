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

@Entity
@Table(name="wrk_web_srv_rpdr_pset_vw")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rpdr {
    @Id
    @Column(name="pset_pset_id")
    private Long psetId;
    @Column(name="rtpl_rtpl_id")
    private Long rtplId;
    @Column(name="rpdr_name")
    private String rpdrName;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_Date")
    private Date endDate;
    @Column(name="prefix")
    private String prefix;
    @Column(name="cou_name")
    private String couName;
    @Column(name="price_$")
    private BigDecimal price;
}
