package com.nexign.dmf.rss.rssmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sys_logs")
public class SysLog {
   @Id
   @Column(name="SLOG_ID")
   private Long slogId;
   @Column(name="LOG_DATE")
   private Date logDate;
   @Column(name="message")
   private String message;
   @Column(name="apmt_apmt_id")
   private Long apmt;
   @Column(name="process")
   private String process;
}
