package com.nexign.dmf.rss.rssvw.controller;

import com.nexign.dmf.rss.rssvw.DAO.SqlScripts;
import com.nexign.dmf.rss.rssvw.config.DBConnectConfig;
import com.nexign.dmf.rss.rssvw.config.UserDBSource;
import com.nexign.dmf.rss.rssvw.model.PriceListData;
import com.nexign.dmf.rss.rssvw.model.RapStat;
import com.nexign.dmf.rss.rssvw.model.RepDetails;
import com.nexign.dmf.rss.rssvw.service.PriceListSrv;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rssvw")
public class BisRepControl {
    @Autowired
    PriceListSrv priceList;
    @Autowired
    DBConnectConfig cfg;
    private final SqlScripts sqlScripts;

    @PostMapping("passwd")
    public int change_passwd(@RequestParam("user") String user, @RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass, HttpServletRequest req) {
        HttpSession session= req.getSession(false);
        log.info("change_passwd:user="+user);
        UserDBSource uds;
        try {
            uds = new UserDBSource();
            uds.changePasswd(user, oldPass, newPass, cfg);
            logOut(session);
            login(user, newPass, req);
            log.info("change_passwd:successfull change password");
        } catch (Exception ex) {
//            System.out.println("change_passwd:"+ex.getMessage());
            log.info("change_passwd:"+ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        log.info("change_passwd:end");
        return 0;
    }

    @PostMapping("trafficprice")
    public List<PriceListData> getTrafficPrice(@RequestParam(name = "val_tp") String val_tp, @RequestParam(name = "value") String value, @RequestParam(name = "date") String startDate, HttpSession session) {
        UserDBSource uds = (UserDBSource) session.getAttribute("datasource");
        List<PriceListData> data;
        if (uds == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "нет соединения с базой данных!");
        }
        data = priceList.getTrafficPrice(val_tp, value, startDate.replace("-", ""), (NamedParameterJdbcTemplate) uds.getJdbc());
        if (data == null || data.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Тарифный план не найден");
        return data;
    }

    @GetMapping(value = "rpname/{id}", produces = "text/plain;charset=UTF-8")
    public String ratePlanNameFromId(@PathVariable Long id, @RequestParam(name = "date") String startDate, HttpSession session) {
        UserDBSource uds = (UserDBSource) session.getAttribute("datasource");
        return priceList.getRtplNameFromId(uds.getJdbc(), id, startDate.replace("-", ""));
    }

    @PostMapping("login")
    public void login(@RequestParam("user") String userName, @RequestParam("pass") String userPass,HttpServletRequest req) {
        //HttpServletResponse res, HttpSession session
        HttpSession session= req.getSession(false);
        if(session==null) {
            session=req.getSession(true);
        }
        log.info("login: user=" + userName);
//        System.out.println("LogInProcess..");
        UserDBSource uds;
        try {
            uds = (UserDBSource) session.getAttribute("datasource");
            if (uds == null) {
                try {
                    uds = new UserDBSource(userName, userPass, cfg);
                    session.setAttribute("datasource", uds);
                    log.info("login successfull:"+uds.getUser().getName());
                } catch (Exception ex) {
                    log.info("login"+userName+":username or password incorrect");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username or password incorrect");
                }
            } else {
                log.info("login:database connect exist,jdbc=" + uds.getJdbc());
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username or password incorrect");
        }
    }

    @GetMapping("logout")
    public void logOut(HttpSession session) {
        try {
            UserDBSource uds = (UserDBSource) session.getAttribute("datasource");
            uds.closeDataSource();
            session.removeAttribute("datasource");
            session.invalidate();
            log.info("LogOut, user="+uds.getUser().getName());
        } catch (Exception ex) {
            log.info("logOut:database not connected");
        }
    }

    //--------------------------------------
    @PostMapping("rap")
    //  @ResponseBody
    public List<RapStat> form_11(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        UserDBSource uds = (UserDBSource) session.getAttribute("datasource");
        System.out.println("---------UDS--=" + uds);
        System.out.println("--------------BEFORE-----");
        if (uds == null) {
            System.out.println(" HERE--------------");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "нет соединения с базой данных!");
        }
        System.out.println("--------------AFTER-----");
        return sqlScripts.getRapData(request, response, (NamedParameterJdbcTemplate) uds.getJdbc());
    }

    //--------------------------------------
    @PostMapping("details")
    public List<RepDetails> form_2(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        UserDBSource uds = (UserDBSource) session.getAttribute("datasource");
        System.out.println("DETAILS--------------------");
        if (uds == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "нет соединения с базой данных!");
        }
        return sqlScripts.getDetailsData(request, response, (NamedParameterJdbcTemplate) uds.getJdbc());
    }
}

