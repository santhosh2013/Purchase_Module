package com.example.purchase.purchaserequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchaserequests")
public class PurchaseRequestController {
    @Autowired
    PurchaseRequestRepository purchaseRequestRepository;

    @PostMapping("/createpurchasingrequest")
    public PurchaseRequest createPurchaseRequest(@RequestBody PurchaseRequest purchaseRequest) {

        return purchaseRequestRepository.save(purchaseRequest);

    }


}