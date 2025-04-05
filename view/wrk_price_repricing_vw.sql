create or replace view wrk_price_repricing_vw as
select tt.cou_name,
       tt.call_type,
       mp.srls_srls_id,
       mp.lcal_lcal_id,
       tt.cou_id,
       tt.price,
       mp.rndt_default,
       mp.name as service_name,
       tt.task
  from (select cou_name, call_type, cou_id, price, task
          from (select t.*, co.cou_id
                  from WRK_RTPL_PRICE_REPRICING t
                  join (select *
                         from countries co
                        where co.cou_code is not null) co
                    on (lower(trim(co.name_r)) = lower(trim(t.cou_name)))
                 where del_date is null
                   and task is not null
                union
                select t.*, co1.cou_id
                  from WRK_RTPL_PRICE_REPRICING t
                  join wrk_rtpl_cou_mapping co1
                    on lower(trim(co1.name)) = lower(trim(t.cou_name))
                 where del_date is null
                   and task is not null) unpivot(price for call_type in(moc_l,
                                                                        moc_r,
                                                                        moc_o,
                                                                        moc_s,
                                                                        mtc,
                                                                        smsmo,
                                                                        gprs))) tt
  left outer join wrk_rtpl_lcal_srls_repricing_map mp
    on mp.call_type = tt.call_type
 where trim(lower(tt.cou_name)) <> 'rndt_id';
