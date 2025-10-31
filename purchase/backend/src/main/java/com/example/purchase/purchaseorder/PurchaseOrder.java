package com.example.purchase.purchaseorder;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchaserequest.PurchaseRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "purchase_order")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "PO_ID")
    private Integer PO_id;

    @Column(name = "EVENTID")
    @NotNull
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
/*
    public PurchaseOrder() {   }

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
    }*/
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