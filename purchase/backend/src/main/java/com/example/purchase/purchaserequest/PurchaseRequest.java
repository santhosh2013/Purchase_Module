package com.example.purchase.purchaserequest;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchaseorder.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prid;
    
    private Integer eventid;  // foreign key from event table, one to one mapping
    
    @Column(name = "EVENTNAME")
    private String eventname;
    
    private Integer vendorid;   // foreign key from vendor table, many to one mapping
    
    @Column(name = "VENDORNAME")
    private String vendorname;
    
    @Column(name = "CDSID", nullable = false, length = 50)
    private String cdsid;     // who generated the PR
    
    private Date requestdate;
    private Double allocatedamount;

    @Enumerated(EnumType.STRING)
    private Status prstatus;

    @OneToOne(mappedBy = "purchaserequest")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    @OneToOne(mappedBy = "purchaseRequest")
    @JsonIgnore
    private Negotiation negotiation;

    public PurchaseRequest() {
    }

    public PurchaseRequest(Integer prid, Integer eventid, String eventname, Integer vendorid, 
                           String vendorname, String cdsid, Date requestdate, 
                           Double allocatedamount, Status prstatus, PurchaseOrder purchaseOrder, 
                           Negotiation negotiation) {
        this.prid = prid;
        this.eventid = eventid;
        this.eventname = eventname;
        this.vendorid = vendorid;
        this.vendorname = vendorname;
        this.cdsid = cdsid;
        this.requestdate = requestdate;
        this.allocatedamount = allocatedamount;
        this.prstatus = prstatus;
        this.purchaseOrder = purchaseOrder;
        this.negotiation = negotiation;
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

    public Status getPrstatus() {
        return prstatus;
    }

    public void setPrstatus(Status prstatus) {
        this.prstatus = prstatus;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Negotiation getNegotiation() {
        return negotiation;
    }

    public void setNegotiation(Negotiation negotiation) {
        this.negotiation = negotiation;
    }
}
