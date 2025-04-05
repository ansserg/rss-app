package com.nexign.dmf.rss.rssvw.model;


public class RepDetails {
    private String imsi;
    private String starttime;
    private String filename;
    private Float charge;
    private int duration;
    private int datavolume;
    private String destination;
    private String msc;
    private String udf;
    private String lac;
    private String ctl1;
    private String calltype;
    private String cltp;
    private String servicecode;
    private Float tax;
    private String dialleddigits;
    private String err;

    public RepDetails() {
        this.imsi = imsi;
        this.starttime = starttime;
        this.filename = filename;
        this.charge = charge;
        this.duration = duration;
        this.datavolume = datavolume;
        this.destination = destination;
        this.msc = msc;
        this.udf = udf;
        this.lac = lac;
        this.ctl1 = ctl1;
        this.calltype = calltype;
        this.tax = tax;
        this.dialleddigits = dialleddigits;
        this.udf = udf;
        this.cltp = cltp;
        this.servicecode = servicecode;
        this.err =err;
    }

    public String getImsi() {
        return imsi;
    }
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getStarttime() {
        return starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public Float getCharge() {
        return charge;
    }
    public void setCharge(Float charge) {
        this.charge = charge;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getDatavolume() {
        return datavolume;
    }
    public void setDatavolume(int datavolume) {
        this.datavolume = datavolume;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getMsc() {
        return msc;
    }
    public void setMsc(String msc) {
        this.msc = msc;
    }
    public String getUdf() {
        return udf;
    }
    public void setUdf(String udf) {
        this.udf = udf;
    }
    public String getLac() {
        return lac;
    }
    public void setLac(String lac) {
        this.lac = lac;
    }
    public String getCtl1() {
        return ctl1;
    }
    public void setCtl1(String ctl1) {
        this.ctl1 = ctl1;
    }
    public String getCalltype() {
        return calltype;
    }
    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }
    public Float getTax() {
        return tax;
    }
    public void setTax(Float tax) {
        this.tax = tax;
    }
    public String getDialleddigits() {
        return dialleddigits;
    }
    public void setDialleddigits(String dialleddigits) {
        this.dialleddigits = dialleddigits;
    }
    public String getCltp() {
        return cltp;
    }
    public void setCltp(String cltp) {
        this.cltp = cltp;
    }
    public String getServicecode() {
        return servicecode;
    }
    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }
    public String getErr() {
        return err;
    }
    public void setErr(String err) {
        this.err = err;
    }
}
