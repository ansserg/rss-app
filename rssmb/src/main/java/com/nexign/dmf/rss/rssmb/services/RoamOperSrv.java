package com.nexign.dmf.rss.rssmb.services;

import com.nexign.dmf.rss.rssmb.dto.*;
import com.nexign.dmf.rss.rssmb.model.Countries;
import com.nexign.dmf.rss.rssmb.model.SysLog;
import com.nexign.dmf.rss.rssmb.repo.*;
import com.nexign.dmf.rss.rssmb.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class RoamOperSrv {

    private final String PROCESSNAME = "WRK_OPERATORS";
    @Autowired
    RoamOperHistRepo rohRepo;
    @Autowired
    RoamOperatorsRepo roRepo;
    @Autowired
    Utils util;
    @Autowired
    SysLogsRepo slr;
    @Autowired
    CountriesRepo couRepo;

    @Autowired
    ExchangeRatesRepo exchRepo;

    public List<SysLog> addLacReg(LacRegionsData lacd,String username) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = slr.getDate();
        try {
            for (LacRegionItem lac : lacd.getLacList()) {
                log.info("addLacReg:task=" + lacd.getTask() + ",userName=" + lacd.getUserName() + ",lac=" + lac.getLac() + ",region=" + lac.getRegion());
                roRepo.addLacRegions(lacd.getTask(), username, lac.getLac(), lac.getRegion());
            }
            sysLogList = slr.getLogData("RFC-" + lacd.getTask(), util.DateToStr(processBeginDate));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

    public List<SysLog> roamOpen(RoamOpenParam rop,String username) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = slr.getDate();
        try {
            roRepo.roamOpen(rop.getRoamTp(), rop.getTapCode(), rop.getRoamStates(), rop.getGprs() ? "Y" : "N", rop.getKk() ? "Y" : "N",
                    rop.getStartDate(), username, rop.getTask(), rop.getCurFile(), rop.getContractNum());
            sysLogList = slr.getLogData("RFC-" + rop.getTask(), util.DateToStr(processBeginDate));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

    public List<SysLog> setHrsRtplId(List<Long> rflwIdList, Long rtplId, String startDate, String userName,Integer task) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = new Date();
        Date date;
        try {
            date = util.StrToDate(startDate);
            for (Long rflwId : rflwIdList) {
                rohRepo.setHrsRtplId(rflwId, rtplId, date, userName,task);
            }
            sysLogList = slr.getLogData("RFC-" + task, util.DateToStr(processBeginDate));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка изменения HRS_RTPL_ID в RSSMB.ROAM_OPER_HIST - " + e.getMessage());
        }
        return sysLogList;
    }

    public List<SysLog> addTestRmop(ParamNewRmop ro,String username) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = new Date();
        try {
            roRepo.addTestRmop(
                    ro.getTapCode(), ro.getName(), ro.getCountry(), ro.getImsi(),
                    ro.getStartDate(), username, ro.getTask(), ro.getTmplTapCode(), ro.getHrsRtplId());
            sysLogList = slr.getLogData("RFC-" + ro.getTask(), util.DateToStr(processBeginDate));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

    public List<SysLog> setS4Hattr(ParamS4Hattr att,String username) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = new Date();
        try {
            roRepo.setS4Hattr(att.getTapCode(), att.getContrNum(), att.getVendor(), att.getDebitor(),
                    att.getSd(), att.getMm(), att.getCurInvOut(), att.getCurInvIn(), att.getTermInDays(), att.getStartDate(),
                    att.getTask(), username);
            sysLogList = slr.getLogData("RFC-" + att.getTask(), util.DateToStr(processBeginDate));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

    public List<Countries> getCountriesList() {
        try {
            return (List<Countries>) couRepo.findByCouCodeIsNotNull();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<SysLog> addExch23(ExchangeParam exchParam, Integer exchTp,String username) {
        Date processBeginDate = new Date();
        List<SysLog> sysLogList = new ArrayList<>();
        List<ExchangeData> exchDataList = exchParam.getExchData();
        try {
            for (ExchangeData data : exchDataList) {
                exchRepo.addExchRate(exchTp, data.getCurName(),
                        new BigDecimal(data.getCurValue().replace(',', '.').replaceAll(" ","")),
                        exchParam.getStartDate(), exchParam.getTask(), username);

            }
            sysLogList = slr.getLogData("RFC-" + exchParam.getTask(), util.DateToStr(processBeginDate));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

}
