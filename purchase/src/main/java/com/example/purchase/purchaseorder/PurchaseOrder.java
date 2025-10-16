package com.example.purchase.purchaseorder;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchaserequest.PurchaseRequest;
import jakarta.persistence.*;

import java.util.Date;

enum status1 {
    REJECTED, COMPLETED
}
@Entity

public class PurchaseOrder {
    @Id

    private Integer PO_id;
    private Integer eventid;  // foreign key from event table , one  to one mapping
    private Integer vendorid; // foreign key from vendor table , many to one  mapping
    private Date orderdate;
    private Double orderamountINR;
    private Double orderamountdollar;
    private String PO_status;
    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="prid")
    private PurchaseRequest purchaserequest;
    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="negotiationid")
    private Negotiation negotiation;

    public PurchaseOrder(Integer PO_id, Integer eventid, Integer vendorid, Date orderdate, Double orderamountINR, Double orderamountdollar, String PO_status, PurchaseRequest purchaserequest, Negotiation negotiation) {
        this.PO_id = PO_id;
        this.eventid = eventid;
        this.vendorid = vendorid;
        this.orderdate = orderdate;
        this.orderamountINR = orderamountINR;
        this.orderamountdollar = orderamountdollar;
        this.PO_status = PO_status;
        this.purchaserequest = purchaserequest;
        this.negotiation = negotiation;
    }
    public PurchaseOrder() {
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

    public Integer getVendorid() {
        return vendorid;
    }

    public void setVendorid(Integer vendorid) {
        this.vendorid = vendorid;
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

    public PurchaseRequest getpurchaserequest() {
        return purchaserequest;
    }

    public void setpurchaserequest(PurchaseRequest purchaserequest) {
        this.purchaserequest = purchaserequest;
    }

    public Negotiation getnegotiation() {
        return negotiation;
    }

    public void setnegotiation(Negotiation negotiation) {
        this.negotiation = negotiation;
    }
}