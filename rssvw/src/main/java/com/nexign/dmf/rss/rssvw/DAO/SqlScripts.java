package com.nexign.dmf.rss.rssvw.DAO;

import com.nexign.dmf.rss.rssvw.model.RapStat;
import com.nexign.dmf.rss.rssvw.model.RepDetails;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component

public class SqlScripts {

    String NODATA ="По данному  запросу  ничего нет";
    int rowLimit =200000;
    String message;

   //-------------------------------------
    public List<RepDetails> getDetailsData (HttpServletRequest request, HttpServletResponse response, NamedParameterJdbcTemplate jdbc)    {

      List outParams = new ArrayList<>();
      List inParams = new ArrayList();
      List inParamsValue = new ArrayList();
      List sheetTitle = new ArrayList<>();
      List<RepDetails> sqlresult = new ArrayList<>();
      //List<RepDetails> err =new ArrayList<>();
       RepDetails err = new RepDetails();
  System.out.println("----------------DETAILS --------------------");
      message="";
      //-----------------------------Getting In parameters-------------------------------
      Enumeration enumeration = request.getParameterNames();
      while (enumeration.hasMoreElements()) {

          String parameterName = (String) enumeration.nextElement();

          if (parameterName.startsWith("out_")) {
             // System.out.println(parameterName+"="+request.getParameter(parameterName));
              outParams.add(parameterName);
          } else if (parameterName.startsWith("in_")) {
                System.out.println(parameterName+"="+request.getParameter(parameterName));
              if (StringUtil.isBlank(request.getParameter(parameterName)) ||
                      request.getParameter(parameterName).isEmpty() || request.getParameter(parameterName) == null) {
                  continue;
              } else {
                  inParams.add(parameterName);
                  inParamsValue.add(request.getParameter(parameterName));
              }
          }
      }



      String CTL1=" null as  CTL1,";

      if ( outParams.contains("out_ctl1Form2") ) {
          CTL1="(select  attr_value from rss.rssmb_call_details cd where cd.call_call_id=c.call_id and cd.start_time=c.start_time and attr_name='CTL1') as ctl1,";
      }
      String queryHeader="file_name, decode(direction,'R', in_charge,out_charge) as charge, " +
              "decode(direction,'R', in_tax,out_tax) as tax, " +
              "to_char(start_time,'dd.mm.yyyy hh24:mi:ss') as start_time_ch, " + CTL1+
              "decode(cltp_cltp_id,6,'GPRS',1,decode(substr(service_code,1,2),'T1','MOC','SMS'),2,decode(substr(service_code,1,2),'T1','MTC','SMST')) as  call_type, " +
              "c.*";

      String queryFrom="";
      String queryWhere="";

      //  List of  tables
      queryFrom=" rss.rssmb_files f, rss.rssmb_calls c, rss.rssmb_roam_operators vpmn, rss.rssmb_roam_operators hpmn";

      //  Forming WHERE clause
      String filesource="hpmn_file_id and \n";

      String VPMN="";
      if ( inParams.contains("in_vpmnForm2") ) {
          VPMN="vpmn.short_name = upper('"+inParamsValue.get(inParams.indexOf("in_vpmnForm2"))+"') and \n";
      }
      //------------------------
      String HPMN="";
      if ( inParams.contains("in_hpmnForm2") )     {
          String hpmn = String.valueOf(inParamsValue.get(inParams.indexOf("in_hpmnForm2"))).toUpperCase();
          HPMN="hpmn.short_name =upper('"+inParamsValue.get(inParams.indexOf("in_hpmnForm2"))+"') and \n";
          if  ( hpmn.equals("RUSNW") )
          { filesource="vpmn_file_id and \n"; }
      }

      //------------------------

      String IMSI="";
      if ( inParams.contains("in_imsiForm2") )     {   IMSI="imsi like '"+inParamsValue.get(inParams.indexOf("in_imsiForm2"))+"%' and \n";   }
      //------------------------
      String MSC="";
      if ( inParams.contains("in_mscForm2") )     {   MSC="msc like '"+inParamsValue.get(inParams.indexOf("in_mscForm2"))+"%' and \n";   }

      //------------------------
      String startFrom="";
      String naviDate="";

      if  (!inParams.contains("in_seqfromForm2")) {
          startFrom = "c.start_time>=sysdate-30 and \n";
          naviDate = "f.navi_Date>sysdate-31 and \n";
      }
      //------------------------
      if ( inParams.contains("in_starttimefromForm2") )
      {
          startFrom=" c.start_time>=to_date('"+inParamsValue.get(inParams.indexOf("in_starttimefromForm2"))+"','yyyy-mm-dd') and \n";
          naviDate=" f.navi_Date>=to_date('"+inParamsValue.get(inParams.indexOf("in_starttimefromForm2"))+"','yyyy-mm-dd')-1 and \n";
      }
      //------------------------
      String startTo=""; //""c.start_time<sysdate and";

      if ( inParams.contains("in_starttimetoForm2") )
      {  startTo=" c.start_time < to_date('"+inParamsValue.get(inParams.indexOf("in_starttimetoForm2"))+"','yyyy-mm-dd') and \n";   }
      //------------------------SRLS

      String SRLS="" ;

      if (   !inParams.contains("in_srlsGPRSForm2")  || !inParams.contains("in_srlsMOCForm2") || !inParams.contains("in_srlsMTCForm2") ||
              !inParams.contains("in_srlsSMSForm2") ||  !inParams.contains("in_srlsT12Form2")
      )
      {
          if (inParams.contains("in_srlsGPRSForm2")   )  { SRLS = SRLS+"cltp_cltp_id=6 or \n" ; }
          if (inParams.contains("in_srlsMOCForm2")   )  { SRLS = SRLS+"(cltp_cltp_id=1 and service_code  not like 'T2%' and NOT (service_code='T12' or destination='112' or dialled_digits='112')) or \n"; }
          if (inParams.contains("in_srlsMTCForm2")    )  { SRLS  =SRLS+"(cltp_cltp_id=2 and service_code  not like 'T2%') or \n"; }
          if (inParams.contains("in_srlsSMSForm2")   )  { SRLS  =SRLS+"(cltp_cltp_id=1 and service_code   like 'T2%') or \n"; }
          if (inParams.contains("in_srlsT12Form2")    )  { SRLS  =SRLS+"(cltp_cltp_id=1 and service_code='T12') or destination ='112' or dialled_digits='112' or \n"; }

          SRLS ="("+SRLS +" 1=2 ) and \n";
      }
      //--------------------- SEQ_NUM

      String seqFrom="";

      if ( inParams.contains("in_seqfromForm2") )
      {
          seqFrom=" seq_num>="+inParamsValue.get(inParams.indexOf("in_seqfromForm2"))+" and";
      }
      String seqTo="";
      if ( inParams.contains("in_seqtoForm2") )
      {
          seqTo=" seq_num<="+inParamsValue.get(inParams.indexOf("in_seqtoForm2"))+" and ";
      }
      ///-------------------- UDF

      String udf="";

       if( inParams.contains("in_branchForm2") &&
               !( inParamsValue.get(inParams.indexOf("in_branchForm2")).equals("all") || inParamsValue.get(inParams.indexOf("in_branchForm2")).equals("xxx")  ) )
       {
             udf=" udf=upper('"+inParamsValue.get(inParams.indexOf("in_branchForm2"))+"') and";

       }

       queryWhere=" f.vpmn_rmop_id=vpmn.rmop_id and \n" +
              " f.hpmn_rmop_id=hpmn.rmop_id and \n" +
              " f.file_id="+filesource+ //decode(f.direction,'R',c.vpmn_file_id,c.hpmn_file_id) and " +
              " c.del_date  is null and \n" +
              // " 1=? and \n" +
            //  " rownum<=3 and  " +
              " f.type_indicator='C' and  service_code<>'T21' and \n" +
              VPMN+" "+HPMN+" "+IMSI+" "+SRLS+" "+startFrom+" "+startTo+" "+naviDate+" "+ MSC+ seqFrom+seqTo+" "+udf;

      String SQL=" SELECT /*+parallel(c,5)*/ "+queryHeader+" FROM "+queryFrom+" WHERE "+queryWhere+" 1=1";
      String preSQL =" SELECT /*+parallel(c,5)*/ count(1) as res FROM "+queryFrom+" WHERE "+queryWhere+" 1=:1";

         System.out.println("----"+SQL);
         System.out.println(preSQL);
      try {
          SqlParameterSource par = new MapSqlParameterSource().addValue("1", 1);
          int sqlres = jdbc.queryForObject(preSQL, par, Integer.class);
          System.out.println("-----------------COUNT-------"+sqlres);
          if (sqlres >= rowLimit) {
              message = "Запрос  возвращает " + sqlres + " строк/строки. " + rowLimit +
                      " -  предел для  одного запроса. Задайте, пожалуйста, дополнительный  критерий отбора данных.";
              // System.out.println("here0");
              //  err.setErr(message);
              err.setErr(message);
              sqlresult.add(err);
              throw new RuntimeException(message);
          } else {
              System.out.println("INSIDE--------------------------");
              try {
                  sqlresult = jdbc.query(SQL,
                          (resultSet, rowNum) -> {
                              RepDetails getresult = new RepDetails();
                              if (outParams.contains("out_imsi")) {
                                  getresult.setImsi(resultSet.getString("imsi"));
                              }
                              if (outParams.contains("out_starttime")) {
                                  getresult.setStarttime(resultSet.getString("start_time_ch"));
                              }
                              if (outParams.contains("out_filename")) {
                                  getresult.setFilename(resultSet.getString("file_name"));
                              }
                              if (outParams.contains("out_charge")) {
                                  getresult.setCharge(resultSet.getFloat("charge"));
                              }
                              if (outParams.contains("out_duration")) {
                                  getresult.setDuration(resultSet.getInt("duration"));
                              }
                              if (outParams.contains("out_datavolume")) {
                                  getresult.setDatavolume(resultSet.getInt("data_volume"));
                              }
                              if (outParams.contains("out_destination")) {
                                  getresult.setDestination(resultSet.getString("destination"));
                              }
                              if (outParams.contains("out_msc")) {
                                  getresult.setMsc(resultSet.getString("msc"));
                              }
                              if (outParams.contains("out_udf")) {
                                  getresult.setUdf(resultSet.getString("udf"));
                              }
                              if (outParams.contains("out_lac")) {
                                  getresult.setLac(resultSet.getString("lac"));
                              }
                              if (outParams.contains("out_ctl1")) {
                                  getresult.setCtl1(resultSet.getString("ctl1"));
                              }
                              if (outParams.contains("out_calltype")) {
                                  getresult.setCalltype(resultSet.getString("call_type"));
                              }
                              if (outParams.contains("out_cltp")) {
                                  getresult.setCltp(resultSet.getString("cltp_cltp_id"));
                              }
                              if (outParams.contains("out_servicecode")) {
                                  getresult.setServicecode(resultSet.getString("service_code"));
                              }
                              if (outParams.contains("out_tax")) {
                                  getresult.setTax(resultSet.getFloat("tax"));
                              }
                              if (outParams.contains("out_dialled")) {
                                  getresult.setDialleddigits(resultSet.getString("dialled_digits"));
                              }


                              return getresult;
                          });
              } catch (Exception e) {
                  System.out.println("SQL-error getDetailData=" + e.getMessage() + "script=" + SQL);
                  err.setErr("ERROR=" + e.getMessage());
                  sqlresult.add(err);
                  return (sqlresult);
              }

          }
      } catch (Exception e)
      {
          System.out.println("SQL-error getDetailData=" + e.getMessage() + "script=" + SQL);
          err.setErr("ERROR=" + e.getMessage());
          sqlresult.add(err);
          return (sqlresult);
      }
       return(sqlresult);
  }


//----------------------------------------------RAP Statistics
 public  List<RapStat>  getRapData (HttpServletRequest request, HttpServletResponse response, NamedParameterJdbcTemplate jdbc)    {

    List outParams = new ArrayList<>();
    List inParams = new ArrayList();
    List inParamsValue = new ArrayList();
     List<RapStat> sqlresult = new ArrayList<>();
     RapStat err = new RapStat();
    message="";
    Enumeration enumeration = request.getParameterNames();
    while (enumeration.hasMoreElements()) {
        String parameterName = (String) enumeration.nextElement();
        if (parameterName.startsWith("out_"))
        {
            outParams.add(parameterName);
        }
        else if ( parameterName.startsWith("in_") )
        {

            if (StringUtil.isBlank(request.getParameter(parameterName)) || request.getParameter(parameterName).isEmpty() || request.getParameter(parameterName)==null)
            {

            }
            else {
                inParams.add(parameterName);
                inParamsValue.add(request.getParameter(parameterName));

            }
        }
    }
    //------------------------------------------
    String VPMN="";
    if ( inParams.contains("in_vpmnForm1") ) {
        VPMN="vpmn.short_name = upper('"+inParamsValue.get(inParams.indexOf("in_vpmnForm1"))+"') and \n";
    }

    String HPMN="";
    if ( inParams.contains("in_hpmnForm1") ) {
        HPMN="hpmn.short_name = upper('"+inParamsValue.get(inParams.indexOf("in_hpmnForm1"))+"') and \n";
    }

    String NAVIDATEFROM="";

    if  (!inParams.contains("in_seqfromForm1")) {
        NAVIDATEFROM = "f.navi_Date>=sysdate-30 and \n";
    }
    //------------------------
    if ( inParams.contains("in_navidatefromForm1") )
    {
        NAVIDATEFROM=" f.navi_Date>=to_date('"+inParamsValue.get(inParams.indexOf("in_navidatefromForm1"))+"','yyyy-mm-dd') and \n";
    }
    //------------------------
    String NAVIDATETO="";

    if ( inParams.contains("in_navidatetoForm1") )
    {  NAVIDATETO=" f.navi_Date < to_date('"+inParamsValue.get(inParams.indexOf("in_navidatetoForm1"))+"','yyyy-mm-dd') and \n";   }
    //--------------------------
    String SEQFROM ="";
    if  (inParams.contains("in_seqfromForm1")) {
        SEQFROM = "f.seq_num>="+inParamsValue.get(inParams.indexOf("in_seqfromForm1"))+" and \n";
    }
    String SEQTO ="";
    if  (inParams.contains("in_seqtoForm1")) {
        SEQTO = "f.seq_num<="+inParamsValue.get(inParams.indexOf("in_seqtoForm1"))+" and \n";
    }
    //------------------------------------------------
    String query_header="  f.file_name as tap_file, rap.file_name as rap_file, -sum(ad.agr_in_charge) as file_charge, -sum(ad.agr_in_tax) as file_tax";
    String query_from=" rss.rssmb_files rap, rss.rssmb_aggr_data ad, rss.rssmb_files f, rss.rssmb_roam_operators vpmn, rss.rssmb_roam_operators hpmn";
    String query_where=" rap.flcl_flcl_id=3 and \n" +
            VPMN  + HPMN  +NAVIDATEFROM +NAVIDATETO +SEQFROM +SEQTO+
            "rap.vpmn_rmop_id=vpmn.rmop_id and \n" +
            "rap.hpmn_rmop_id=hpmn.rmop_id and \n"+
            "rap.file_id=ad.file_file_id and \n" +
            "ad.del_date is null and \n"+
            "ad.tap_file_id=f.file_id \n" +
            "group by f.file_name, rap.file_name \n"+
            "order by f.file_name \n";

    String SQL=" SELECT /*+parallel(5)*/ "+query_header+" FROM "+query_from+" WHERE "+query_where;

      System.out.println(SQL);

try {
    System.out.println("before  SQL request");
    sqlresult = jdbc.query(SQL,
            (resultSet, rowNum) -> {
                RapStat getresult = new RapStat();
                getresult.setTapname(resultSet.getString("tap_file"));
                getresult.setRapname(resultSet.getString("rap_file"));
                getresult.setCharge(resultSet.getFloat("file_charge"));
                getresult.setTax(resultSet.getFloat("file_tax"));
                return getresult;
            });
    System.out.println("----------------after SQL request-------");
} catch (Exception e )
{
    System.out.println("----------------some ERROR-------");
    err.setErr("ERROR="+e.getMessage());
    sqlresult.add(err);
    return(sqlresult);
}

/*
    try {
        Statement statement = connection.createStatement();
        int sqlres = 0;

        ResultSet resultSet = statement.executeQuery(SQL);

        while (resultSet.next()) {

            RapStat getresult = new RapStat();
            getresult.setTapname(resultSet.getString("tap_file"));
            getresult.setRapname(resultSet.getString("rap_file"));
            getresult.setCharge(resultSet.getFloat("file_charge"));
            getresult.setTax(resultSet.getFloat("file_tax"));
            sqlresult.add(getresult);
        }
    } catch (SQLException e) {
        System.out.println("SQL-error getDetailData="+e.getMessage()+"script="+SQL);
        return(sqlresult);
      }
      catch (RuntimeException e) {
        System.out.println("Runtime-error getDetailData="+e.getMessage());
        return (sqlresult);
      }
      catch (Exception e) {
        System.out.println("Other-error getDetailData="+e.getMessage());
        return(sqlresult);
      }
*/

     return(sqlresult);
}



//---------------------------------------  Статистика по регионам--------------------------

    public String getRegionStat (HttpServletRequest request, HttpServletResponse response) throws IOException  {

        List outParams = new ArrayList<>();
        List inParams = new ArrayList();
        List inParamsValue = new ArrayList();
        message="";
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            if (parameterName.startsWith("out_"))
            {
                outParams.add(parameterName);
            }
            else if ( parameterName.startsWith("in_") )
            {

                if (StringUtil.isBlank(request.getParameter(parameterName)) || request.getParameter(parameterName).isEmpty() || request.getParameter(parameterName)==null)
                {

                }
                else {
                    inParams.add(parameterName);
                    inParamsValue.add(request.getParameter(parameterName));

                }
            }
        }
        //------------------------------------------
        String VPMN= "vpmn.short_name = upper('"+inParamsValue.get(inParams.indexOf("in_vpmn"))+"') and \n";


        String HPMN="";
        if ( inParams.contains("in_hpmn") ) {
            HPMN="hpmn.short_name = upper('"+inParamsValue.get(inParams.indexOf("in_hpmn"))+"') and \n";
        }

        String NAVIDATEFROM="";

        if  (!inParams.contains("in_seqfrom")) {
            NAVIDATEFROM = "f.navi_Date>=sysdate-30 and \n";
        }
        //------------------------
        if ( inParams.contains("in_navidatefrom") )
        {
            NAVIDATEFROM=" f.navi_Date>=to_date('"+inParamsValue.get(inParams.indexOf("in_navidatefrom"))+"','yyyy-mm-dd') and \n";
        }
        //------------------------
        String NAVIDATETO="";

        if ( inParams.contains("in_navidateto") )
        {  NAVIDATETO=" f.navi_Date < to_date('"+inParamsValue.get(inParams.indexOf("in_navidateto"))+"','yyyy-mm-dd') and \n";   }
        //--------------------------
        String SEQFROM ="";
        if  (inParams.contains("in_seqfrom")) {
            SEQFROM = "f.seq_num>="+inParamsValue.get(inParams.indexOf("in_seqfrom"))+" and \n";
        }
        String SEQTO ="";
        if  (inParams.contains("in_seqto")) {
            SEQTO = "f.seq_num<="+inParamsValue.get(inParams.indexOf("in_seqto"))+" and \n";
        }
        //------------------------------------------------
        String query_header="  f.file_name as tap_file, rap.file_name as rap_file, -sum(ad.agr_in_charge) as file_charge, -sum(ad.agr_in_tax) as file_tax";
        String query_from=" rss.rssmb_files rap, rss.rssmb_aggr_data ad, rss.rssmb_files f, rss.rssmb_roam_operators vpmn, rss.rssmb_roam_operators hpmn";
        String query_where=" rap.flcl_flcl_id=3 and \n" +
                VPMN  + HPMN  +NAVIDATEFROM +NAVIDATETO +SEQFROM +SEQTO+
                "rap.vpmn_rmop_id=vpmn.rmop_id and \n" +
                "rap.hpmn_rmop_id=hpmn.rmop_id and \n"+
                "rap.file_id=ad.file_file_id and \n" +
                "ad.del_date is null and \n"+
                "ad.tap_file_id=f.file_id \n" +
                "group by f.file_name, rap.file_name \n"+
                "order by f.file_name \n";

        String SQL=" SELECT /*+parallel(5)*/ "+query_header+" FROM "+query_from+" WHERE "+query_where;
        //  System.out.println(SQL);




           return (message);
    }

}
