package com.nexign.dmf.rss.ocsdb.controllers;

import com.nexign.dmf.rss.ocsdb.dto.ParamPriceRepricing;
import com.nexign.dmf.rss.ocsdb.entityes.RatePlans;
import com.nexign.dmf.rss.ocsdb.entityes.RoundTypes;
import com.nexign.dmf.rss.ocsdb.entityes.SysLog;
import com.nexign.dmf.rss.ocsdb.entityes.WrkRtplPriceRepricing;
import com.nexign.dmf.rss.ocsdb.services.RepricingSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("ocsdb/repricing")
@Slf4j
public class OcsdbControllerRepricing {
    @Autowired
    RepricingSrv repricingSrv;

    @GetMapping("getRtplList")
    public @ResponseBody List<RatePlans> getRepricingRatePlans() {
        List<RatePlans> rtplList = new ArrayList<>();
        try {
            rtplList = repricingSrv.getRtpl();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return rtplList;
    }

    @PostMapping(value = "/repriceup")
    public @ResponseBody List<WrkRtplPriceRepricing> uploadRePriceList(
            @RequestParam("file") MultipartFile file,
            @RequestParam("task") String task,
            @AuthenticationPrincipal Jwt jwt
    ) {
        List<WrkRtplPriceRepricing> priceListData = null;
        System.out.println("repriceup->uploadRePriceList-inf:file_name=" + file.getOriginalFilename());

        try {
            priceListData = repricingSrv.uploadRePriceList(task, file,jwt.getClaim("preferred_username"));
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (priceListData.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error read price list - not found sheet in file");
        }
        return priceListData;
    }

    @GetMapping("checkpricelist/{task}")
    public List<SysLog> checkRepricingXlsData(@PathVariable String task) {
        return repricingSrv.checkRepricingXlsData(task);
    }

    @PostMapping("changePrices")
    public @ResponseBody List<SysLog> changePrice(@RequestBody ParamPriceRepricing ppr,@AuthenticationPrincipal Jwt jwt) {
        System.out.println("RestController:ocsdb/repricing/changePrice");
        List<SysLog> slData=null;
        try{
            slData=repricingSrv.changePrices(ppr,jwt.getClaim("preferred_username"));
        } catch(Exception e) {

        }
        return slData;
    }

    @PostMapping("addnewrtpl")
    public @ResponseBody List<SysLog> addNewRtpl(@RequestBody ParamPriceRepricing ppr,@AuthenticationPrincipal Jwt jwt) {
        System.out.println("RestController:ocsdb/repricing/changePrice");
        List<SysLog> slData=null;
        try{
            slData=repricingSrv.addNewRtpl(ppr,jwt.getClaim("preferred_username"));
        } catch(Exception e) {

        }
        return slData;
    }

    @GetMapping("readreprice/{task}")
    public List<WrkRtplPriceRepricing> readRePrice(@PathVariable String task) {
        List<WrkRtplPriceRepricing> dataList = new ArrayList<>();
        System.out.println("readreprice:task=" + task);
        try {
            dataList=repricingSrv.readWrkPriceRepricing(task);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error read price list");
        }
        return dataList;
    }

    @GetMapping("roundtypes")
    public @ResponseBody List<RoundTypes> getRoundTypes() {
        List<RoundTypes> rndtList = new ArrayList<>();
        try {
            rndtList = repricingSrv.getRoundTypes();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return rndtList;
    }

}
