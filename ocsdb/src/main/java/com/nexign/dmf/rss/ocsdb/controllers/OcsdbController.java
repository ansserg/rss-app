package com.nexign.dmf.rss.ocsdb.controllers;

import com.nexign.dmf.rss.ocsdb.dto.*;
import com.nexign.dmf.rss.ocsdb.entityes.DefData;
import com.nexign.dmf.rss.ocsdb.entityes.*;
import com.nexign.dmf.rss.ocsdb.repositoryes.RatePlansRepo;
import com.nexign.dmf.rss.ocsdb.services.CdrService;
import com.nexign.dmf.rss.ocsdb.services.DefSrv;
import com.nexign.dmf.rss.ocsdb.services.RepricingSrv;
import com.nexign.dmf.rss.ocsdb.services.RtplSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("ocsdb")
@Slf4j
//@CrossOrigin()
public class OcsdbController {
    @Autowired
    private RtplSrv rtplSrv;
    @Autowired
    private CdrService cdrSrv;
    @Autowired
    private DefSrv defSrv;
    @Autowired
    RatePlansRepo rtplRepo;
//    @Autowired
//    RepricingSrv repricingSrv;




    @PostMapping("addRmop")
    public List<SysLog> addRmop(@RequestBody ParamNewRmop rmop, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> slList = new ArrayList<>();
        slList = rtplSrv.addRmop(rmop, jwt.getClaim("preferred_username"));
        return slList;
    }

    @PostMapping("addlacreg")
    public List<SysLog> addLacReg(@RequestBody LacRegionsData lacd, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> slList = new ArrayList<>();
        System.out.println("user_name=" + jwt.getClaim("preferred_username"));
        slList = rtplSrv.addLacReg(lacd, jwt.getClaim("preferred_username"));
        return slList;
    }

    @PostMapping(value = "/updef")
    public @ResponseBody List<DefData> uploadDef(@RequestParam("file") MultipartFile file, @PathParam("task") String task) {
        System.out.println("uploadDEF..Ok");
        try {
            return defSrv.uploadDefRU(file, task);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/getdef/{task}")
    public @ResponseBody List<DefData> getDef(@PathVariable String task) {
        System.out.println("uploadDEF..Ok");
        try {
            return defSrv.getDefRU(task);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("setdef/{task}")
    public List<SysLog> setDef(@PathVariable String task) {
        return defSrv.setDef(task);
    }

    @PostMapping(value = "/priceup")
    //,produces = MediaType.APPLICATION_JSON_VALUE
    public @ResponseBody List<WrkRtplDef> uploadPriceList(@RequestParam("file") MultipartFile file, @RequestParam("task") String task
    ) {
        List<WrkRtplDef> priceListData = null;
        System.out.println("priceup->uploadPriceList-inf:" + file == null ? "file is null" : "file is not null");
        try {
            priceListData = rtplSrv.uploadPriceList(task, file);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (priceListData.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error read price list - not found sheet in file");
        }
        return priceListData;
    }

    @GetMapping("readwrkdef/{task}")
    public List<WrkRtplDef> readWrkDef(@PathVariable String task) {
        List<WrkRtplDef> dataList = new ArrayList<>();
        try {
            dataList = rtplSrv.readWrkDef(task);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error read price list");
        }
        return dataList;
    }

    @GetMapping("checkPriceMapping/{task}")
    public List<SysLog> checkPriceMapping(@PathVariable String task) {
        return rtplSrv.checkPriceMapping(task);
    }

    @GetMapping("setRatePlans/{task}")
    public List<SysLog> setRtpl(@PathVariable String task) {
        return rtplSrv.setRatePlans(task);
    }

    @GetMapping("rtpldata/{task}")
    public List<RtplView> viewRtpl(@PathVariable String task) {
        List<RtplView> val = new ArrayList<>();
        try {
            val = rtplSrv.getRtplDataFromWrkDef(task);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return val;
    }

    @GetMapping("psets")
    public List<PrefixSets> test_get_prefix_sets() {
        List<PrefixSets> psLst;
        try {
            psLst = cdrSrv.get_pset();
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return psLst;
    }

    @PostMapping(value = "cdrtst/{date}", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> creteTestCdrs(@RequestBody List<RmopParam> rmopList, @PathVariable("date") String date) {
        List<String> cdrList;
        try {
            System.out.println("creteTestCdrs..");
            cdrList = cdrSrv.create_cdrtst(rmopList, date);
            System.out.println("creteTestCdrs end");
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        System.out.println("creteTestCdrs return");
        return cdrList;
    }

    @PostMapping(value = "/psdirparam/{rtplId}/{startDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PsetDirParam> psetDirParam(@RequestBody List<Long> psList, @PathVariable("rtplId") Long rtplId, @PathVariable("startDate") String startDate) {
        List<PsetDirParam> psetDirParam = new ArrayList<>();
        try {
            psetDirParam = cdrSrv.getPsetDirParam(psList, rtplId, startDate);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return psetDirParam;
    }

    @GetMapping(value = "getrtplid", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getRtplId(@RequestParam("name") String rtplName) {
        Long id = -1l;
        try {
            id = rtplSrv.getRtplIdFromRatePlanName(rtplName);
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return id;
    }

    @GetMapping("getListRatePlans")
    public @ResponseBody List<RtplList> getListRatePlans() {
        List<RtplList> rtplList = new ArrayList<>();
        Date sysDate = new Date();
        try {
            rtplList = rtplRepo.findByEndDateGreaterThanEqual(sysDate).
                    stream()
                    .map(v -> new RtplList(v.getRtplId(), v.getRtplName()))
                    .distinct()
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR);
        }
        return rtplList;
    }

    //---------------- tests ------------------------------------
    @PostMapping("tstpost")
    public String testPost() {
        return "tstPost";
    }

    //----------
    @GetMapping("get-testString")
    public String test() {
        return "Hello!";
    }

    //----------
    @GetMapping("get-testData")
    public List<TestData> testDataProperty() {
        List<TestData> testData = new ArrayList<>(Arrays.asList(
                new TestData(1l, "DirName1", "Descr1"),
                new TestData(2l, "DirName2", "Descr2"),
                new TestData(3l, "DirName3", "Descr3"),
                new TestData(4l, "DirName4", "Descr3"),
                new TestData(5l, "DirName5", "Descr3")
        ));
        return testData;
    }
}


