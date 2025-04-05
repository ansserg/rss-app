package com.nexign.dmf.rss.rssmb.controllers;

import com.nexign.dmf.rss.rssmb.dto.*;
import com.nexign.dmf.rss.rssmb.model.*;
import com.nexign.dmf.rss.rssmb.repo.CurrenciesRepo;
import com.nexign.dmf.rss.rssmb.repo.RegionsRepo;
import com.nexign.dmf.rss.rssmb.repo.RoamOperatorsRepo;
import com.nexign.dmf.rss.rssmb.services.LacSrv;
import com.nexign.dmf.rss.rssmb.services.RoamOperSrv;
import com.nexign.dmf.rss.rssmb.utils.Utils;
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
@RequestMapping("/rssmb/operator")
@Slf4j
//@CrossOrigin()
public class RoamOperatorsController {
    @Autowired
    Utils util;
    @Autowired
    RoamOperSrv roSrv;
    @Autowired
    RoamOperatorsRepo roRepo;
    @Autowired
    CurrenciesRepo curRepo;
    @Autowired
    LacSrv lacSrv;
    @Autowired
    RegionsRepo regRepo;

    //
    @PostMapping(value = "/uplac")
    public @ResponseBody List<LacRegionItem> uploadDef(@RequestParam("file") MultipartFile file, @RequestParam("task") String task) {
        List<LacRegionItem> lacList = new ArrayList<>();
        System.out.println("uploadLac..Ok");
        try {
            lacList = lacSrv.uploadLac(file, task);
            return lacList;

        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("addlacreg")
    public List<SysLog> addLacReg(@RequestBody LacRegionsData lacd, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> slList = new ArrayList<>();
        slList = roSrv.addLacReg(lacd, jwt.getClaim("preferred_username"));
        return slList;
    }

    @PostMapping("roamopen")
    public List<SysLog> roamOpen(@RequestBody RoamOpenParam rop, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> slList = new ArrayList<>();
        slList = roSrv.roamOpen(rop, jwt.getClaim("preferred_username"));
        return slList;
    }

    @PostMapping("addTestRmop")
    public @ResponseBody List<SysLog> addTestRmop(@RequestBody ParamNewRmop rmop, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> sl = new ArrayList<>();
        try {
            sl = roSrv.addTestRmop(rmop, jwt.getClaim("preferred_username"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return sl;
    }

    @PostMapping("sets4h")
    public @ResponseBody List<SysLog> setS4Hattr(@RequestBody ParamS4Hattr attr, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> sl = new ArrayList<>();
        try {
            sl = roSrv.setS4Hattr(attr, jwt.getClaim("preferred_username"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return sl;
    }

    @GetMapping("countries")
    public @ResponseBody List<Countries> getCountries() {
        List<Countries> couList = new ArrayList<>();
        try {
            couList = roSrv.getCountriesList();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return couList;
    }

    @GetMapping("roamoperators")
    public @ResponseBody List<RoamOperators> getRoamOperators() {
        List<RoamOperators> roList = new ArrayList<>();
        try {
            roList = roRepo.findAll();
        } catch (Exception e) {
            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка чтения таблицы roam_operators");
        }
        return roList;
    }

    @GetMapping("regions")
    public @ResponseBody List<Regions> getRegions() {
        List<Regions> regions = new ArrayList<>();
        try {
            regions = (List<Regions>) regRepo.findByDelDateIsNull();
        } catch (Exception e) {
            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка чтения таблицы roam_operators");
        }
        return regions;
    }

    @GetMapping("currencies")
    public @ResponseBody List<Currencies> getCurrencies() {
        List<Currencies> curList = new ArrayList<>();
        try {
            curList = (List<Currencies>) curRepo.findAll();
        } catch (Exception e) {
            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ошибка чтения справочниа currencies: " + e.getMessage());
        }
        return curList;
    }

    @PostMapping("addexch/{exchtp}")
    public @ResponseBody List<SysLog> exch23(@RequestBody ExchangeParam exchValue, @PathVariable("exchtp") Integer exchTp, @AuthenticationPrincipal Jwt jwt) {
        List<SysLog> sysLogData = new ArrayList<>();
        try {
            sysLogData = roSrv.addExch23(exchValue, exchTp, jwt.getClaim("preferred_username"));
        } catch (Exception e) {
            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return sysLogData;
    }
}
