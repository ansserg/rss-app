package com.nexign.dmf.rss.rssmb.services;

import com.nexign.dmf.rss.rssmb.dto.LacRegionItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.STRING;


@Service
@Slf4j
public class LacSrv {
    final int MAXCOLNUMBER = 24;
    final int BRANCH = 1;
    final int REGION = 2;
    final int LAC = 3;
    final int NOVAL = -1;
    Integer[] colValueMap = new Integer[MAXCOLNUMBER];
    private List<LacRegionItem> lacList = new ArrayList<>();

    public List<LacRegionItem> uploadLac(MultipartFile file, String task) {
        InputStream is;
        XSSFWorkbook book;
        XSSFSheet sheet = null;
        try {
            is = file.getInputStream();
            book = new XSSFWorkbook(is);
            sheet = book.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            Iterator<Cell> cells;
            Row row;
            row = it.next();
            readHeadRow(row);
            readBody(sheet);
            book.close();
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return lacList.stream().distinct().collect(Collectors.toList());
    }

    private void readBody(XSSFSheet sheet) {
        lacList.clear();
        String vals = new String();
        DataFormatter formatter = new DataFormatter();
        Iterator<Row> it;
        Iterator<Cell> cells;
        it = sheet.rowIterator();
        it.next();
        while (it.hasNext()) {
            LacRegionItem lac = new LacRegionItem();
            Row row = it.next();
            cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                try {
                    vals = formatter.formatCellValue(cell);
                    if (vals != null) {
                        vals = vals.replace("[\n\r]", "");
                        int idx = colValueMap[cell.getColumnIndex()];
                        if (idx > 0) {
                            switch (colValueMap[cell.getColumnIndex()]) {
//                                case BRANCH:
//                                    lac.setBranch(vals);
//                                    break;
                                case REGION:
                                    lac.setRegion(vals);
                                    break;
                                case LAC:
                                    lac.setLac(vals);
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
            if (lac.getLac() != null && lac.getRegion() != null) {
//                if (lac.getLac() != null && lac.getBranch() != null && lac.getRegion() != null) {
                lacList.add(lac);
            }
        }
    }

    private void readHeadRow(Row row) {
        Iterator<Cell> cells;
        DataFormatter formatter = new DataFormatter();
        cells = row.iterator();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            int i = cell.getColumnIndex();
            switch (formatter.formatCellValue(cell).trim().replaceAll("[^A-Za-zА-Яа-я0-9]", "_").toUpperCase(Locale.ROOT)) {
                case "ФИЛИАЛ_ОБСЛУЖИВАНИЯ_БС":
                    colValueMap[i] = BRANCH;
                    System.out.println("BRANCH=" + i);
                    break;
                case "РЕГИОН_ОБСЛУЖИВАНИЯ_БС":
                    colValueMap[i] = REGION;
                    System.out.println("REGION=" + i);
                    break;
                case "LAC_TAC":
                    colValueMap[i] = LAC;
                    System.out.println("LAC=" + i);
                    break;
                default:
                    colValueMap[i] = NOVAL;
                    break;
            }
        }
    }
}
