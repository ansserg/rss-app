package com.nexign.dmf.rss.rssmb.services;

import com.nexign.dmf.rss.rssmb.model.ViewCalls;
import com.nexign.dmf.rss.rssmb.repo.ViewCallsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CallsSrv {

    @Autowired
    ViewCallsRepo viewCallsRepo;

    public List<ViewCalls> getViewCallsData(Long rmopId, String startDate) {
        List<ViewCalls> callsPriceList=new ArrayList<>();
        try{
            callsPriceList=viewCallsRepo.findByCallsRtplStartTime(rmopId, startDate);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return callsPriceList;
    }
}
