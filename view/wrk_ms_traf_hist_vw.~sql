create or replace view wrk_ms_traf_hist_vw as 
select --sl.def as srvc_name,
       lc.def as lcal_name,
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
   and th.start_date >= tah.start_date and th.start_date < tah.end_date
  join logic_calls lc
    on lc.lcal_id = th.lcal_lcal_id
  join serv_lists sl
    on sl.srls_id = tah.srls_srls_id
  left outer join free_times ft
    on ft.rtpl_rtpl_id = tah.rtpl_rtpl_id
   and ft.lcal_lcal_id = th.lcal_lcal_id
   and nvl(ft.cou_cou_id, 0) = nvl(th.cou_cou_id, 0)
   and ft.start_date >= tah.end_date and ft.start_date < tah.end_date
  left outer join round_types rt
    on rt.rndt_id = tah.rndt_rndt_id
  left outer join round_types rt1
    on rt1.rndt_id = ft.rndt_rndt_id
 order by /*srvc_name,*/ lcal_name, tah.start_date, th.start_date

