package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="currencies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currencies {
    @Id
    @Column(name="cur_id")
    Long curId;
    @Column(name="def_r")
    String nameR;
    @Column(name="def_e")
    String nameE;
    @Column(name="code")
    String code;

}
