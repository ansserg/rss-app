package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Data
@Entity
@Table(name = "WRK_RTPL_HICOST_DIRECTIONS")
public class WrkRtplHighDir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dir_id")
    private Long dirId;
    @Column(name = "direction")
    private String direction;
    @Column(name = "code")
    private String code;
//    @Column(name = "price")
//    private BigDecimal price;
    @Column(name = "round_text")
    private String roundText;
    @Column(name = "zone_excl")
    private String zoneExcl;
    @Column(name = "task")
    private Integer task;
    @Column(name = "navi_date")
    private Date naviDate;
    @Column(name = "del_date")
    private Date delDate;

    public WrkRtplHighDir() {
        direction = null;
        code = null;
        roundText = null;
        zoneExcl = null;
        task = null;
        naviDate = null;
        delDate = null;
    }
}
