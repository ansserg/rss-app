package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "wrk_web_srv_pset_from_rpdr11070_vw")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrefixSets {
    @Id
    @Column(name="pset_id")
    private Long pset_id;
    @Column(name="prefix")
    private String prefix;
    @Column(name="cou_code")
    private String couCode;
}
