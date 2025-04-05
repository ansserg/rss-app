package com.nexign.dmf.rss.ocsdb.repositoryes;

import com.nexign.dmf.rss.ocsdb.entityes.RatePlanTariffs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatePlanTariffsRepo extends CrudRepository<RatePlanTariffs, String> {
    @Query(value = "select * from wrk_web_srv_rate_plan_tariffs where rtpl_rtpl_id=:id and end_date>sysdate", nativeQuery = true)
    List<RatePlanTariffs> getTariffs(Long id);

}
