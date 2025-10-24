package com.example.purchase.purchaseorder;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchaserequest.PurchaseRequest;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PO_ID")
    private Integer PO_id;

    @Column(name = "EVENTID")
    private Integer eventid;

    @Column(name = "EVENTNAME")
    private String eventname;

    @Column(name = "VENDORID")
    private Integer vendorid;

    @Column(name = "VENDORNAME")
    private String vendorname;

    @Column(name = "CDSID", nullable = false, length = 50)
    private String cdsid;

    @Column(name = "ORDERDATE")
    private Date orderdate;

    @Column(name = "ORDERAMOUNTINR")
    private Double orderamountINR;

    @Column(name = "ORDERAMOUNTDOLLAR")
    private Double orderamountdollar;

    @Column(name = "PO_STATUS")
    private String PO_status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prid")
    private PurchaseRequest purchaserequest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "negotiationid")
    private Negotiation negotiation;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Integer PO_id, Integer eventid, String eventname, Integer vendorid,
                         String vendorname, String cdsid, Date orderdate, Double orderamountINR,
                         Double orderamountdollar, String PO_status, PurchaseRequest purchaserequest,
                         Negotiation negotiation) {
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
        this.purchaserequest = purchaserequest;
        this.negotiation = negotiation;
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
