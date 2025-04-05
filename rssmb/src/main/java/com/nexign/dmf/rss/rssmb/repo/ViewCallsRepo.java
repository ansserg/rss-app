package com.nexign.dmf.rss.rssmb.repo;


import com.nexign.dmf.rss.rssmb.model.ViewCalls;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewCallsRepo extends CrudRepository<ViewCalls,Long> {
    @Query(value="select /*+ parallel(t,8) */ * from wrk_web_serv_calls_vw t where hpmn_rmop_id=:rmopId and trunc(start_time)=to_date(:startTime,'yyyymmdd')",nativeQuery = true)
    List<ViewCalls> findByCallsRtplStartTime(@Param("rmopId") Long rtplId, @Param("startTime") String startTime);
}
