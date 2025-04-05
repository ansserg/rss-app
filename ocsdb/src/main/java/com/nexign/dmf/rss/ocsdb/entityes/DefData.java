package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedStoredProcedureQuery(name="defRu",procedureName = "wrk_def_ru.def_ru",parameters = {
        @StoredProcedureParameter(mode=ParameterMode.IN,name="p_task",type=Integer.class)
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="wrk_prefix")
public class DefData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pr_id")
    private Long id;
    @Column(name="code")
    private String code;
    @Column(name="prefix_start")
    private String numb;
    @Column(name="prefix_end")
    private String nume;
    @Column(name="operator")
    private String operator;
    @Column(name="region")
    private String region;
    @Column(name="region_code")
    private Integer regionCode;
    @Column(name="branch")
    private String filial;
    @Column(name="rn")
    private String rn;
    @Column(name="status")
    private String status;
}

