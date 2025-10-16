package com.example.purchase.negotiation;
import com.example.purchase.purchase_order.Po;
import com.example.purchase.purchase_request.Pr;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "negotiation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Negotiation {@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "Negotiation_id")
private Long negotiationId;

    @Column(name = "training_course_id", nullable = false)
    private Long trainingCourseId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "Initial_Quote_Amount", precision = 12, scale = 2)
    private BigDecimal initialQuoteAmount;

    @Column(name = "final_quote", precision = 12, scale = 2)
    private BigDecimal finalQuote;

    @Column(name = "Negotiation_status", length = 50)
    private String negotiationStatus;

    @Column(name = "PR_id", nullable = false, insertable = false, updatable = false)
    private Long prId;

    // Bidirectional 1-to-1 with PurchaseRequest (owning side)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PR_id", referencedColumnName = "PR_id")
    private Pr purchaseRequest;
}