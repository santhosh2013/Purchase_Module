package com.example.purchase.negotiation;

import java.util.Date;

public class NegotiationDTO {
    private Integer negotiationid;
    private Integer eventid;
    private String eventname;
    private Integer vendorid;
    private String vendorname;
    private String cdsid;
    private Date negotiationdate;
    private Double initialquoteamount;
    private Double finalamount;
    private String negotiationstatus;
    private String notes;
    private Integer prid;

    public NegotiationDTO() {
    }

    public NegotiationDTO(Integer negotiationid, Integer eventid, String eventname, Integer vendorid,
                          String vendorname, String cdsid, Date negotiationdate, Double initialquoteamount,
                          Double finalamount, String negotiationstatus, String notes, Integer prid) {
        this.negotiationid = negotiationid;
        this.eventid = eventid;
        this.eventname = eventname;
        this.vendorid = vendorid;
        this.vendorname = vendorname;
        this.cdsid = cdsid;
        this.negotiationdate = negotiationdate;
        this.initialquoteamount = initialquoteamount;
        this.finalamount = finalamount;
        this.negotiationstatus = negotiationstatus;
        this.notes = notes;
        this.prid = prid;
    }

    public Integer getNegotiationid() {
        return negotiationid;
    }

    public void setNegotiationid(Integer negotiationid) {
        this.negotiationid = negotiationid;
    }

    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public Integer getVendorid() {
        return vendorid;
    }

    public void setVendorid(Integer vendorid) {
        this.vendorid = vendorid;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getCdsid() {
        return cdsid;
    }

    public void setCdsid(String cdsid) {
        this.cdsid = cdsid;
    }

    public Date getNegotiationdate() {
        return negotiationdate;
    }

    public void setNegotiationdate(Date negotiationdate) {
        this.negotiationdate = negotiationdate;
    }

    public Double getInitialquoteamount() {
        return initialquoteamount;
    }

    public void setInitialquoteamount(Double initialquoteamount) {
        this.initialquoteamount = initialquoteamount;
    }

    public Double getFinalamount() {
        return finalamount;
    }

    public void setFinalamount(Double finalamount) {
        this.finalamount = finalamount;
    }

    public String getNegotiationstatus() {
        return negotiationstatus;
    }

    public void setNegotiationstatus(String negotiationstatus) {
        this.negotiationstatus = negotiationstatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPrid() {
        return prid;
    }

    public void setPrid(Integer prid) {
        this.prid = prid;
    }
}
