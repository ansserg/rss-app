package com.nexign.dmf.rss.ocsdb.entityes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Entity
@NamedStoredProcedureQuery(name = "wrkRtplParsData", procedureName = "wrk_set_rate_plan.parse_data", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class)
})
@NamedStoredProcedureQuery(name = "wrkRtplCheckMapping", procedureName = "wrk_set_rate_plan.check_mapping", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class)
})
@NamedStoredProcedureQuery(name = "wrkSetRatePlans", procedureName = "wrk_set_rate_plan.set_rate_plans", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_RFC", type = Integer.class)
})
@NamedStoredProcedureQuery(name = "addLacRegions", procedureName = "wrk_set_rate_plan.add_lac_regions", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_lac", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_region", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_task", type = Integer.class)

})
@Table(name = "wrk_rtpl_definition")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WrkRtplDef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrd_id")
    Long wrdId;
    @Column(name = "attr_id")
    Long attrId;
    @Column(name = "attr_def")
    String attrdef;
    @Column(name = "attr_value")
    String attrvalue;
    @Column(name = "attr_value2")
    String attrvalue2;
    Long task;
    @Column(name = "navi_date")
    Date navidate;

    public WrkRtplDef(Long attrId, String attrDef, String attrValue, String attrValue2, Long task, Date naviDate, Date delDate) {
        this.attrId = attrId;
        this.attrdef = attrDef;
        this.attrvalue = attrValue;
        this.attrvalue2 = attrValue2;
        this.task = task;
        this.navidate = naviDate;
        this.deldate = delDate;
    }

    @Column(name = "del_date")
    Date deldate;
}
