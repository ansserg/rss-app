package com.nexign.dmf.rss.rssmb.utils;

import com.nexign.dmf.rss.rssmb.model.Calls;
import com.nexign.dmf.rss.rssmb.model.CdrZone;
import com.nexign.dmf.rss.rssmb.repo.CallsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class Utils {
    @Value("${cdr.index.start_time}")
    private int CDR_START_TIME_IDX;
    @Value("${cdr.dir_name}")
    private String CDR_DIRNAME;
    @Value("${cdr.filename.prefix.bis}")
    private String FILENAME_PREFIX_BISDB;
    @Value("${cdr.filename.prefix.rsoot}")
    private String FILENAME_PREFIX_RSOOT;
    @Value("${app.filename.prefix.bis.test}")
    private String FILENAME_PREFIX_BIS_TEST;
    @Value("${cdr.index.lac}")
    private int CDR_LAC_IDX;
    @Value("${cdr.lac.rsoot}")
    private String CDR_LAC_RSOOT;

    @Autowired
    CallsRepo callsRepo;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public Date StrToDate(String start_date) {
        Date dt = null;
        try {
            dt = DF.parse(start_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public String DateToStr(Date date) {
        String dateStr;
        try {
            dateStr = SDF.format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dateStr;
    }

    public Date IncDate(Date date, int off,int fild) {
        System.out.println("IncDate:I-date="+date.toString()+",off="+off+",fild="+fild);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(fild, off);
        System.out.println("IncDate:I-cal_getTime="+cal.getTime().toString());
        return cal.getTime();
    }

    public Date DateUtc(Date date, String utcOffset) {
        System.out.println("DateUtc:I-date="+date.toString()+",utcoffset="+utcOffset);
        int offset = 0;
        if (
                utcOffset != null
                        && utcOffset.length() == 5
                        && (utcOffset.charAt(0) == '+' || utcOffset.charAt(0) == '-')
        ) {
            System.out.println("DateUtc:I-utcoffset validate=Ok");
            try {
                offset = (int) Integer.valueOf(utcOffset.substring(1,3));
                if (utcOffset.charAt(0) == '+') {
                    offset *= -1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                new RuntimeException(e.getMessage());
            }
        } else {
            System.out.println("DateUtc:I-utcoffset validate=Err");
        }
        return IncDate(date, offset,Calendar.HOUR);
    }

    public String incStartTime(String cdr, long val) {
        String[] filds = cdr.split(",");
        LocalDateTime start_time = LocalDateTime.parse(filds[CDR_START_TIME_IDX], DTF).plusSeconds(val);
        filds[CDR_START_TIME_IDX] = start_time.format(DTF);
        return String.join(",", filds);
    }

    public String updateCdrField(String cdr, int pos, String s) {
        String[] fields = cdr.split(",");
        fields[pos] = s;
        return String.join(",", fields);
    }

    public void creCdrFile(List<String> cdrList, boolean fileTypeProm) {
        Path pathDir = Paths.get(CDR_DIRNAME);
        if (cdrList.size() > 0) {
            try {
                if (!Files.exists(pathDir)) {
                    Files.createDirectories(pathDir);
                }
                if (fileTypeProm) {
                    putFile(CdrZone.BISDB, cdrList, fileTypeProm);
                    putFile(CdrZone.RSOOT, cdrList, fileTypeProm);
                } else {
                    putFile(CdrZone.BISDB, cdrList, fileTypeProm);
                    putFile(CdrZone.RSOOT, cdrList, fileTypeProm);
                }
                log.info("creCdrFile:end");
            } catch (Exception e) {
                log.error("creCdrFile:" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void putFile(CdrZone zone, List<String> cdrList, boolean fileTypeProm) {
        Path fileOut;
        List<String> cdrFile;
        String prefix = fileTypeProm ? "" : FILENAME_PREFIX_BIS_TEST;
        prefix += zone.equals(CdrZone.BISDB) ? FILENAME_PREFIX_BISDB : FILENAME_PREFIX_RSOOT;
        String fileName = CDR_DIRNAME + prefix + DTF.format(LocalDateTime.now()) + "." + "0001" + ".txt";
        try {
            cdrFile = cdrList.stream()
                    .filter(v -> getCdrZone(v).equals(zone))
                    .collect(toList());
            if (cdrFile.size() > 0) {
                fileOut = Paths.get(fileName);
                Files.write(fileOut, cdrFile);
                log.trace("putFile: " + fileName);
            }
        } catch (Exception e) {
            log.error("putFile:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private CdrZone getCdrZone(String cdr) {
        String[] s = cdr.split(",");
        return s[CDR_LAC_IDX].equals(CDR_LAC_RSOOT) ? CdrZone.RSOOT : CdrZone.BISDB;
    }

    public Calls getCalls(Long callId, Date startTime) {
        try {
            return callsRepo.findByCallidAndStarttime(callId, startTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
