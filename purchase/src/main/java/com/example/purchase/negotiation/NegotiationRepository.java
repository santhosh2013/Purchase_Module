package com.example.purchase.negotiation;

import com.example.purchase.purchaseorder.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegotiationRepository extends JpaRepository<PurchaseOrder,Integer> {
}