
function setForm ( vForm)
{

  document.getElementById("Form1").style.display="none";
  document.getElementById("Form2").style.display="none";
  document.getElementById("Form3").style.display="none";
  if (vForm !='all')
  {
  document.getElementById(vForm).style.display="block";
  }
}
//---------------------------------------------
function unlogFinal(msg)
{
	setForm('all');
	setDisabledEl("fsForm1",0);
	setDisabledEl("fsForm2",0);	
	document.getElementById("errMsg").innerHTML='<div id="errMsg">'+msg+'</div>';
	
}
//------------------------------------------------
function setDisabledEl (elId, state)
{
  if (state==1)
  {
   document.getElementById(elId).disabled=true;
   document.getElementById(elId).style.opacity="0.5";
   document.body.style.backgroundColor="#C0C0C0";
  }
  else
  {
   document.getElementById(elId).disabled=false;
   document.getElementById(elId).style.opacity="1";
   document.body.style.backgroundColor="#FFFFFF";
  }
}
//------------------------------------------------
function replaceDot (str)
{

  if ( typeof str === "number") {
    str=str.toString().replace(".",",");
  //  console.log(str);
  }
  return str;
}
//-----------------------------------------------
function replaceUndef (str)
{
   if (str === undefined)  {  str = ""; }
   if (str === null)  {  str = ""; }
   return str;
}

//----------------------------------------------------------------------
  function getRap()
  {
   // document.getElementById("errMsg").style.display="none";
    var v_header = [];
    var formData="";
    //--------------IN params
     formData=formData+"in_vpmnForm1="+(document.getElementById("in_vpmnForm1").value)+"&";
     formData=formData+"in_hpmnForm1="+(document.getElementById("in_hpmnForm1").value)+"&";
     formData=formData+"in_navidatefromForm1="+(document.getElementById("in_navidatefromForm1").value)+"&";
     formData=formData+"in_navidatetoForm1="+(document.getElementById("in_navidatetoForm1").value)+"&";
     formData=formData+"in_seqfromForm1="+(document.getElementById("in_seqfromForm1").value)+"&";
     formData=formData+"in_seqtoForm1="+(document.getElementById("in_seqtoForm1").value)+"&";

    ///----------------OUT param
     if (document.getElementById("out_tapForm1").checked) {
        v_header.push("tap_file");
        formData=formData+"out_tapForm1=1"+"&";
      }
     if (document.getElementById("out_rapForm1").checked) {
        v_header.push("rap_file");
        formData=formData+"out_rapForm1=1"+"&";
      }
     if (document.getElementById("out_chargeForm1").checked) {
        v_header.push("charge, SDR");
        formData=formData+"out_chargeForm1=1"+"&";
      }
     if (document.getElementById("out_taxForm1").checked) {
        v_header.push("tax, SDR");
        formData=formData+"out_taxForm1=1"+"&";
      }
      //---------------main logic

   if ( !document.getElementById("in_vpmnForm1").value  &&   !document.getElementById("in_hpmnForm1").value  )
    {
      document.getElementById("errMsg").innerHTML='<div   class="errMsgW"   id="errMsg">Укажите, пожалуйста, VPMN или HPMN</div>';
      document.getElementById("errMsg").style.display="block";
    }
    else
    {
      setDisabledEl ("fsForm1",1);
      document.getElementById("errMsg").innerHTML='<span  class="errMsgI"  id="errMsgForm1">Отчет формируется</span>';
    //  xhr_req("POST","http://localhost:8800/rss_portal/reports/rap",req_rap,formData, v_header);
      //xhr_req("POST","http://localhost:8800/rss_portal/reports/rap",req_file,formData, v_header, "rap_statistics", "fsForm1");
       xhr_reqRep("POST","http://msk-rss-appb3.megafon.ru:8765/rssvw/rap",req_file,formData, v_header, "rap_statistics", "fsForm1");    
    }

   }

  //-------------------------------
  function getDetails()
  {
    //document.getElementById("errMsg").style.display="none";
    let v_header = [];
    let formData="";

     // IN params
     formData=formData+"in_vpmnForm2="+(document.getElementById("in_vpmnForm2").value)+"&";
     formData=formData+"in_hpmnForm2="+(document.getElementById("in_hpmnForm2").value)+"&";
	 formData=formData+"in_imsiForm2="+(document.getElementById("in_imsiForm2").value)+"&";
     formData=formData+"in_starttimefromForm2="+(document.getElementById("in_starttimefromForm2").value)+"&";
     formData=formData+"in_starttimetoForm2="+(document.getElementById("in_starttimetoForm2").value)+"&";
     formData=formData+"in_mscForm2="+(document.getElementById("in_mscForm2").value)+"&";
     formData=formData+"in_branchForm2="+(document.getElementById("in_branchForm2").value)+"&";
     if (document.getElementById("in_srlsGPRSForm2").checked) { formData=formData+"in_srlsGPRSForm2=1"+"&"; }
     if (document.getElementById("in_srlsMOCForm2").checked) { formData=formData+"in_srlsMOCForm2=1"+"&"; }
     if (document.getElementById("in_srlsMTCForm2").checked) { formData=formData+"in_srlsMTCForm2=1"+"&"; }
     if (document.getElementById("in_srlsSMSForm2").checked) { formData=formData+"in_srlsSMSForm2=1"+"&"; }
     if (document.getElementById("in_srlsT12Form2").checked) { formData=formData+"in_srlsT12Form2=1"+"&"; }

     formData=formData+"in_seqfromForm2="+(document.getElementById("in_seqfromForm2").value)+"&";
     formData=formData+"in_seqtoForm2="+(document.getElementById("in_seqtoForm2").value)+"&";
     /*------------------------------------------------------------------------------
        OUT params : filename, imsi, starttime, duration, datavolume,destination, dialled,calltype,cltp,servicecode,
                     charge,tax,msc, ctl1, udf,lac
     -----------------------------------------------------------------------------------*/
      if (document.getElementById("out_filenameForm2").checked) {
        v_header.push("file_name");
        formData=formData+"out_filename=1"+"&";
      }
     if (document.getElementById("out_imsiForm2").checked) {
       v_header.push("imsi");
       formData=formData+"out_imsi=1"+"&";
     }
     if (document.getElementById("out_starttimeForm2").checked) {
       v_header.push("start_time");
       formData=formData+"out_starttime=1"+"&";
     }
     if (document.getElementById("out_durationForm2").checked) {
       v_header.push("duration, sec");
       formData=formData+"out_duration=1"+"&";
     }
     if (document.getElementById("out_datavolumeForm2").checked) {
       v_header.push("volume, bt");
       formData=formData+"out_datavolume=1"+"&";
     }
     if (document.getElementById("out_destinationForm2").checked) {
       v_header.push("destination");
       formData=formData+"out_destination=1"+"&";
     }
     if (document.getElementById("out_dialledForm2").checked) {
       v_header.push("dialled");
       formData=formData+"out_dialled=1"+"&";
      }
     if (document.getElementById("out_calltypeForm2").checked) {
       v_header.push("calltype");
       formData=formData+"out_calltype=1"+"&";
     }
     if (document.getElementById("out_cltpForm2").checked) {
       v_header.push("cltp");
       formData=formData+"out_cltp=1"+"&";
     }
     if (document.getElementById("out_servicecodeForm2").checked) {
       v_header.push("service_code");
       formData=formData+"out_servicecode=1"+"&";
     }
     if (document.getElementById("out_chargeForm2").checked) {
       v_header.push("charge, SDR");
       formData=formData+"out_charge=1"+"&";
     }
     if (document.getElementById("out_taxForm2").checked) {
       v_header.push("tax, SDR");
       formData=formData+"out_tax=1"+"&";
     }
     if (document.getElementById("out_mscForm2").checked) {
       v_header.push("msc");
       formData=formData+"out_msc=1"+"&";
     }
     if (document.getElementById("out_ctl1Form2").checked) {
       v_header.push("ctl1");
       formData=formData+"out_ctl1=1"+"&";
     }
     if (document.getElementById("out_udfForm2").checked) {
       v_header.push("branch");
       formData=formData+"out_udf=1"+"&";
     }
     if (document.getElementById("out_lacForm2").checked) {
       v_header.push("lac");
       formData=formData+"out_lac=1"+"&";
     }
      //---------------main logic
   if ( !document.getElementById("in_vpmnForm2").value  &&   !document.getElementById("in_hpmnForm2").value  && !document.getElementById("in_imsiForm2").value)
    {
      document.getElementById("errMsg").innerHTML='<div   class="errMsgW"   id="errMsg">Укажите, пожалуйста, VPMN и HPMN или ИМСИ</div>';
      document.getElementById("errMsg").style.display="block";
    }
    else
    {
      setDisabledEl ("fsForm2",1);
      document.getElementById("errMsg").innerHTML='<span  class="errMsgI"  id="errMsg">Отчет формируется</span>';    
      xhr_reqRep("POST","http://msk-rss-appb3.megafon.ru:8765/rssvw/details",req_file,formData, v_header, "details", "fsForm2");


    }
  }
//---------------------------------------
function req_file(p_header, p_body, p_filename, p_form)
{
  let fileheader="";
  let filebody ="";

  // Header
  p_header.forEach( (elem)=> { fileheader +=elem+";"; });
  fileheader +="\n";

 // Body
  let  err="";

  p_body.forEach( (elem) =>{

    for (let j=0; j<p_header.length; j++)
     {
       if (elem.err) {  err=elem.err;  }
       switch(p_header[j])
       {
         case 'file_name':
            filebody +=replaceUndef(elem.filename);
            break;
         case 'imsi':
            filebody +=replaceUndef(elem.imsi);
            break;
         case 'start_time':
            filebody +=replaceUndef(elem.starttime);
            break;
         case 'duration, sec':
            filebody +=replaceUndef(elem.duration);
            break;
         case 'volume, bt':
            filebody +=replaceUndef(elem.datavolume);
            break;
         case 'destination':
            filebody +=replaceUndef(elem.destination);
            break;
         case 'dialled':
            filebody +=replaceUndef(elem.dialleddigits);
            break;
         case 'calltype':
            filebody +=replaceUndef(elem.calltype);
            break;
         case 'cltp':
            filebody +=replaceUndef(elem.cltp);
            break;
         case 'service_code':
            filebody +=replaceUndef(elem.servicecode);
            break;
         case 'charge, SDR':
            filebody +=replaceDot(replaceUndef(elem.charge));
            break;
         case 'tax, SDR':
            filebody +=replaceDot(replaceUndef(elem.tax));
            break;
         case 'msc':
            filebody +=replaceUndef(elem.msc);
            break;
         case 'ctl1':
            filebody +=replaceUndef(elem.ctl1);
            break;
         case 'branch':
            filebody +=replaceUndef(elem.udf);
            break;
         case 'lac':
            filebody +=replaceUndef(elem.lac);
            break;
            //------------------------

         case 'tap_file':
            filebody +=replaceUndef(elem.tapname);
            break;
         case 'rap_file':
            filebody +=replaceUndef(elem.rapname);
            break;
         case 'charge, SDR':
            filebody +=replaceDot(replaceUndef(elem.charge));
            break;
         case 'tax, SDR':
            filebody +=replaceDot(replaceUndef(elem.tax));
            break;
       }
       filebody +=";";
     }
     filebody +="\n";
 });


 if (err == "" )
 {
  let hiddenElement =document.createElement("a");
  hiddenElement.href="data:text/csv;charset=utf-8,"+encodeURI(fileheader+filebody);
  hiddenElement.target="_blank";
  hiddenElement.download=p_filename+".csv";
  hiddenElement.click();
   document.getElementById("errMsg").innerHTML='<span  id="errMsg"></span>';
 }
 else
 {
   document.getElementById("errMsg").innerHTML='<div   class="errMsgW"  id="errMsg">'+err+'</div>';
 }

  setDisabledEl (p_form,0);

}

//-----------------------------

function xhr_reqRep(p_method, p_url, p_exec, p_data, p_header, p_filename, p_form) {
    let xhr = new XMLHttpRequest();
    xhr.open(p_method, p_url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    if (p_method.toUpperCase() == 'POST') {
		console.log("p_data="+p_data);
        xhr.send(p_data);
    } else {
        xhr.send();
    }


    xhr.onreadystatechange = function () {

        if (this.readyState == 4) {
            if (this.status == 200) {
             //  p_exec(p_header,JSON.parse(xhr.response));
              p_exec(p_header,JSON.parse(xhr.response),p_filename, p_form);
            } 
			else if (this.status == 401)
			{
			  unlogFinal();	
			  logIn(e);
			}
			else {
				unlogFinal('Ошибка https= '+this.status);				
                console.log('Error');
                //p_exec('err', this.response);
            }
        }
    }
}

//------------------------------------------------
function getRegions()
  {
   document.getElementById("errMsg").style.display="none";
    if (  ( document.getElementById("out_gprsForm3").checked && (document.getElementById("gprsVolume").value==0 || document.getElementById("gprsRndt").value==0) )||
          ( document.getElementById("out_voiceForm3").checked &&  (document.getElementById("voiceVolume").value==0 || document.getElementById("voiceRndt").value==0) )
         )
    {
      document.getElementById("errMsg").innerHTML='<div   class="errMsg"  name="errMsg" id="errMsg">Не для  всех  выбранных услуг  задан объем или тарификационные единицы</div>';
      document.getElementById("errMsg").style.display="block";
    }
    else if ( !document.getElementById("out_gprsForm3").checked  && !document.getElementById("out_voiceForm3").checked && !document.getElementById("out_smsForm3").checked)
     {
      document.getElementById("errMsg").innerHTML='<div   class="errMsg"  name="errMsg" id="errMsg">Не выбраны услуги, по которым  необходимо собрать статистику</div>';
      document.getElementById("errMsg").style.display="block";
     }
     else if ( !document.getElementById("vpmnForm3").value)
     {
      document.getElementById("errMsg").innerHTML='<div   class="errMsg"  name="errMsg" id="errMsg">Не заданы  VPMN для сбора статистики</div>';
      document.getElementById("errMsg").style.display="block";
     }
     else
     {
      document.Form3.submit();
     }
   }
