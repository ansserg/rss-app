package com.nexign.dmf.rss.ocsdb.repositoryes;


import com.nexign.dmf.rss.ocsdb.entityes.RatePlans;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RatePlansRepo extends CrudRepository<RatePlans,Long> {


    final Integer RPTP_REPRICING=2;
    List<RatePlans> findByEndDateGreaterThanEqual(Date endDate);
    @Query(value="select * from rate_plans rp where rp.end_date>sysdate and rp.rptp_rptp_id=2",nativeQuery = true)
    List<RatePlans> findRepricingRatePlans();
   @Query(value="select rp.name_r from rate_plans rp where rp.rtpl_id=:id and rp.end_date>sysdate+30",nativeQuery = true)
    String findRtplName(Long id);
    @Query(value = "select *  from rate_plans where name_r=:name and end_date>sysdate",nativeQuery = true)
    RatePlans findRatePlansFromRtplName(String name);


}
