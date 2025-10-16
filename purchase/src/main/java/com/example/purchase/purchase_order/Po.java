package com.example.purchase.purchase_order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Po {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_id")
    private Long poId;

    @Column(name = "training_course_id", nullable = false)
    private Long trainingCourseId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "amount_INR", precision = 12, scale = 2)
    private BigDecimal amountINR;

    @Column(name = "amount_$", precision = 12, scale = 2)
    private BigDecimal amountUSD;

    @Column(name = "PO_status", length = 50)
    private String poStatus; // DRAFT, APPROVED, SENT, CLOSED, CANCELLED

    @Column(name = "PR_id", nullable = false)
    private Long prId;

    @Column(name = "negotiation_id")
    private Long negotiationId;

}