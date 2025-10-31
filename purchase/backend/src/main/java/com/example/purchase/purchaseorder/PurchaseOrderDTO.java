package com.example.purchase.purchaseorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PurchaseOrderDTO {
    @JsonProperty("po_id")
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
    
    @JsonProperty("po_status")
    @NotBlank(message = "Purchase Order status is required")
    @Pattern(regexp = "^(PENDING|COMPLETED|REJECTED)$", message = "Status must be PENDING, COMPLETED, or REJECTED")
    private String PO_status;
    
    @Positive(message = "Purchase Request ID must be positive")
    private Integer prid;

    @Positive(message = "Negotiation ID must be positive")
    private Integer negotiationid;

   /* public PurchaseOrderDTO() {
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
*/
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
