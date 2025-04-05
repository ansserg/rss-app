package com.nexign.dmf.rss.rssmb.controllers;

import com.nexign.dmf.rss.rssmb.dto.CallParam;
import com.nexign.dmf.rss.rssmb.dto.RmopParam;
import com.nexign.dmf.rss.rssmb.dto.RoamOpenParam;
import com.nexign.dmf.rss.rssmb.model.*;
import com.nexign.dmf.rss.rssmb.repo.RmopParamRepo;
import com.nexign.dmf.rss.rssmb.services.CallErrSrv;
import com.nexign.dmf.rss.rssmb.services.CallsSrv;
import com.nexign.dmf.rss.rssmb.services.RapSrv;
import com.nexign.dmf.rss.rssmb.services.RoamOperSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/rssmb")
@Slf4j
//@CrossOrigin()
public class RssbmController {

    @Autowired
    RmopParamRepo rmopRepo;
    @Autowired
    CallsSrv callsSrv;
    @Autowired
    private CallErrSrv callErrSrv;
    @Autowired
    RoamOperSrv roSrv;
    @Autowired
    RapSrv rapSrv;

    @PostMapping("calls/{sDate}")
    public List<ViewCalls> getCallsData(@PathVariable("sDate") String startDate, @RequestBody List<Long> rmopIdList) {
        List<ViewCalls> callPriceList = new ArrayList<>();
        List<ViewCalls> callData = new ArrayList<>();
        for (Long rmopId : rmopIdList) {
            try {
                callPriceList.clear();
                callPriceList = callsSrv.getViewCallsData(rmopId, startDate);
                if (callPriceList.size() > 0) {
                    callData.addAll(callPriceList);
                }
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        return callData;
    }

    ;

    @PostMapping("loadrmop/{startDate}")
    public List<RmopParam> loadRmop(@RequestBody List<String> pTapList, @PathVariable("startDate") String startDate) {
        RmopParam rmop;
        List<RmopParam> rmopList = new ArrayList<>();
        for (String tap : pTapList) {
            rmop = rmopRepo.getRmopParam(tap, startDate);
            if (rmop != null) {
                rmopList.add(rmop);
            }
        }
        return rmopList;
    }

    @GetMapping("callerr")
    public List<ViewCallError> getCallErrList() {
        List<ViewCallError> callErrList = new ArrayList<>();
        try {
            callErrList = callErrSrv.getCallErrorRecords();
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return callErrList;
    }

    ;

    @PostMapping("callerrproc")
    public List<CallParam> callErrProc(@RequestBody List<CallParam> callParam,@AuthenticationPrincipal Jwt jwt) {
        List<CallParam> res = new ArrayList<>();
        try {
            callErrSrv.creCdrFromCallErr(callParam,jwt.getClaim("preferred_username"));
            res = callErrSrv.findCallErrors(callParam);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return res;
    }

    @PostMapping("callerrcheck")
    public List<CallParam> callErrCheck(@RequestBody List<CallParam> callParam) {
        List<CallParam> res = new ArrayList<>();
        try {
            res = callErrSrv.findCallErrors(callParam);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return res;
    }

    @PostMapping("sethrsrtpl/{rtplId}/{startDate}/{user}/{task}")
    public @ResponseBody List<SysLog> setHrsRtplId(
            @RequestBody List<Long> rflwIdList,
            @PathVariable("rtplId") Long rtplId,
            @PathVariable("startDate") String startDate,
            @PathVariable("user") String userName,
            @PathVariable("task") Integer task,
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<SysLog> sysLogData=new ArrayList<>();
        String jwt_username=jwt.getClaim("preferred_username");
        try {
            sysLogData=roSrv.setHrsRtplId(rflwIdList, rtplId, startDate, jwt_username,task);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return sysLogData;
    }

    @GetMapping("getrap")
    public List<ViewRap> getViewRAP() {
        List<ViewRap> rapList = new ArrayList<>();
        try {
            rapList = rapSrv.getVwRap();
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return rapList;
    }

    @PostMapping("getmsgrap")
    public List<String> getMsgRap(@RequestBody List<ViewRap> rapList) {
        List<String> strListOut = new ArrayList<>();
        try {
            strListOut = rapSrv.getMsgRap(rapList);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return strListOut;
    }

    @PostMapping("rapreproc/{naviUser}")
    List<Calls> rapReProc(@RequestBody List<ViewRap> vwRapList, @PathVariable("naviUser") String user,@AuthenticationPrincipal Jwt jwt) {
        List<Calls> callsList = new ArrayList<>();
        String jwt_username=jwt.getClaim("preferred_username");
        rapSrv.rapProcessing(vwRapList, "WS:" + jwt_username);
        return callsList;
    }

    //    ------------------------Tests-------------------------------
    @GetMapping("test")
    public String test() {
        return "RssmbController-Ok!";
    }

    @PostMapping("test")
    public String getPostString(@RequestBody String str) {
        return "RSSMB POST TEST..str=" + str;
    }
}
