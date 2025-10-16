package com.example.purchase.negotiation;

import com.example.purchase.purchaserequest.PurchaseRequest;
import jakarta.persistence.*;
enum status {
    PENDING,
    APPROVED,
    REJECTED,
    COMPLETED
}

@Entity
public class Negotiation {
    @Id
    private Integer negotiationid;
    private Integer eventid;  // foreign key from event table , one to one  mapping
    private Integer vendorid;  // foreign key from vendor table , many to one  mapping
    private Double initialquoteamount;
    private Double finalquoteamount;
    private String negotiationstatus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prid")
    //@JsonIgnore
    private PurchaseRequest purchaseRequest;


    public Negotiation() {
    }

    public Negotiation(Integer negotiationid, Integer eventid, Integer vendorid, Double initialquoteamount, Double finalquoteamount, String negotiationstatus, PurchaseRequest purchaseRequest) {
        this.negotiationid = negotiationid;
        this.eventid = eventid;
        this.vendorid = vendorid;
        this.initialquoteamount = initialquoteamount;
        this.finalquoteamount = finalquoteamount;
        this.negotiationstatus = negotiationstatus;
        this.purchaseRequest = purchaseRequest;

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

    public Integer getVendorid() {
        return vendorid;
    }

    public void setVendorid(Integer vendorid) {
        this.vendorid = vendorid;
    }

    public Double getInitialquoteamount() {
        return initialquoteamount;
    }

    public void setInitialquoteamount(Double initialquoteamount) {
        this.initialquoteamount = initialquoteamount;
    }

    public Double getFinalquoteamount() {
        return finalquoteamount;
    }

    public void setFinalquoteamount(Double finalquoteamount) {
        this.finalquoteamount = finalquoteamount;
    }

    public String getNegotiationstatus() {
        return negotiationstatus;
    }

    public void setNegotiationstatus(String negotiationstatus) {
        this.negotiationstatus = negotiationstatus;
    }

    public PurchaseRequest getPurchaseRequest() {
        return purchaseRequest;
    }

    public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
    }
}