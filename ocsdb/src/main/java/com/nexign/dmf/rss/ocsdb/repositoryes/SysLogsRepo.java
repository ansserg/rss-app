package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.SysLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SysLogsRepo extends CrudRepository<SysLog,Long> {
    @Query(value = "select slog_id,log_date,message,apmt_apmt_id,process from sys_logs where message like '%'||:task||'%' and log_date>=to_date(:logDate,'yyyymmddhh24miss') order by slog_id",nativeQuery = true)
    List<SysLog> getLogSetRtpl(@Param("task") Integer task, String logDate);

    @Query(value = "select slog_id,log_date,message,apmt_apmt_id,process from sys_logs where process like '%'||:procName||'%' and log_date>=to_date(:logDate,'yyyymmddhh24miss') order by slog_id",nativeQuery = true)
    public List<SysLog> getLogData(@Param("procName") String procName, String logDate);
    @Query(value="select sysdate from dual",nativeQuery = true)
    public Date getDate();
}
