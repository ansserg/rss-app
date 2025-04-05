package com.nexign.dmf.rss.rssmb.repo;


import com.nexign.dmf.rss.rssmb.model.CallErrors;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CallErrorsRepo extends CrudRepository<CallErrors,Long> {
    @Modifying
    @Transactional
    @Query(value="update call_errors set del_date=sysdate,del_user='WS:'||:username where call_call_id=:id and start_time=:startDate",nativeQuery = true)
    void updateDelDate(Long id, Date startDate,String username);

    CallErrors findByCallIdAndStartTime(Long callId, Date startTime);
    CallErrors findByCallIdAndStartTimeAndDelDateIsNull(Long callId, Date startTime);
}
