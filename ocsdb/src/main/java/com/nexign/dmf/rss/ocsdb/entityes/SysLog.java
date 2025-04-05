package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="sys_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLog {
   @Id
   @Column(name="SLOG_ID")
   private Long slogId;
   @Column(name="LOG_DATE")
   private Date logDate;
   private String message;
   @Column(name="apmt_apmt_id")
   private Long apmt;
   @Column(name="process")
   private String process;
}
