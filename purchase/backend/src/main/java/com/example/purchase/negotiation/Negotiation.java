package com.example.purchase.negotiation;

import com.example.purchase.purchaserequest.PurchaseRequest;
import com.example.purchase.purchaseorder.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "negotiations")
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer negotiationid;

    private Integer eventid; // foreign key from event table

    @Column(name = "EVENTNAME")
    private String eventname;

    private Integer vendorid; // foreign key from vendor table

    @Column(name = "VENDORNAME")
    private String vendorname;

 

    @Column(name = "CDSID", nullable = false, length = 50)
    private String cdsid; // who is conducting negotiation

    private Date negotiationdate;
    private Double initialquoteamount;
    private Double finalamount;
    private String negotiationstatus; // Pending, Completed, Cancelled
    private String notes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prid")
    private PurchaseRequest purchaseRequest;

    @OneToOne(mappedBy = "negotiation")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    public Negotiation() {
    }

    public Negotiation(Integer negotiationid, Integer eventid, String eventname, Integer vendorid,
            String vendorname,  String cdsid, Date negotiationdate, Double initialquoteamount,
            Double finalamount, String negotiationstatus, String notes,
            PurchaseRequest purchaseRequest, PurchaseOrder purchaseOrder) {
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
        this.purchaseRequest = purchaseRequest;
        this.purchaseOrder = purchaseOrder;
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

    public PurchaseRequest getPurchaseRequest() {
        return purchaseRequest;
    }

    public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
