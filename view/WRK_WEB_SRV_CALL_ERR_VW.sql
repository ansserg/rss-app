CREATE OR REPLACE VIEW WRK_WEB_SRV_CALL_ERR_VW AS
with wrk_web_srv_tmp_err as
 (select /*+parallel(b,4) */
   c.error_code,
   c.error_msg,
   b."CALL_ID",
   b."IMSI",
   b."START_TIME",
   b."CLTP_CLTP_ID",
   b."DURATION",
   b."DESTINATION",
   b."MSC",
   b."SERVICE_CODE",
   b."ACTION_CODE",
   b."UNIQUE_CHECK",
   b."IN_CHARGE",
   b."IN_TAX",
   b."IN_DISCOUNT",
   b."OUT_CHARGE",
   b."OUT_TAX",
   b."OUT_DISCOUNT",
   b."TAX_RATE",
   b."TEST_CALL",
   b."CALL_EVENT_MEMO",
   b."FILE_RECNUM",
   b."VPMN_FILE_ID",
   b."HPMN_FILE_ID",
   b."HRAP_FILE_ID",
   b."VRAP_FILE_ID",
   b."RMOP_RMOP_ID",
   b."FRRP_FRRP_ID",
   b."SERV_CHARGE",
   b."MARKUP_AMOUNT",
   b."LAC",
   b."MSISDN",
   b."DATA_VOLUME",
   b."UTC_OFFSET",
   b."UDF",
   b."REPRICING",
   b."BRT_FILE_ID",
   b."SERVING_NETWORK",
   b."NAVI_USER",
   b."NAVI_DATE",
   b."DEL_USER",
   b."DEL_DATE",
   b."VPMN_CUR_ID",
   b."IN_PRICE",
   b."IN_PRICE_TAX",
   b."SERV_ATTR",
   b."HPMN_RMOP_ID",
   b."DIALLED_DIGITS",
   b."DEST_TYPE",
   b."FILE_RECNUM_OUT",
   b."RRF_FILE_ID",
   b."RMOP_SNDR_ID",
   b."VPMN_RMOP_ID",
   b."CELL_ID",
   b."IMEI",
   b."CHARGE_ALT",
   b."TAX_ALT",
   b."CAMEL_FLAG",
   b."SGSN",
   b."SGW",
   b."IBS_TARIFF_CLASS",
   b."REPRICE_CHARGE",
   b."REPRICE_TAX"
    from files a,
         (select trunc(sysdate, 'mm') as v_period from dual),
         calls b,
         call_errors c
   where a.navi_date > v_period - 2
     and a.dctp_dctp_id = 6
     and a.flcl_flcl_id = 3
     and a.hpmn_rmop_id = 1
     and a.vpmn_rmop_id in (1117, 2)
     and b.vrap_file_id = a.file_id
     and cltp_cltp_id <> 3
     and c.call_call_id = b.call_id
     and c.start_time = b.start_time
     and c.error_msg not like
         'Mobile Terminated SMS present although not agreed bilaterally%'
     and not (service_code like 'T2%' and cltp_cltp_id = 2 and
          c.error_msg = 'Rejected in accordance with Spec Rules')
     and not
          (service_code = 'T12' and
          c.error_msg =
          'Mobile Originated Call present although not agreed bilaterally')
        --and not (service_code='PS' and c.error_msg='GPRS Call present although not agreed bilaterally' )
     and not (cltp_cltp_id = 6 and b.hpmn_rmop_id = 1440 and
          c.error_msg = 'Rejected in accordance with Spec Rules')
        --and b.imsi not like '25094%' -- добавлено с запуском тестового оператора без TADIG и ТП
     and not (b.cltp_cltp_id = 6 and b.hpmn_rmop_id = 1216 and b.lac = 1531) --GPRS RSOOT_RUST2
  )

SELECT 0 as err_type,
       r.call_id,
       r.start_time,
       r.error_msg,
       r.imsi,
       r.service_code,
       r.error_code,
       r.cltp_cltp_id,
       r.destination,
       r.duration,
       r.data_volume
  FROM (select (select count(1)
                  from calls t
                 where t.imsi = a.imsi
                   and t.start_time = a.start_time
                   and t.duration = a.duration
                   and t.destination = a.destination
                   and t.lac = a.lac
                   and a.cell_id = t.cell_id
                   and t.cltp_cltp_id = a.cltp_cltp_id
                   and t.service_code = a.service_code
                   and t.call_id <> a.call_id
                   and t.msc = a.msc -- and substr(a.call_event_memo,1,length(a.call_event_memo)*4/5)=substr(t.call_event_memo,1,length(t.call_event_memo)*4/5)
                   and nvl(a.data_volume, 0) = nvl(t.data_volume, 0)) as flag_calls,
               a.*
          from wrk_web_srv_tmp_err a
         where error_msg = 'Duplicate call'
           and exists (select 1
                  from call_errors t
                 where t.call_call_id = a.call_id
                   and t.start_time = a.start_time
                   and t.del_date is null)
           and a.call_event_memo not like '%sgw01%') r
 WHERE flag_calls > 0

union

SELECT 1 as err_type,
       r.call_id,
       r.start_time,
       r.error_msg,
       r.imsi,
       r.service_code,
       r.error_code,
       r.cltp_cltp_id,
       r.destination,
       r.duration,
       r.data_volume
  FROM (select (select count(1)
                  from calls t
                 where t.imsi = a.imsi
                   and t.start_time = a.start_time
                   and t.duration = a.duration
                   and t.destination = a.destination
                   and t.lac = a.lac
                   and a.cell_id = t.cell_id
                   and t.cltp_cltp_id = a.cltp_cltp_id
                   and t.service_code = a.service_code
                   and t.call_id <> a.call_id
                   and t.msc = a.msc -- and substr(a.call_event_memo,1,length(a.call_event_memo)*4/5)=substr(t.call_event_memo,1,length(t.call_event_memo)*4/5)
                ) as flag_calls,
               a.*
          from wrk_web_srv_tmp_err a
         where error_msg = 'Duplicate call'
           and exists (select 1
                  from call_errors t
                 where t.call_call_id = a.call_id
                   and t.start_time = a.start_time
                   and t.del_date is null)
           and a.call_event_memo not like '%sgw01%') r
 WHERE flag_calls = 0

union

select 2 as err_type,
       a.call_id,
       a.start_time,
       a.error_msg,
       a.imsi,
       a.service_code,
       a.error_code,
       a.cltp_cltp_id,
       a.destination,
       a.duration,
       a.data_volume
  from wrk_web_srv_tmp_err a
 where error_msg like 'Value out of range'
   and exists (select 1
          from call_errors t
         where t.call_call_id = a.call_id
           and t.start_time = a.start_time
           and t.del_date is null)
union

select 3 as err_type,
       a.call_id,
       a.start_time,
       a.error_msg,
       a.imsi,
       a.service_code,
       a.error_code,
       a.cltp_cltp_id,
       a.destination,
       a.duration,
       a.data_volume
  from wrk_web_srv_tmp_err a
 where error_msg like
       'A combination of Service Code and Action which must not be transferred%'
   and exists (select 1
          from call_errors t
         where t.call_call_id = a.call_id
           and t.start_time = a.start_time
           and t.del_date is null)
union

SELECT 4 as err_type,
       r.call_id,
       r.start_time,
       r.error_msg,
       r.imsi,
       r.service_code,
       r.error_code,
       r.cltp_cltp_id,
       r.destination,
       r.duration,
       r.data_volume
  FROM (select (select count(1)
                  from calls t
                 where t.imsi = a.imsi
                   and t.start_time = a.start_time
                   and t.duration = a.duration
                   and t.destination = a.destination
                   and t.lac = a.lac
                   and a.cell_id = t.cell_id
                   and t.cltp_cltp_id = a.cltp_cltp_id
                   and t.service_code = a.service_code
                   and t.call_id <> a.call_id
                   and t.msc = a.msc -- and substr(a.call_event_memo,1,length(a.call_event_memo)*4/5)=substr(t.call_event_memo,1,length(t.call_event_memo)*4/5)
                ) as flag_calls,
               a.*
          from wrk_web_srv_tmp_err a
         where error_msg = 'Duplicate call'
           and exists (select 1
                  from call_errors t
                 where t.call_call_id = a.call_id
                   and t.start_time = a.start_time
                   and t.del_date is null)
           and a.call_event_memo like '%sgw01%') r
 order by imsi, start_time
-- select * from wrk_web_srv_call_err_vw
;
