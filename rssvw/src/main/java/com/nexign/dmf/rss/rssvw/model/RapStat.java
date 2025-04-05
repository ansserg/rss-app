package com.nexign.dmf.rss.rssvw.model;

public class RapStat {
    private String tapname;
    private String rapname;
    private Float charge;
    private Float tax;
    private String err;

    public RapStat() {
        this.tapname = tapname;
        this.rapname = rapname;
        this.charge = charge;
        this.tax = tax;
        this.err =err;
    }

    public String getTapname() {
        return tapname;
    }

    public void setTapname(String tapname) {
        this.tapname = tapname;
    }

    public String getRapname() {
        return rapname;
    }

    public void setRapname(String rapname) {
        this.rapname = rapname;
    }

    public Float getCharge() {
        return charge;
    }

    public void setCharge(Float charge) {
        this.charge = charge;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }
    public String getErr() {
        return err;
    }
    public void setErr(String err) {
        this.err = err;
    }
}
