package com.example.purchase.purchase_request;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.purchase_order.Po;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pr {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "PR_id")
private Long prId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(name = "allocated_amount", precision = 12, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(name = "PR_status", length = 50)
    private String prStatus;

    // Bidirectional 1-to-1 with Negotiation (inverse side)
    @OneToOne(mappedBy = "purchaseRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Negotiation negotiation;

    // Unidirectional 1-to-1 to PurchaseOrder
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Po purchaseOrder;
}