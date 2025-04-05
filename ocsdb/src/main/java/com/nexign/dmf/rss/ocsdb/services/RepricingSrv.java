package com.nexign.dmf.rss.ocsdb.services;

import com.nexign.dmf.rss.ocsdb.dto.ParamPriceRepricing;
import com.nexign.dmf.rss.ocsdb.entityes.RatePlans;
import com.nexign.dmf.rss.ocsdb.entityes.RoundTypes;
import com.nexign.dmf.rss.ocsdb.entityes.SysLog;
import com.nexign.dmf.rss.ocsdb.entityes.WrkRtplPriceRepricing;
import com.nexign.dmf.rss.ocsdb.repositoryes.RatePlansRepo;
import com.nexign.dmf.rss.ocsdb.repositoryes.RoundTypesRep;
import com.nexign.dmf.rss.ocsdb.repositoryes.SysLogsRepo;
import com.nexign.dmf.rss.ocsdb.repositoryes.WrkRtplPriceRepricingRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class RepricingSrv {

    @Autowired
    Utils utl;
    @Autowired
    WrkRtplPriceRepricingRepo repr;
    @Autowired
    SysLogsRepo slogRepo;
    @Autowired
    RoundTypesRep roundTypesRep;
    @Autowired
    RatePlansRepo ratePlansRepo;
    @Autowired
    Utils utils;

    public List<WrkRtplPriceRepricing> uploadRePriceList(String task, MultipartFile file, String user) throws IOException {
        List<WrkRtplPriceRepricing> priceList = null;
        XSSFWorkbook book;
        XSSFSheet sheet = null;

        book = new XSSFWorkbook(file.getInputStream());
        for (int i = 0; i < book.getNumberOfSheets(); i++) {
            sheet = book.getSheetAt(i);
            switch (sheet.getSheetName().trim().toUpperCase(Locale.ROOT)) {
                case "ТАРИФЫ":
                case "ЛИСТ1":
                    log.info("readInputPriceList(RtplSrv):" + book.getSheetName(i) + "-change or create rate plan");
                    priceList = readRePriceFromXlsSheed(sheet, task, user);
                    break;
                default:
                    break;
            }
        }

        if (book != null) {
            book.close();
        }
        return priceList;
    }

    private List<WrkRtplPriceRepricing> readRePriceFromXlsSheed(XSSFSheet sheet, String task, String user) {
        List<WrkRtplPriceRepricing> priceList = new ArrayList<>();
        HashMap<Integer, String> colValMap = new HashMap<>();
        HashMap<String, Integer> valName = new HashMap<>();

        HashMap<String, String> roundTypes = new HashMap<>();
        Iterator<Row> it = sheet.iterator();
        Iterator<Cell> cells;
        Row row;

        row = it.next(); //первая строка игнорируется
        row = it.next(); //названия колонок начинается со второй строки
        cells = row.iterator();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            colValMap.put(cell.getColumnIndex(), cell.getStringCellValue().trim().toUpperCase(Locale.ROOT));
            valName.put(cell.getStringCellValue().trim().toUpperCase(Locale.ROOT), cell.getColumnIndex());
        }

//        row = it.next(); // после названия колонок строка с типами округления
//        cells = row.iterator();
//        while (cells.hasNext()) {
//            Cell cell = cells.next();
//            roundTypes.put(colValMap.get(cell.getColumnIndex()), utl.getXlsCellValue(cell));
//        }

        try {
            // далее - тарифы по странам
            while (it.hasNext()) {
                WrkRtplPriceRepricing rd = new WrkRtplPriceRepricing();
                row = it.next();
                rd.setCouname(getStringCellVal(row, "NAME_R", valName));
                rd.setMocl(getBigDecimalCelVal(getStringCellVal(row, "MOC_L", valName)));
                rd.setMocr(getBigDecimalCelVal(getStringCellVal(row, "MOC_R", valName)));
                rd.setMoco(getBigDecimalCelVal(getStringCellVal(row, "MOC_O", valName)));
                rd.setMocs(getBigDecimalCelVal(getStringCellVal(row, "MOC_S", valName)));
                rd.setMtc(getBigDecimalCelVal(getStringCellVal(row, "MTC", valName)));
                rd.setGprs(getBigDecimalCelVal(getStringCellVal(row, "GPRS", valName)));
                rd.setSmsmo(getBigDecimalCelVal(getStringCellVal(row, "SMSMO", valName)));
                rd.setTask(Long.valueOf(task));
                rd.setNaviuser(user);
                priceList.add(rd);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new RuntimeException("Ошибка чтения прайс-листа. " + e.getMessage());
        }
        repr.deleteByTask(task);
        repr.saveAll(priceList);
        log.info("readRePriceFromXlsSheed:" + colValMap);
        log.info("readRePriceFromXlsSheed:RoundTypes:" + roundTypes);
        priceList.stream().forEach(v -> log.info(v.toString()));
        return priceList;
    }

    private String getStringCellVal(Row row, String name, HashMap<String, Integer> valName) {
        if (null == valName.get(name)) {
            return null;
        }
        return utl.getXlsCellValue(row.getCell(valName.get(name))).isEmpty() ? "0" : utl.getXlsCellValue(row.getCell(valName.get(name)));
    }

    private BigDecimal getBigDecimalCelVal(String val) {
        return val == null || val.isEmpty() ? null : new BigDecimal(val);
    }

    public List<WrkRtplPriceRepricing> readWrkPriceRepricing(String task) {
        List<WrkRtplPriceRepricing> priceDataList = new ArrayList<>();
        try {
            priceDataList = repr.findByTask(Long.valueOf(task));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return priceDataList;
    }

    public List<SysLog> checkRepricingXlsData(String task) {
        System.out.println("RepricingSrv.checkRepricingXlsData-I:begin");
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = slogRepo.getDate();
        repr.validPLData(Integer.valueOf(task));
        sysLogList = slogRepo.getLogData("RFC-" + task, utl.DateToStr(processBeginDate));
        return sysLogList;
    }

    public List<RoundTypes> getRoundTypes() {
        try {
            return (List<RoundTypes>) roundTypesRep.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<RatePlans> getRtpl() {
        System.out.println("getRtpl..");
        try {
            return (List<RatePlans>) ratePlansRepo.findRepricingRatePlans();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<SysLog> changePrices(ParamPriceRepricing ppr, String username) {
        System.out.println("RepricingSrv:begin..");
        List<SysLog> slData = new ArrayList<>();
        Date processBeginDate = slogRepo.getDate();
        System.out.println("changePrices..");
        System.out.println("rtplId=" + ppr.getRtplId() + ",rndtvId=" + ppr.getRndtvId() + ",rndtdId=" + ppr.getRndtdId() + ",startDate=" + ppr.getStartDate() +
                ",task=" + ppr.getTask() + ",userName=" + username + ",startDateStr=" + ppr.getStartDateStr());
        try {
            repr.changePrices(ppr.getRtplId(), ppr.getRndtvId(), ppr.getRndtdId(), utils.StrToDate(ppr.getStartDateStr()), ppr.getTask(), username);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("changePrices-Ok");
        slData = slogRepo.getLogData("RFC-" + ppr.getTask(), utl.DateToStr(processBeginDate));
        return slData;
    }

    public List<SysLog> addNewRtpl(ParamPriceRepricing ppr, String username) {
        System.out.println("RepricingSrv:begin..");
        List<SysLog> slData = new ArrayList<>();
        Date processBeginDate = slogRepo.getDate();
        System.out.println("changePrices..");
        System.out.println("rtplId=" + ppr.getRtplId() + ",rndtvId=" + ppr.getRndtvId() + ",rndtdId=" + ppr.getRndtdId() + ",startDate=" + ppr.getStartDate() +
                ",task=" + ppr.getTask() + ",userName=" + username + ",startDateStr=" + ppr.getStartDateStr());
        try {
            System.out.println("addNewRtpl..");
            repr.addNewRtpl("",ppr.getRtplName(), ppr.getRndtvId(), ppr.getRndtdId(), utils.StrToDate(ppr.getStartDateStr()), ppr.getTask(), username);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("changePrices-Ok");
        slData = slogRepo.getLogData("RFC-" + ppr.getTask(), utl.DateToStr(processBeginDate));
        return slData;
    }
}
