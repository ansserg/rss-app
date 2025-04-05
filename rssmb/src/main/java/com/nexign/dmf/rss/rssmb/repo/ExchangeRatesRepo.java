package com.nexign.dmf.rss.rssmb.repo;

import com.nexign.dmf.rss.rssmb.dto.ExchangeData;
import com.nexign.dmf.rss.rssmb.model.ExchangeRates;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public interface ExchangeRatesRepo extends CrudRepository<ExchangeRates,Long> {
    @Transactional
    @Procedure(name="addExchRate")
    void addExchRate(
            @Param("p_exch_rate_type") Integer ert,
            @Param("p_cur_code") String curCode,
            @Param("p_value") BigDecimal value,
            @Param("p_date") Date rateDate,
            @Param("p_task") Integer task,
            @Param("p_user_name") String userName
    );

}
