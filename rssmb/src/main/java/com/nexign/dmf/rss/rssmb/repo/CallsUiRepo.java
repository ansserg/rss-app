package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.model.CallsUi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;;

import java.util.Date;

public interface CallsUiRepo extends CrudRepository<CallsUi, Long> {
    public void deleteByCallCallIdAndStartTimeUtc(Long call_call_id, Date start_time);
    @Query(value = "update calls_ui set del_date_4ui=sysdate where call_call_id=:call_call_id and start_time_utc=:start_time_utc", nativeQuery = true)
    public void setDelDate(@Param("call_call_id") Long call_call_id, @Param("start_time_utc") Date start_time_utc);
}
