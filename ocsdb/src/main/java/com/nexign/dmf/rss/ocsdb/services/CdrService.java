package com.nexign.dmf.rss.ocsdb.services;

import com.nexign.dmf.rss.ocsdb.dto.PsetDirParam;
import com.nexign.dmf.rss.ocsdb.dto.RmopParam;
import com.nexign.dmf.rss.ocsdb.entityes.PrefixSets;
import com.nexign.dmf.rss.ocsdb.entityes.Rpdr;
import com.nexign.dmf.rss.ocsdb.repositoryes.PsetRepo;
import com.nexign.dmf.rss.ocsdb.repositoryes.RatePlansRepo;
import com.nexign.dmf.rss.ocsdb.repositoryes.RpdrRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CdrService {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyyMMdd");
    @Value("${cdr.lac.bisdb}")
    private String LACRUSNW;
    @Value("${cdr.lac.rsoot}")
    private String LACRSOOT;
    @Value("${cdr.path.dir}")
    private String PATHCDRTESTDIR;
    @Value("${cdr.filename.template.bis}")
    private String BISFILENAMETEMPL;
    @Value("${cdr.filename.template.rsoot}")
    private String RSOOTFILENAMETEMPL;
    @Autowired
    PsetRepo psRepo;
    @Autowired
    RpdrRepo rpdrRepo;
    @Autowired
    RatePlansRepo rtplRepo;

    @Value("${cdr.index.lac}")
    private Integer CDRIDXLAC;

    public List<PrefixSets> get_pset() {
        List<PrefixSets> psList = new ArrayList<>();
        try {
            psList = (List<PrefixSets>) psRepo.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return psList;
    }

    public List<Rpdr> getRpdr(Long rtplId, String startDate) {
        List<Rpdr> rpdrList = new ArrayList<>();
        try {
            rpdrList = rpdrRepo.getRpdr(rtplId, startDate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return rpdrList;
    }

    public List<PsetDirParam> getPsetDirParam(List<Long> psList, Long rtplId, String startDate) {
        List<Rpdr> rpdrList = new ArrayList<>();
        List<PsetDirParam> psetDirParam = new ArrayList<>();
        try {
//            rpdrList=rpdrRepo.getRpdr(rtplId,startDate);
            rpdrList = rpdrRepo.getPsetDirParam(psList, rtplId, startDate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        psetDirParam = rpdrList.stream()
                .map(v -> new PsetDirParam(v.getPsetId(), v.getRpdrName(), v.getCouName()))
                .collect(Collectors.toList());
        return psetDirParam;
    }

    public String getRtplName(Long id) {
        String name;
        try {
            name = rtplRepo.findRtplName(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return name;
    }

    public List<String> create_cdrtst(List<RmopParam> rmop_lst, String v_start_time) throws BadAttributeValueExpException {
        List<String> cdr_lst = new ArrayList<>();
        List<PrefixSets> pset_lst = new ArrayList<>();
        String start_time;
        RmopParam rmop;
        //инициализация переменных
        cdr_lst.clear();
        pset_lst.clear();
        pset_lst = get_pset();
        start_time = v_start_time.replace("-", "") + "000000";
        rmop = rmop_lst.get(0);
        //формируем MOC-cdr для TAP-кода, первого из списка
        if (rmop != null) {
            int seq_num = 0;
            for (PrefixSets ps : pset_lst) {
                cdr_lst.add(create_cdrtst_moc(rmop.getTapCode(), rmop.getImsiStart(), start_time, ps.getPrefix(), LACRUSNW));
            }
            //формируем по одной MTC,SMSMO,GPRS cdr для TAP-кода, первого из списка
            cdr_lst.add(create_cdrtst_mtc(rmop.getTapCode(), rmop.getImsiStart(), start_time, LACRUSNW));
            cdr_lst.add(create_cdrtst_smsmo(rmop.getTapCode(), rmop.getImsiStart(), start_time, LACRUSNW));
            cdr_lst.add(create_cdrtst_gprs(rmop.getTapCode(), rmop.getImsiStart(), start_time, LACRUSNW));
            //формируем по одной cdr для TAP-кодов, из списка, начиная со второго
            for (int i = 1; i < rmop_lst.size(); i++) {
                cdr_lst.add(create_cdrtst_moc(rmop_lst.get(i).getTapCode(), rmop_lst.get(i).getImsiStart(), start_time, pset_lst.get(pset_lst.size() - 1).getPrefix(), LACRUSNW));
            }
            //формируем MOC-cdr для RSOOT
            start_time = incStartTime(start_time);
            cdr_lst.add(create_cdrtst_moc(rmop.getTapCode(), rmop.getImsiStart(), start_time, pset_lst.get(pset_lst.size() - 1).getPrefix(), LACRSOOT));
        } else throw new BadAttributeValueExpException(rmop_lst);
        try {
            creTestFile(rmop.getTapCode(), cdr_lst);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cdr_lst;
    }

    private void creTestFile(String tapCode, List<String> cdr_lst) throws IOException {
        if (cdr_lst.size() > 0) {
            List<String> bis_cdrs = cdr_lst.stream()
                    .filter(v -> v.split(",")[CDRIDXLAC].equals(LACRUSNW))
                    .collect(Collectors.toList());
            List<String> rsoot_cdrs = cdr_lst.stream()
                    .filter(v -> v.split(",")[CDRIDXLAC].equals(LACRSOOT))
                    .collect(Collectors.toList());
            if (bis_cdrs.size() > 0) {
                saveTestFile(tapCode, BISFILENAMETEMPL, bis_cdrs);
            }
            if (rsoot_cdrs.size() > 0) {
                saveTestFile(tapCode, RSOOTFILENAMETEMPL, rsoot_cdrs);
            }
        }
    }

    private void saveTestFile(String tapCode, String filenametempl, List<String> cdrsList) {
        log.info("saveTestFile: filenametemplate=" + filenametempl + ",tapcode=" + tapCode);
        Path pathDir = Paths.get(PATHCDRTESTDIR);
        try {
            if (!Files.exists(pathDir)) {
                log.info("saveTestFile:create directory");
                Files.createDirectories(pathDir);
            }
            String ldt = LocalDateTime.now().format(DTF);
            String fileName = filenametempl
                    .replaceFirst("<YYYYMMDDHH24MISS>", ldt)
                    .replaceFirst("<SEQ>", "00001")
                    .replaceFirst("<TAPCODE>", tapCode)
                    .replaceFirst("<COUNT>",
                            String.format(String.valueOf(cdrsList.size()), "%04d")
                    );
            log.info("saveTestFile: filename=" + fileName);
            Path pathFile = Paths.get(PATHCDRTESTDIR + "/" + fileName);
            log.info("saveTestFile:write cdrlist");
            Files.write(pathFile, cdrsList);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage()) ;
        }

    }

    private String incStartTime(String start_time) {
        return LocalDateTime.parse(start_time, DTF).plusSeconds(1).format(DTF);
    }

    private String create_cdrtst_moc(String p_tap_code, String imsi, String p_start_time, String prefix, String lac) {
        return "01,E871B8C3F3,,," + imsi + ",," + prefix + "," + p_start_time + ",50,0,05,,,869194042228620,,00,11,,," + lac
                + ",45272,,,,,," + prefix + ",,05,,RNC17_NER,RNC17_NER,,,+0900,,,79246600663,,,,,,,,,,," + p_tap_code + ",,,,,,,,,,,,,";
    }


    private String create_cdrtst_mtc(String p_tap_code, String p_imsi, String p_start_time, String lac) {
        return "02,E871B8C3F3,,," + p_imsi + ",,," + p_start_time + ",50,0,05,,,869194042228620,,00,11,,," + lac
                + ",45272,,,,,,,,05,,RNC17_NER,RNC17_NER,,,+0900,,,79246600663,,,,,,,,,,," + p_tap_code + ",,,,,,,,,,,,,";
    }

    private String create_cdrtst_gprs(String p_tap_code, String p_imsi, String p_start_time, String lac) {
        return "31,905522477,,977948834:80.214.242.192," + p_imsi + ",,," + p_start_time + ",2,0,,,,355278050137950,,,,SSU77-ECOTAXE.COM,," + lac
                + ",63202,,,,,,,,,,,,,,,,,83.149.57.7,,,,<GPRS><Node>1</Node><PDP>10.124.63.214</PDP><sgsn>83.149.57.7</sgsn><ggsn>80.214.242.192</ggsn><chrgID>977948834</chrgID><duration>344</duration>" +
                "<APN>SSU77-ECOTAXE.COM|MNC020.MCC208.GPRS</APN><VolIn>767</VolIn><VolOut>1065</VolOut><sgsnChange>0</sgsnChange><QoSReq><delay>0</delay><mean>0</mean><peak>0</peak><prcdnc>0</prcdnc>" +
                "<rlblt>0</rlblt></QoSReq><QoSUse><delay>0</delay><mean>0</mean><peak>0</peak><prcdnc>0</prcdnc><rlblt>0</rlblt></QoSUse></GPRS>,,,,,,," + p_tap_code + ",,,,,,,,,,,,,";
    }

    private String create_cdrtst_smsmo(String p_tap_code, String p_imsi, String p_start_time, String lac) {
        return "08,E871B8C3F3,,," + p_imsi + ",,78121234567," + p_start_time + ",0,0,05,,79043490000,869194042228620,,00,22,,," + lac
                + ",45272,,,,,,78121234567,,05,,RNC17_NER,RNC17_NER,,,+0900,,,79246600663,,,,,,,,,,,"
                + p_tap_code + ",,,,,,,,,,,,,";
    }
}

