package com.nexign.dmf.rss.ocsdb.services;

import com.nexign.dmf.rss.ocsdb.dto.LacRegionItem;
import com.nexign.dmf.rss.ocsdb.dto.LacRegionsData;
import com.nexign.dmf.rss.ocsdb.dto.ParamNewRmop;
import com.nexign.dmf.rss.ocsdb.entityes.*;
import com.nexign.dmf.rss.ocsdb.repositoryes.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@Slf4j
public class RtplSrv {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    @Autowired
    WrkRtplDefRepo rtpldefrepo;
    @Autowired
    WrkRtplHighDirRepo hiDirRepo;
    @Autowired
    SysLogsRepo slogRepo;
    @Autowired
    private RatePlanTariffsRepo rtplTrfRepo;
    @Autowired
    private RtplViewRepo rtplvwRepo;
    @Autowired
    private RatePlansRepo ratePlansRepo;
    @Autowired
    private WrkOperatorsRepo operRepo;
    @Autowired
    Utils utl;


    private Date StrToDate(String start_date) {
        Date dt = null;
        try {
            dt = SDF.parse(start_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public List<RtplView> getRtplDataFromWrkDef(String task) {
        List<RtplView> res = new ArrayList<>();
        try {
            Long id = getRtplIdFromRatePlanName(getStrPriceParam(getPriceParam(task), "НАЗВАНИЕ СПЕЦИАЛЬНОГО ТП"));
            String startDate;
            startDate = getStrPriceParam(getPriceParam(task), "Дата начала применения ТП").replaceAll("[-:\s]", "");
            res = rtplvwRepo.findByRtplIdAndEndDateGreaterThanEqual(id, StrToDate(startDate));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return res;
    }

    private String getStrPriceParam(List<WrkRtplDef> priceParam, String paramName) {
        return priceParam.stream()
                .filter(v -> v.getAttrdef().equalsIgnoreCase(paramName))
                .map(v -> v.getAttrvalue())
                .findFirst()
                .get();
    }

    public List<WrkRtplDef> uploadPriceList(String task, MultipartFile file) {
        List<WrkRtplDef> rtpDef = new ArrayList<>();
        InputStream is;
        System.out.println("uploadPriceList-INF:file="+file==null?"file is null":file.getOriginalFilename());
        try {
            is = file.getInputStream();
            rtpDef = readInputPriceList(task, is);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error upload file:" + file.getOriginalFilename());
            throw new RuntimeException(e);
        }
        return rtpDef;
    }

    public List<WrkRtplDef> readInputPriceList(String pTask, InputStream fis) {
        List<WrkRtplDef> rtpDef = new ArrayList<>();
        log.info("readInputPriceList(RtplSrv)..");
        XSSFWorkbook book;
        XSSFSheet sheet = null;
        try {
            book = new XSSFWorkbook(fis);
            for (int i = 0; i < book.getNumberOfSheets(); i++) {
                log.info("readInputPriceList(RtplSrv):" + book.getSheetName(i));
                if (!book.isSheetHidden(i) && !book.isSheetVeryHidden(i)) {
                    sheet = book.getSheetAt(i);
                    switch (sheet.getSheetName().trim().toUpperCase(Locale.ROOT)) {
                        case "ТП НОВЫЙ":
                        case "НОВЫЙ ТП":
                        case "ТП ИЗМЕНЕНИЕ":
                        case "ИЗМЕНЕНИЕ ТП":
                            log.info("readInputPriceList(RtplSrv):" + book.getSheetName(i) + "-change or create rate plan");
                            readPriceFromXlsSheed(sheet, pTask);
                            break;
                        case "ВЫСОКОЗАТРАТНЫЕ НАПРАВЛЕНИЯ":
                            log.info("readInputPriceList(RtplSrv):" + book.getSheetName(i) + "-read readHighCostDir");
                            readHighCostDir(sheet, pTask);
                            break;
                        default:
                            if (sheet.getSheetName().toUpperCase(Locale.ROOT).matches(".*ТП.*")) {
                                readPriceFromXlsSheed(sheet, pTask);
                            }
                            break;
                    }
                } else {
                    log.info("readInputPriceList(RtplSrv):" + book.getSheetName(i) + "-hidden");
                }
            }
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        rtpDef = rtpldefrepo.findByTaskAndDeldateIsNull(Long.valueOf(pTask));
        return rtpDef;
    }

    public List<WrkRtplDef> readWrkDef(String pTask) {
        List<WrkRtplDef> rtpDef = new ArrayList<>();
        try {
            rtpDef = rtpldefrepo.findByTaskAndDeldateIsNull(Long.valueOf(pTask));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return rtpDef;
    }

    private void readHighCostDir(XSSFSheet sheet, String pTask) {
        final int MAXCOLNUMBER = 10;
        final int DIRVAL = 1;
        final int CODEVAL = 2;
        final int ZONEVAL = 3;
        final int NOVAL = -1;

        List<WrkRtplHighDir> hiDirList = new ArrayList<>();
        Integer[] colValueMap = new Integer[MAXCOLNUMBER];
        Iterator<Row> it = sheet.iterator();
        Iterator<Cell> cells;
        Row row;
        row = it.next();
        cells = row.iterator();
//        for (int i = 0; i < row.getLastCellNum() && i < MAXCOLNUMBER; i++) {
        while (cells.hasNext()) {
            Cell cell = cells.next();
            int i = cell.getColumnIndex();
            if (cell.getCellType() == STRING) {
                switch (cell.getStringCellValue().trim().toUpperCase(Locale.ROOT)) {
                    case "НАПРАВЛЕНИЕ":
                        colValueMap[i] = DIRVAL;
                        break;
                    case "КОД НАПРАВЛЕНИЯ":
                        colValueMap[i] = CODEVAL;
                        break;
                    case "ВЫДЕЛЕНО ИЗ НАПРАВЛЕНИЯ":
                        colValueMap[i] = ZONEVAL;
                        break;
                    default:
                        colValueMap[i] = NOVAL;
                        break;
                }
            }
        }
        while (it.hasNext()) {
            String vals = new String();
            WrkRtplHighDir hiDirIt = new WrkRtplHighDir();
            row = it.next();
            cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                vals = utl.getXlsCellValue(cell);
                vals = vals.replace("[\n\r]", "");
                switch (colValueMap[cell.getColumnIndex()]) {
                    case DIRVAL:
                        hiDirIt.setDirection(vals.replaceAll(":", ""));
                        break;
                    case CODEVAL:
                        int i = vals.indexOf(".");
                        if (i > 0) {
                            hiDirIt.setCode(vals.substring(0, vals.indexOf(".")));
                        } else {
                            hiDirIt.setCode(vals);
                        }
                        break;
                    case ZONEVAL:
                        hiDirIt.setZoneExcl(vals);
                        break;
                    default:
                        break;
                }
            }
            if (hiDirIt.getDirection() != null && hiDirIt.getDirection().length() > 0
                    && hiDirIt.getCode() != null && hiDirIt.getCode().length() > 0
                    && hiDirIt.getZoneExcl() != null && hiDirIt.getZoneExcl().length() > 0) {
                hiDirIt.setTask(Integer.valueOf(pTask));
                hiDirIt.setNaviDate(new Date());
                hiDirList.add(hiDirIt);
            }
        }
        hiDirRepo.deleteByTask(pTask);
        hiDirRepo.saveAll(hiDirList);
    }

    private List<WrkRtplDef> readPriceFromXlsSheed(XSSFSheet sheet, String pTask) {
        log.info("readPriceFromXlsSheed(RtplSrv):..");
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<WrkRtplDef> rtpDef = new ArrayList<>();
        Iterator<Row> it = sheet.iterator();
        Long numr = 1l;
        String tmp_val = new String();
        while (it.hasNext()) {
            log.info("------------------------------");
            Row row = it.next();
            WrkRtplDef wrd = new WrkRtplDef(numr, "", "", "", Long.valueOf(pTask), new Date(), null);
            Iterator<Cell> cells = row.iterator();
            String vals = new String();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                vals = utl.getXlsCellValue(cell);
                switch (cell.getColumnIndex()) {
                    case 0:
                        wrd.setAttrdef(vals.replaceAll(":", ""));
                        break;
                    case 1:
                        wrd.setAttrvalue(vals);
                        break;
                    case 2:
                        wrd.setAttrvalue2(vals);
                        break;
                    default:
                        break;
                }
            }
            if (wrd.getAttrdef().equals("") && !(wrd.getAttrvalue().equals("") && wrd.getAttrvalue2().equals(""))) {
                wrd.setAttrdef(tmp_val);
            }
            if (!wrd.getAttrdef().equals("")) {
                if (!wrd.getAttrdef().equals("Направление")
                        &&
                        (wrd.getAttrvalue().toLowerCase().contains("минут")
                                || wrd.getAttrvalue().toLowerCase().contains("сообщен")
                                || wrd.getAttrvalue().toLowerCase().contains("байт")
                                || wrd.getAttrvalue().toLowerCase().contains("секунд")
                        )
                ) {
                    wrd.setAttrvalue(wrd.getAttrvalue().substring(0, wrd.getAttrvalue().indexOf(',')));
                    wrd.setAttrvalue2(wrd.getAttrvalue().substring(wrd.getAttrvalue().indexOf(',')));
                }
                rtpDef.add(wrd);
                tmp_val = wrd.getAttrdef();
                numr++;
            }
        }
        log.info("readPriceFromXlsSheed(RtplSrv):rtpDef.size=" + rtpDef.size());
        if (rtpDef.size() > 0) {
            rtpldefrepo.deleteByTask(pTask);
            rtpldefrepo.saveAll(rtpDef);
            rtpldefrepo.wrkRtplParsData(Integer.valueOf(pTask));
            rtpldefrepo.wrkRtplDefCheckMapping(Integer.valueOf(pTask));
        }
        return rtpDef;
    }

    public List<SysLog> checkPriceMapping(String task) {
        Date start_date = new Date();
        rtpldefrepo.wrkRtplParsData(Integer.valueOf(task));
        rtpldefrepo.wrkRtplDefCheckMapping(Integer.valueOf(task));
        return slogRepo.getLogSetRtpl(Integer.valueOf(task), SDF.format(start_date));
    }

    public List<SysLog> setRatePlans(String task) {
        Date startDate = new Date();
        try {
            rtpldefrepo.wrkSetRatePlans(Integer.valueOf(task));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return slogRepo.getLogSetRtpl(Integer.valueOf(task), SDF.format(startDate));
    }

    public List<WrkRtplDef> getPriceParam(String task) {
        List<WrkRtplDef> data = rtpldefrepo.findByTaskAndDeldateIsNull(Long.valueOf(task));
        return data;
    }

    public Long getRtplIdFromRatePlanName(String name) {
        return ratePlansRepo.findRatePlansFromRtplName(name).getRtplId();
    }

    public List<RatePlanTariffs> getTariffs(Long id) {
        return rtplTrfRepo.getTariffs(id);
    }

    public List<RatePlanTariffs> getTariffsForName(String name) {
        List<RatePlanTariffs> tariffs = (List<RatePlanTariffs>) rtplTrfRepo.getTariffs(ratePlansRepo.findRatePlansFromRtplName(name).getRtplId());
        return tariffs;
    }

    public List<SysLog> addLacReg(LacRegionsData lacd,String username) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = slogRepo.getDate();
        try {
            for (LacRegionItem lac : lacd.getLacList()) {
//                System.out.println("addLacReg:task=" + lacd.getTask() + ",userName=" + username + ",lac=" + lac.getLac() + ",branch=" + lac.getBranch() + ",region=" + lac.getRegion());
                rtpldefrepo.addLacRegions(lac.getLac(), lac.getRegion(), username, lacd.getTask());
            }
            sysLogList = slogRepo.getLogData("RFC-" + lacd.getTask(), utl.DateToStr(processBeginDate));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

    public List<SysLog> addRmop(ParamNewRmop rmop,String username) {
        List<SysLog> sysLogList = new ArrayList<>();
        Date processBeginDate = slogRepo.getDate();
        try {
            operRepo.addRmop(
                    rmop.getTapCode()
                    , rmop.getName()
                    , rmop.getImsi()
                    , rmop.getHrsRtplId()
                    , rmop.getStartDate()
                    , rmop.getTmplTapCode()
                    , username
                    , rmop.getTask()
            );
            sysLogList = slogRepo.getLogData("RFC-" + rmop.getTask(), utl.DateToStr(processBeginDate));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysLogList;
    }

}
