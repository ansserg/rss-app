package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="countries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Countries {
    @Id
    @Column(name="cou_id")
    Long couId;
    @Column(name="name_e")
    String nameE;
    @Column(name="name_r")
    String nameR;
    @Column(name="couCode")
    String couCode;
    @Column(name="dial_code")
    String dialCode;
    @Column(name="alfa2_code")
    String alfa2Code;
}
