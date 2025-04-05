package com.nexign.dmf.rss.rssmb.services;

import com.netflix.discovery.converters.Auto;
import com.nexign.dmf.rss.rssmb.model.BillCallsRap;
import com.nexign.dmf.rss.rssmb.model.ChargeData;
import com.nexign.dmf.rss.rssmb.model.RmopPoint;
import com.nexign.dmf.rss.rssmb.model.ViewRap;
import com.nexign.dmf.rss.rssmb.repo.*;
import com.nexign.dmf.rss.rssmb.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RapSrv {
    private static final DateTimeFormatter DTF_DD = DateTimeFormatter.ofPattern("yyMMdd");
    final static Integer ERR_CODE_200 = 200;
    final static Integer ERR_CODE_255 = 255;
    private static final Long checkHpmnFileId = Long.valueOf(String.valueOf(DTF_DD.format(LocalDateTime.now().minusDays(1)) + "0000000000"));
    @Autowired
    ViewRapRepo vwRR;
    @Autowired
    Utils utils;
    @Autowired
    BillCallsRapRepo billCallsRapRepo;
    @Autowired
    CallsRepo callsRepo;
    @Autowired
    FilesRepo filesRepo;
    @Autowired
    RoamOperatorsRepo roamOperatorsRepo;
    @Autowired
    CallsUiRepo callsUiRepo;

    public List<ViewRap> getVwRap() {
        List<ViewRap> vwRapList = new ArrayList<>();
        try {
            vwRapList = (List<ViewRap>) vwRR.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения wrk_web_serv_rap255rep_vw - " + e.getMessage());
        }
        return vwRapList;
    }

    public List<String> getMsgRap(List<ViewRap> data) {
        List<String> msg = new ArrayList<>();
        List<String> err_num = new ArrayList<>();
        Integer msg_num;
        err_num = data.stream()
                .map(value -> value.getErrorMsg())
                .distinct()
                .sorted()
                .collect(toList());
        msg_num = 0;
        for (String er : err_num) {
            Set<String> vpmn = new HashSet<>();
            String s = data.stream()
                    .filter(val -> val.getErrorMsg().equals(er))
                    .filter(val -> val.getFileName().startsWith("RCLUXMARUSNW"))
                    .map(val -> {
                        vpmn.add(val.getVpmnName());
                        return val.getHpmnName();
                    })
                    .distinct()
                    .sorted().collect(Collectors.joining(","));
            if (!s.isEmpty()) {
                msg.add(++msg_num + ".SEREVE.LUXMA(" + s + ")," + er
                        + (er.trim().startsWith("Error 255") ?
                        ",вызовы исправлены и будут перевыставлены" :
                        ",вызовы совершены на территории " + String.join(",", vpmn))
                );
            }
            List<RmopPoint> ss = data.stream()
                    .filter(val -> val.getErrorMsg().equals(er))
                    .filter(val -> !val.getFileName().startsWith("RCLUXMARUSNW"))
                    .map(val -> {
                        return new RmopPoint(val.getHpmnName(), val.getVpmnName());
                    })
                    .distinct()
                    .sorted(Comparator.comparing(RmopPoint::getHpmn))
                    .collect(toList());
            if (ss.size() > 0) {
                for (RmopPoint p : ss) {
                    msg.add(++msg_num + ".SEREVE."
                            + p.getHpmn()
                            + "," + er
                            + (er.trim().startsWith("Error 255") ? ",вызовы исправлены и будут перевыставлены" :
                            ",вызовы совершены на территории " + (p.getVpmn().equals("RUSNW") ? "МегаФон" : p.getVpmn())));
                }
            }
        }
        return msg;
    }

    public void rapProcessing(List<ViewRap> vwrap, String pUser) {
        List<String> cdrList = new ArrayList<>();
        Set<Long> hpmnFileIdList = new HashSet<>();
        Set<String> hpmnTapCodeList = new HashSet<>();
        Long timeOff;
        Date minStartDate = Collections.min(vwrap, Comparator.comparing(ViewRap::getStartTime)).getStartTime();
        Date maxStartDate = Collections.max(vwrap, Comparator.comparing(ViewRap::getStartTime)).getStartTime();
        cdrList = vwrap.stream()
                .filter(val -> val.getErrorCode().equals(ERR_CODE_200))
                .peek(val -> hpmnTapCodeList.add(val.getHpmnName()))
                .map(val -> utils.getCalls(val.getCallId(), val.getStartTime()))
                .filter(val -> val.getDeldate() == null)
                .peek(val -> {
                            hpmnFileIdList.add(val.getHpmnfileid());
                            checkAndAddBillCallsRap(val.getHpmnfileid(), val.getCallid(), val.getStarttime());
                            callsRepo.rollBackCalls(val.getCallid(), val.getStarttime(), pUser, new Date());
                            System.out.println("call_id=" + val.getCallid() + ",stat_time=" + val.getStarttime() + ",UTC-OFFSET=" + val.getUtcOffset() + ",start_time_utc=" + utils.DateUtc(val.getStarttime(), val.getUtcOffset()));
//                            callsUiRepo.deleteByCallCallIdAndStartTimeUtc(val.getCallid(), utils.DateUtc(val.getStarttime(), val.getUtcOffset()));
                            callsUiRepo.setDelDate(val.getCallid(), utils.DateUtc(val.getStarttime(), val.getUtcOffset()));
                        }
                )
                .map(val -> utils.incStartTime(val.getCdr(), 0L))
                .collect(toList());
        cdrList.addAll(
                vwrap.stream()
                        .filter(val -> val.getErrorCode().equals(ERR_CODE_255))
                        .peek(val -> hpmnTapCodeList.add(val.getHpmnName()))
                        .map(val -> utils.getCalls(val.getCallId(), val.getStartTime()))
                        .filter(val -> val.getDeldate() == null)
                        .peek(val -> {
                                    hpmnFileIdList.add(val.getHpmnfileid());
                                    checkAndAddBillCallsRap(val.getHpmnfileid(), val.getCallid(), val.getStarttime());
                                    callsRepo.rollBackCalls(val.getCallid(), val.getStarttime(), pUser, new Date());
//                                    callsUiRepo.deleteByCallCallIdAndStartTimeUtc(val.getCallid(), utils.DateUtc(val.getStarttime(), val.getUtcOffset()));
                                    callsUiRepo.setDelDate(val.getCallid(), utils.DateUtc(val.getStarttime(), val.getUtcOffset()));
                                }
                        )
                        .map(val -> utils.incStartTime(val.getCdr(), 1L))
                        .collect(toList())
        );
        if (cdrList.size() > 0) {
            updateFileCharge(hpmnFileIdList);
//            hpmnFileIdList.stream().forEach(System.out::println);
//            System.out.println("hpmnTapCodeList:");
//            hpmnTapCodeList.stream().forEach(System.out::println);
//            asyncAggrData(hpmnTapCodeList, minStartDate, maxStartDate);
            utils.creCdrFile(cdrList, true);
        }
    }

    @Async
    public int asyncAggrData(Set<String> hpmn_opers, Date start_time, Date end_time) {
        int ip_num = 0;
        log.trace("agrDataReprocessing start");
        String[] directions = new String[1];
        directions[0] = "S";
        String[] vpmnList = new String[1];
        String[] hpmnList = new String[1];

        vpmnList[0] = "RUSNW";
        hpmnList[0] = "RUST2";
        roamOperatorsRepo.massAggregator(start_time, end_time, 10000);
        log.trace("agrDataReprocessing success");
        return (0);
    }

    private void updateFileCharge(Set<Long> hpmn_list_file_id) {
        log.trace("updateFileCharge start");
        for (Long id : hpmn_list_file_id) {
            filesRepo.updateFileCharge(id);
        }
    }

    private void checkAndAddBillCallsRap(Long hpmnFileId, Long call_id, Date start_time) {
        BillCallsRap billCallsRap = new BillCallsRap();
        if (hpmnFileId.compareTo(checkHpmnFileId) < 0) {
            billCallsRap.setCall_call_id(call_id);
            billCallsRap.setStart_time(start_time);
            billCallsRapRepo.save(billCallsRap);
        }
    }
}
