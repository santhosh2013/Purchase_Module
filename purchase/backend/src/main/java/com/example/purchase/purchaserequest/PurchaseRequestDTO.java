package com.example.purchase.purchaserequest;

import java.util.Date;

public class PurchaseRequestDTO {
    private Integer prid;
    private Integer eventid;
    private String eventname;
    private Integer vendorid;
    private String vendorname;
    private String cdsid;
    private Date requestdate;
    private Double allocatedamount;
    private String prstatus;

    public PurchaseRequestDTO() {
    }

    public PurchaseRequestDTO(Integer prid, Integer eventid, String eventname, Integer vendorid,
                              String vendorname, String cdsid, Date requestdate,
                              Double allocatedamount, String prstatus) {
        this.prid = prid;
        this.eventid = eventid;
        this.eventname = eventname;
        this.vendorid = vendorid;
        this.vendorname = vendorname;
        this.cdsid = cdsid;
        this.requestdate = requestdate;
        this.allocatedamount = allocatedamount;
        this.prstatus = prstatus;
    }

    public Integer getPrid() {
        return prid;
    }

    public void setPrid(Integer prid) {
        this.prid = prid;
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

    public Date getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(Date requestdate) {
        this.requestdate = requestdate;
    }

    public Double getAllocatedamount() {
        return allocatedamount;
    }

    public void setAllocatedamount(Double allocatedamount) {
        this.allocatedamount = allocatedamount;
    }

    public String getPrstatus() {
        return prstatus;
    }

    public void setPrstatus(String prstatus) {
        this.prstatus = prstatus;
    }
}
