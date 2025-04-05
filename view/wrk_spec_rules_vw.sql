create or replace view wrk_spec_rules_vw as
select sprl_id,
       ruct.def,
       ruc.condition,
       ruc.logic,
       rus.def       as spc_rules_sets_def,
       spras.def     as spec_rules_actions_sets_def,
       spra.action,
       ruh.start_date,ruh.end_date,
       ruh.sprlh_id
  from spec_rules_sets_hist rush
  join spec_rules_sets rus
    on rus.sprs_id = rush.sprs_sprs_id
  join spec_rules_hist ruh
    on ruh.sprl_sprl_id = rush.sprl_sprl_id
  join spec_rules_conditions ruc
    on ruc.sprcnd_id = ruh.sprcnd_sprcnd_id
  join spec_rules ru
    on ru.sprl_id = ruh.sprl_sprl_id
  join spec_rules_conditions_types ruct
    on ruct.sprct_id = ruc.sprct_sprct_id
  join spec_rules_actions_map spram
    on spram.sprs_sprs_id = rus.sprs_id
  join spec_rules_actions_sets spras
    on spras.spras_id = spram.spras_spras_id
  join spec_rules_actions_sets_hist sprash
    on sprash.spras_spras_id = spras.spras_id
  join spec_rules_actions spra
    on spra.spra_id = sprash.spra_spra_id
 where rush.del_date is null
   and ruh.del_date is null
   and ruc.del_date is null
   and ru.del_date is null
   and ruh.end_date>sysdate
  -- and rush.sprs_sprs_id in
 order by sprl_id, sprct_sprct_id, condition
;
