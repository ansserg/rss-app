create or replace view bis.wrk_ms_traf_hist_vw as
select
 lc.def as tarif_name,
 th.price_$ as price,
 case
   when rt1.rndt_id is null then
    rt.def
   else
    rt1.def
 end as rndt_name,
 /*       tah.start_date as tah_start_date,
 tah.end_date as tah_end_date,*/
 th.start_date,
 th.end_date,
 tah.rtpl_rtpl_id,
 --tah.srls_srls_id,
 lc.lcal_id
  from (select * from tarif_histories tah) tah
  join (select * from trafic_histories th) th
    on th.rtpl_rtpl_id = tah.rtpl_rtpl_id
   and th.srls_srls_id = tah.srls_srls_id
   and th.start_date >= tah.start_date
   and th.start_date < tah.end_date
  join logic_calls lc
    on lc.lcal_id = th.lcal_lcal_id
  join serv_lists sl
    on sl.srls_id = tah.srls_srls_id
  left outer join free_times ft
    on ft.rtpl_rtpl_id = tah.rtpl_rtpl_id
   and ft.lcal_lcal_id = th.lcal_lcal_id
   and nvl(ft.cou_cou_id, 0) = nvl(th.cou_cou_id, 0)
   and ft.start_date >= tah.end_date
   and ft.start_date < tah.end_date
  left outer join round_types rt
    on rt.rndt_id = tah.rndt_rndt_id
  left outer join round_types rt1
    on rt1.rndt_id = ft.rndt_rndt_id
union
select distinct rpd.name_r as tarif_name,
                tbd.price_$,
                rt.def,
                tbd.start_date,
                tbd.end_date,
                rpd.rtpl_rtpl_id,
                rpd.rpdr_id
  from rate_plan_directions rpd
  join trafics_by_directions tbd
    on tbd.rpdr_rpdr_id = rpd.rpdr_id
  join pset_directions psd
    on psd.rpdr_rpdr_id = rpd.rpdr_id
  join round_types rt
    on rt.rndt_id = rpd.rndt_rndt_id
 where psd.start_date < tbd.end_date
   and psd.end_date > tbd.start_date
 order by tarif_name, start_date
;
