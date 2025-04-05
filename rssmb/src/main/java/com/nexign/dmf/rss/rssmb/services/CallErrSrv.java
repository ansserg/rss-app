package com.nexign.dmf.rss.rssmb.services;


import com.nexign.dmf.rss.rssmb.dto.CallParam;
import com.nexign.dmf.rss.rssmb.model.CallErrors;
import com.nexign.dmf.rss.rssmb.model.Calls;
import com.nexign.dmf.rss.rssmb.model.CdrZone;
import com.nexign.dmf.rss.rssmb.model.ViewCallError;
import com.nexign.dmf.rss.rssmb.repo.CallErrorsRepo;
import com.nexign.dmf.rss.rssmb.repo.CallsRepo;
import com.nexign.dmf.rss.rssmb.repo.ViewCallErrorRepo;
import com.nexign.dmf.rss.rssmb.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.core.OrderComparator.sort;

@Service
@Slf4j
public class CallErrSrv {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Value("${cdr.index.active.code1}")
    private int CDR_ACTION_IDX1;
    @Value("${cdr.index.active.code2}")
    private int CDR_ACTION_IDX2;
    @Value("${calls.del_user}")
    private String DEL_USER;
    @Autowired
    ViewCallErrorRepo viewCallErrRepo;
    @Autowired
    CallErrorsRepo callErrRepo;
    @Autowired
    CallsRepo callsRepo;
    @Autowired
    Utils utils;

    public List<ViewCallError> getCallErrorRecords() {
        return (List<ViewCallError>) viewCallErrRepo.findAll();
    }

    public List<CallParam> findCallErrors(List<CallParam> cpl) {
        List<CallParam> callParam = new ArrayList<>();
        callParam = cpl.stream()
                .map(v -> {
                    try {
                        CallErrors ce = callErrRepo.findByCallIdAndStartTime(v.getCallId(), v.getStartTime());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(v.getStartTime());
                        cal.add(Calendar.SECOND, 1);
                        Calls c = callsRepo.findByImsiAndStarttimeAndCltpidAndDurationAndDeldateIsNull(
                                v.getImsi(), v.getCallErrType() == 1 ? cal.getTime() : v.getStartTime(), v.getCallType(), v.getDuration()
                        );
                        return new CallParam(
                                c != null ? c.getCallid() : null, c != null ? c.getStarttime() : null, v.getImsi(), v.getDuration(), v.getCallType(), v.getCallErrType(),
                                ce != null ? ce.getDelDate() : null, v.getNaviUser()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new CallParam();
                })
                .collect(toList());
        return callParam;
    }

    public void creCdrFromCallErrTest(List<CallParam> pCallParam) {
        List<String> cdrList = new ArrayList<>();
        List<String> cdrTests = new ArrayList<>();
        List<CallParam> sortedList = pCallParam.stream()
                .sorted(Comparator.comparing(CallParam::getCallErrType)
                        .thenComparing(CallParam::getCallType)
                        .thenComparing(CallParam::getImsi))
                .collect(toList());
        long dt = 0L;
        CallParam curVal = sortedList.get(0);
        for (CallParam v : sortedList) {
            if (!(
                    curVal.getCallErrType().equals(v.getCallErrType())
                            && curVal.getCallType().equals(v.getCallType())
                            && curVal.getImsi().equals(v.getImsi())
            )
            ) {
                dt = 1L;
                curVal = v;
            } else {
                dt += 1L;
            }
        }
    }

    public void creCdrFromCallErr(List<CallParam> pCallParam,String username) {
        List<String> cdrList = new ArrayList<>();
        List<String> cdrTests = new ArrayList<>();
        List<CallParam> sortedList = pCallParam.stream()
                .sorted(Comparator.comparing(CallParam::getCallErrType)
                        .thenComparing(CallParam::getCallType)
                        .thenComparing(CallParam::getImsi)
                        .thenComparing(CallParam::getStartTime))
                .collect(toList());
        int dt = 0;
        CallParam curVal = sortedList.get(0);
        try {
            for (CallParam v : sortedList) {
                if (!(
                        curVal.getCallErrType().equals(v.getCallErrType())
                                && curVal.getCallType().equals(v.getCallType())
                                && curVal.getImsi().equals(v.getImsi())
                )
                ) {
                    dt = 1;
                    curVal = v;
                } else {
                    dt++;
                }
                if(v.getCallErrType().equals(4)) {
                    while (Optional.ofNullable(callsRepo.findByImsiAndStarttimeAndCltpidAndDeldateIsNull(v.getImsi(), utils.IncDate(v.getStartTime(), dt, Calendar.SECOND), v.getCallType())).isPresent()) {
                        dt++;
                    }
                }
                CallErrors ce = callErrRepo.findByCallIdAndStartTimeAndDelDateIsNull(v.getCallId(), v.getStartTime());
                if (ce != null) {
                    String cdr = callsRepo.findByCallidAndStarttime(v.getCallId(), v.getStartTime()).getCdr();
                    switch (v.getCallErrType()) {
                        case (1):
                        case (4):
                            cdrList.add(utils.incStartTime(cdr, dt));
                            break;
                        case (2):
                            cdrTests.add(cdr);
                            break;
                        case (3):
//                            utils.updateCdrField(cdr, CDR_ACTION_IDX1, "2A");
//                            utils.updateCdrField(cdr, CDR_ACTION_IDX2, "2A");
//                            cdrList.add(cdr);
                            break;
                        default:
                            break;
                    }
                    callErrRepo.updateDelDate(v.getCallId(), v.getStartTime(), username);
                }
            }
            utils.creCdrFile(cdrList, true);
            utils.creCdrFile(cdrTests, false);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

