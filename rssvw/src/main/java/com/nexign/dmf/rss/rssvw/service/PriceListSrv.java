package com.nexign.dmf.rss.rssvw.service;

import com.nexign.dmf.rss.rssvw.config.DBConnectConfig;
import com.nexign.dmf.rss.rssvw.model.PriceListData;
//import jakarta.ws.rs.NotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Data
@NoArgsConstructor
public class PriceListSrv {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private static final SimpleDateFormat SDFRU = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

    @Value("${db.sql.pricelist}")
    private String sql_price_list;
    @Value("${db.sql.rtplid}")
    private String sql_rtpl_id;
    @Value("${db.sql.rtplname}")
    private String sql_rtpl_name;
    @Value("${db.sql.tapcode}")
    private String sql_tap_code;

    @Autowired
    DBConnectConfig cfg;

    private Date StrToDate(String start_date) {
        Date dt = null;
        try {
            dt = SDF.parse(start_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public List<PriceListData> getPriceList(NamedParameterJdbcTemplate jdbc, Long rtplId, Date date) {
        List<PriceListData> plist = new ArrayList<>();
        SqlParameterSource sqlpar = new MapSqlParameterSource().addValue("id", rtplId).addValue("date", date);
        jdbc.query(sql_price_list, sqlpar, rs -> {
            PriceListData pl = new PriceListData();
            pl.setLcalId(rs.getLong("lcal_id"));
            pl.setRtplId(rs.getLong("rtpl_rtpl_id"));
            pl.setTarifName(rs.getString("tarif_name"));
            pl.setRndtName(rs.getString("rndt_name"));
            pl.setPrice(rs.getBigDecimal("price"));
            pl.setEndDate(SDFRU.format(rs.getDate("end_date")));
            pl.setStartDate(SDFRU.format(rs.getDate("start_date")));
            plist.add(pl);
        });
        return plist;
    }

    public String getRtplNameFromId(NamedParameterJdbcTemplate jdbc, Long id, String start_date) {
        SqlParameterSource sqlpar = new MapSqlParameterSource().addValue("id", id).addValue("date", StrToDate(start_date));
        return jdbc.queryForObject(sql_rtpl_name, sqlpar, String.class);
    }

    public Long getRtplIdFromName(NamedParameterJdbcTemplate jdbc, String name, Date date) {
        long rtpl_id=0l;
        SqlParameterSource sqlpar = new MapSqlParameterSource().addValue("name", name).addValue("date", date);
        try {
            rtpl_id = jdbc.queryForObject(sql_rtpl_id, sqlpar, Long.class);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Тарифный план не найден");
        }
        return rtpl_id;
    }

    public Long getRtplIdFromTapCode(NamedParameterJdbcTemplate jdbc, String tap_code, Date date) {
        SqlParameterSource sqlpar = new MapSqlParameterSource().addValue("tap_code", tap_code).addValue("date", date);
        return jdbc.queryForObject(sql_tap_code, sqlpar, Long.class);
    }

    public List<PriceListData> getTrafficPrice(String val_tp, String value, String start_date, NamedParameterJdbcTemplate jdbc) {
        Long rtpl_id = -1l;
        List<PriceListData> price_data= new ArrayList<>();
        rtpl_id = switch (val_tp) {
            case "id" -> Long.parseLong(value);
            case "tap" -> getRtplIdFromTapCode(jdbc, value, StrToDate(start_date));
            case "name" -> getRtplIdFromName(jdbc, value, StrToDate(start_date));
            default -> -1l;
        };
        if (rtpl_id>0)
            price_data = getPriceList(jdbc, rtpl_id, StrToDate(start_date));
        return price_data;
    }
}
