package com.nexign.dmf.rss.ocsdb.services;

import com.nexign.dmf.rss.ocsdb.entityes.DefData;
import com.nexign.dmf.rss.ocsdb.entityes.SysLog;
import com.nexign.dmf.rss.ocsdb.repositoryes.DefDataRepo;
import com.nexign.dmf.rss.ocsdb.repositoryes.SysLogsRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
@Slf4j
public class DefSrv {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    final int MAXCOLNUMBER = 24;
    final int CODEVAL = 1;
    final int NUMB = 2;
    final int NUME = 3;
    final int OPERVAL = 4;
    final int BRNCVAL = 5;
    final int REGNAMEVAL = 6;
    final int REGCODEVAL = 7;
    final int RNVAL = 8;
    final int STATUSVAL = 9;
    final int NOVAL = -1;
    Integer[] colValueMap = new Integer[MAXCOLNUMBER];
    List<DefData> defList = new ArrayList<>();
    List<DefData> resData = new ArrayList<>();
    @Autowired
    Utils utl;
    @Autowired
    DefDataRepo ddr;
    @Autowired
    SysLogsRepo slogRepo;
    public List<DefData> uploadDefRU(MultipartFile file, String task) {
        InputStream is;
        try {
            is = file.getInputStream();
            XSSFWorkbook book;
            XSSFSheet sheet = null;
            try {
                book = new XSSFWorkbook(is);
                sheet = book.getSheetAt(0);
                Iterator<Row> it = sheet.iterator();
                Iterator<Cell> cells;
                Row row;
                row = it.next();
                readHeadRow(row);
                readBody(sheet);
                book.close();
                ddr.truncateTable();
                ddr.saveAll(defList);
            } catch (Exception e) {
            }
            is.close();
        } catch (Exception e) {
        }
        return resData;
    }
    private void readBody(XSSFSheet sheet) {
        String vals = new String();
        DataFormatter formatter = new DataFormatter();
        Iterator<Row> it;
        Iterator<Cell> cells;
//        CellStyle cellStl=null;
//        XSSFColor color = null;
        defList.clear();
        resData.clear();
        it = sheet.rowIterator();
        it.next();
        while (it.hasNext()) {
            DefData defData = new DefData();
            Row row = it.next();
            cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
//                cellStl = cell.getCellStyle();
                try {
                    vals = formatter.formatCellValue(cell);
                    if (vals != null) {
                        vals = vals.replace("[\n\r]", "");
                        int idx = colValueMap[cell.getColumnIndex()];
                        if (idx > 0) {
                            switch (colValueMap[cell.getColumnIndex()]) {
                                case CODEVAL:
                                    defData.setCode(vals);
                                    break;
                                case NUMB:
                                    defData.setNumb(vals);
                                    break;
                                case NUME:
                                    defData.setNume(vals);
                                    break;
                                case OPERVAL:
                                    defData.setOperator(vals);
                                    break;
                                case BRNCVAL:
                                    defData.setFilial(vals);
                                    break;
                                case REGCODEVAL:
                                    defData.setRegionCode(Integer.valueOf(vals));
                                    break;
                                case REGNAMEVAL:
                                    defData.setRegion(vals);
                                    break;
                                case RNVAL:
                                    defData.setRn(vals);
                                    break;
                                case STATUSVAL:
                                    defData.setStatus(vals);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            if (defData.getCode() != null) {
                if (defData.getStatus() != null && defData.getStatus().length() > 0) {
                    resData.add(defData);
                }
                if (defData.getStatus() != null && !(defData.getStatus().equals("2") || defData.getStatus().toLowerCase().equals("d"))) {
                    defData.setStatus(null);
                }
                defList.add(defData);
            }
        }
    }
    private void readHeadRow(Row row) {
        Iterator<Cell> cells;
        cells = row.iterator();
        while(cells.hasNext()) {
            Cell cell = cells.next();
            int i=cell.getColumnIndex();
            if(cell.getCellType()==STRING) {
                switch (cell.getStringCellValue().trim().toUpperCase(Locale.ROOT)) {
                    case "DEF":
                        colValueMap[i] = CODEVAL;
                        break;
                    case "НАЧАЛО ДИАПАЗОНА":
                        colValueMap[i] = NUMB;
                        break;
                    case "ОКОНЧАНИЕ ДИАПАЗОНА":
                        colValueMap[i] = NUME;
                        break;
                    case "ОПЕРАТОР":
                        colValueMap[i] = OPERVAL;
                        break;
                    case "ПРИЗНАК ФИЛИАЛА МГФ":
                        colValueMap[i] = BRNCVAL;
                        break;
                    case "РЕГИОН":
                        colValueMap[i] = REGNAMEVAL;
                        break;
                    case "КОД РЕГИОНА":
                        colValueMap[i] = REGCODEVAL;
                        break;
                    case "RN":
                        colValueMap[i] = RNVAL;
                        break;
                    case "СТАТУС":
                        colValueMap[i] = STATUSVAL;
                        break;
                    default:
                        colValueMap[i] = NOVAL;
                        break;
                }
            }
        }
    }
    public List<SysLog> setDef(String task) {
        Date startDate = new Date();
        try {
            System.out.println("setDef..");
            ddr.defRu(Integer.valueOf(task));
        } catch (Exception e) {
            log.error(e.getMessage());
//            new RuntimeException(e);
        }
        return slogRepo.getLogData(task, SDF.format(startDate));
    }

    public List<DefData> getDefRU(String task) {
        return ddr.findStatusIsNotNull();
    }
}




