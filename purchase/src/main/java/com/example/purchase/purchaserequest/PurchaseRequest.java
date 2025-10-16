package com.example.purchase.purchaserequest;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchaseorder.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Date;
enum Status {
    PENDING, APPROVED, REJECTED
}
@Entity
public class PurchaseRequest {
    @Id
    @GeneratedValue
    private Integer prid;
    private Integer eventid;  // foreign key from event table , one  to one mapping
    private Integer vendorid;   // foreign key from vendor table , many to one  mapping
    private Date requestdate;
    private Double allocatedamount;
    private Status prstatus;
    @OneToOne(mappedBy = "purchaserequest")

    private PurchaseOrder purchaseOrder;
    @OneToOne(mappedBy = "purchaseRequest")
    @JsonIgnore

    private Negotiation negotiation;

    public PurchaseRequest() {
    }

    public PurchaseRequest(Integer prid, Integer eventid, Integer vendorid, Date requestdate, Double allocatedamount, Status prstatus, PurchaseOrder purchaseOrder, Negotiation negotiation) {
        this.prid = prid;
        this.eventid = eventid;
        this.vendorid = vendorid;
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

    public Integer getVendorid() {
        return vendorid;
    }

    public void setVendorid(Integer vendorid) {
        this.vendorid = vendorid;
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
