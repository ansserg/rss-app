package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilesData {
    @Id
    private Long fileId;
    private BigDecimal fileCharge;
    private BigDecimal fileTax;
    private Long hpmnRmopId;
    private Long vpmnRmopId;
    private String fileName;
}
