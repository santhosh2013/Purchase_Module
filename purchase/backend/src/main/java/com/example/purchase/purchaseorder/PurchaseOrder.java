package com.example.purchase.purchaseorder;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchaserequest.PurchaseRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer PO_id;

    @NotNull(message = "Event ID is required")
    @Positive(message = "Event ID must be positive")
    private Integer eventid;

    @NotBlank(message = "Event name is required")
    @Size(max = 255, message = "Event name must not exceed 255 characters")
    private String eventname;

    @NotNull(message = "Vendor ID is required")
    @Positive(message = "Vendor ID must be positive")
    private Integer vendorid;

    @NotBlank(message = "Vendor name is required")
    @Size(max = 255, message = "Vendor name must not exceed 255 characters")
    private String vendorname;

    @NotBlank(message = "CDSID is required")
    @Size(min = 1, max = 50, message = "CDSID must be between 1 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "CDSID can only contain alphanumeric characters, hyphens, and underscores")
    @Column( nullable = false, length = 50)
    private String cdsid;

    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date cannot be in the future")
    private Date orderdate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Order amount in INR must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Order amount in INR must have at most 10 integer digits and 2 decimal places")
    private Double orderamountINR;

    @DecimalMin(value = "0.0", inclusive = false, message = "Order amount in USD must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Order amount in USD must have at most 10 integer digits and 2 decimal places")

    private Double orderamountdollar;

    @NotBlank(message = "Purchase Order status is required")
    @Pattern(regexp = "^(PENDING|COMPLETED|REJECTED)$", message = "Status must be PENDING, COMPLETED, or REJECTED")

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
