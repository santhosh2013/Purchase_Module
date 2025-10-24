package com.example.purchase.purchaseorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class PurchaseOrderDTO {
    @JsonProperty("po_id")
    private Integer PO_id;

    private Integer eventid;
    private String eventname;
    private Integer vendorid;
    private String vendorname;
  
    private String cdsid;
    private Date orderdate;
    private Double orderamountINR;
    private Double orderamountdollar;

    @JsonProperty("po_status")
    private String PO_status;

    private Integer prid;
    private Integer negotiationid;

    public PurchaseOrderDTO() {
    }

    public PurchaseOrderDTO(Integer PO_id, Integer eventid, String eventname, Integer vendorid,
            String vendorname, String cdsid, Date orderdate, Double orderamountINR,
            Double orderamountdollar, String PO_status, Integer prid, Integer negotiationid) {
        this.PO_id = PO_id;
        this.eventid = eventid;
        this.eventname = eventname;
        this.vendorid = vendorid;
        this.vendorname = vendorname;
       
        this.cdsid = cdsid;
        this.orderdate = orderdate;
        this.orderamountINR = orderamountINR;
        this.orderamountdollar = orderamountdollar;
        this.PO_status = PO_status;
        this.prid = prid;
        this.negotiationid = negotiationid;
    }

    public Integer getPO_id() {
        return PO_id;
    }

    public void setPO_id(Integer PO_id) {
        this.PO_id = PO_id;
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

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Double getOrderamountINR() {
        return orderamountINR;
    }

    public void setOrderamountINR(Double orderamountINR) {
        this.orderamountINR = orderamountINR;
    }

    public Double getOrderamountdollar() {
        return orderamountdollar;
    }

    public void setOrderamountdollar(Double orderamountdollar) {
        this.orderamountdollar = orderamountdollar;
    }

    public String getPO_status() {
        return PO_status;
    }

    public void setPO_status(String PO_status) {
        this.PO_status = PO_status;
    }

    public Integer getPrid() {
        return prid;
    }

    public void setPrid(Integer prid) {
        this.prid = prid;
    }

    public Integer getNegotiationid() {
        return negotiationid;
    }

    public void setNegotiationid(Integer negotiationid) {
        this.negotiationid = negotiationid;
    }
}
