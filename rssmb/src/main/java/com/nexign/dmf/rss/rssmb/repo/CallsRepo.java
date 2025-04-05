package com.nexign.dmf.rss.rssmb.repo;


import com.nexign.dmf.rss.rssmb.model.Calls;
import com.nexign.dmf.rss.rssmb.model.ChargeData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CallsRepo extends JpaRepository<Calls, Long> {

    public List<Calls> findByHpmnfileid(Long id);

//    @Query(value = "select new com.nexign.dmf.rss.rssmb.model.ChargeData(sum(outcharge) as charge,sum(outtax) as tax) from Calls where hpmnfileid=:hpmn_file_id and del_date is null")
//    public ChargeData getCallsChargeOut(@Param("hpmn_file_id") Long id);

    @Query(value = "select c.call_event_memo from calls c where c.call_id=:id and c.start_time=:start_time", nativeQuery = true)
    public String getCdr(@Param("id") Long id, @Param("start_time") Date start_time);

    public Calls findByCallidAndStarttime(Long call_id, Date start_time);

    public Calls findByCallidAndStarttimeAndDeldateIsNull(Long call_id, Date start_time);
    public Calls findByImsiAndStarttimeAndCltpidAndDeldateIsNull(String imsi,Date startTime,Integer cltpId);
    public Calls findByImsiAndStarttimeAndCltpidAndDurationAndDeldateIsNull(String imsi, Date starttime, Integer cltpid, Long duration);
    public Calls findByImsiAndCltpidAndDurationAndDeldateIsNull(String imsi, Integer cltpid, Long duration);

    @Modifying
    @Transactional
    @Query(value = "update calls c set c.del_date=:del_date,c.del_user=:navi_user where c.call_id=:call_id and c.start_time=:start_time", nativeQuery = true)
    public int rollBackCalls(@Param("call_id") Long call_id, @Param("start_time") Date start_time, @Param("navi_user") String navi_user, @Param("del_date") Date del_date);

    @Query(value = "select * from calls c where c.imsi=:imsi and start_time>=:startTime", nativeQuery = true)
    public List<Calls> getCallsForImsiAndStartTime(@Param("imsi") String imsi, @Param("startTime") Date startTime);

}
