package com.nexign.dmf.rss.ocsdb.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
@Slf4j
public class Utils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

    public String DateToStr(Date date) {
        String dateStr;
        try {
            dateStr = SDF.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dateStr;
    }

    public Date StrToDate(String start_date) {
        Date dt = null;
        try {
            dt = DF.parse(start_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public String getXlsCellValue(Cell cell) {
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String vals = null;
        try {
            switch (cell.getCellType()) {
                case STRING:
                    vals = cell.getStringCellValue().replaceAll(":", "");
                    log.info(vals);
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        vals = dateformat.format(cell.getDateCellValue());
                        log.info(vals);
                    } else {
                        vals = String.format("%f", cell.getNumericCellValue()).replace(",", ".");
                        log.info(String.valueOf(vals));
                    }
                    break;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case NUMERIC:
                            vals = String.format("%f", cell.getNumericCellValue()).replace(",", ".");
                            log.info(String.valueOf(vals));
                            break;
                        case STRING:
                            vals = cell.getStringCellValue().replace(":", "");
                            log.info(vals);
                            break;
                        default:
                            log.info("default value formula");
                    }
                    break;
                case BLANK:
                    vals = "";
                    log.info("blank type cell");
                    break;
                default:
                    vals = "";
                    String s1 = "undefined_cell_type: " + cell.getStringCellValue();
                    log.info(s1);
                    break;
            }
            vals = vals.replace("[\n\r]", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vals;
    }
}
