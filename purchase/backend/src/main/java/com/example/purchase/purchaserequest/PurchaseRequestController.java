package com.example.purchase.purchaserequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-requests")
@Tag(name = "Purchase Request Management", description = "APIs for managing purchase requests")
public class PurchaseRequestController {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @GetMapping
    @Operation(summary = "Get all purchase requests")
    public ResponseEntity<List<PurchaseRequestDTO>> getAllPurchaseRequests() {
        return ResponseEntity.ok(purchaseRequestService.getAllPurchaseRequests());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase request by ID")
    public ResponseEntity<PurchaseRequestDTO> getPurchaseRequestById(
            @PathVariable @Parameter(description = "Purchase Request ID") Integer id) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get purchase requests by status")
    public ResponseEntity<List<PurchaseRequestDTO>> getPurchaseRequestsByStatus(
            @PathVariable @Parameter(description = "Status (PENDING, APPROVED, REJECTED)") String status) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestsByStatus(Status.valueOf(status)));
    }

    @GetMapping("/vendor/{vendorid}")
    @Operation(summary = "Get purchase requests by vendor ID")
    public ResponseEntity<List<PurchaseRequestDTO>> getPurchaseRequestsByVendor(
            @PathVariable @Parameter(description = "Vendor ID") Integer vendorid) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestsByVendor(vendorid));
    }

    @GetMapping("/event/{eventid}")
    @Operation(summary = "Get purchase requests by event ID")
    public ResponseEntity<List<PurchaseRequestDTO>> getPurchaseRequestsByEvent(
            @PathVariable @Parameter(description = "Event ID") Integer eventid) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestsByEvent(eventid));
    }

    @GetMapping("/cdsid/{cdsid}")
    @Operation(summary = "Get purchase requests by CDSID")
    public ResponseEntity<List<PurchaseRequestDTO>> getPurchaseRequestsByCdsid(
            @PathVariable @Parameter(description = "CDSID") String cdsid) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestsByCdsid(cdsid));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get purchase requests by year")
    public ResponseEntity<List<PurchaseRequestDTO>> getPurchaseRequestsByYear(
            @PathVariable @Parameter(description = "Year (e.g., 2024)") int year) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestsByYear(year));
    }

    @GetMapping("/filter")
    @Operation(summary = "Get purchase requests by date range")
    public ResponseEntity<List<PurchaseRequestDTO>> getPurchaseRequestsByDateRange(
            @RequestParam @Parameter(description = "Start date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @Parameter(description = "End date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequestsByDateRange(fromDate, toDate));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending purchase requests")
    public ResponseEntity<List<PurchaseRequestDTO>> getPendingPurchaseRequests() {
        return ResponseEntity.ok(purchaseRequestService.getPendingPurchaseRequests());
    }

    @GetMapping("/approved")
    @Operation(summary = "Get all approved purchase requests")
    public ResponseEntity<List<PurchaseRequestDTO>> getApprovedPurchaseRequests() {
        return ResponseEntity.ok(purchaseRequestService.getApprovedPurchaseRequests());
    }

    @PostMapping
    @Operation(summary = "Create new purchase request")
    public ResponseEntity<PurchaseRequestDTO> createPurchaseRequest(@RequestBody PurchaseRequestDTO dto) {
        return new ResponseEntity<>(purchaseRequestService.createPurchaseRequest(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update purchase request")
    public ResponseEntity<PurchaseRequestDTO> updatePurchaseRequest(
            @PathVariable Integer id, @RequestBody PurchaseRequestDTO dto) {
        return ResponseEntity.ok(purchaseRequestService.updatePurchaseRequest(id, dto));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve purchase request")
    public ResponseEntity<PurchaseRequestDTO> approvePurchaseRequest(@PathVariable Integer id) {
        return ResponseEntity.ok(purchaseRequestService.approvePurchaseRequest(id));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject purchase request")
    public ResponseEntity<PurchaseRequestDTO> rejectPurchaseRequest(@PathVariable Integer id) {
        return ResponseEntity.ok(purchaseRequestService.rejectPurchaseRequest(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete purchase request")
    public ResponseEntity<Void> deletePurchaseRequest(@PathVariable Integer id) {
        purchaseRequestService.deletePurchaseRequest(id);
        return ResponseEntity.noContent().build();
    }
}
