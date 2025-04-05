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
@Table(name="regions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Regions {
    @Id
    @Column(name="reg_id")
    private Long regId;
    @Column(name="def")
    private String name;
    @Column(name="del_date")
    private Date delDate;
}
