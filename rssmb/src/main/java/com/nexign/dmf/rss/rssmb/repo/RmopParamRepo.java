package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.dto.RmopParam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RmopParamRepo extends CrudRepository<RmopParam,Long> {
    @Query(value="select * from wrk_web_serv_rssmb_roam_param_vw t where t.short_name = :tapCode and to_date(:startDate, 'yyyymmdd') between t.start_date and t.end_date and rownum<2",nativeQuery = true)
    RmopParam getRmopParam(@Param("tapCode") String tapCode,@Param("startDate") String startDate);

    List<RmopParam> findByRtplIdAndEndDateAfter(Long priceRtplId, Date from);
}
