package com.example.purchase.purchaseorder;

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
@RequestMapping("/api/purchase-orders")
@Tag(name = "Purchase Order Management", description = "APIs for managing purchase orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping
    @Operation(summary="created a new purchase order")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO purchaseOrderDTO)throws InvalidInputException {
        return new ResponseEntity<>(purchaseOrderService.createPurchaseOrder(purchaseOrderDTO),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> updateStatus(@PathVariable Integer id,@RequestBody PurchaseOrderDTO dto) throws InvalidInputException {
        return new ResponseEntity<>(purchaseOrderService.updatePurchaseOrder(id,dto),HttpStatus.OK);
    }
    @PutMapping("/{id}/reject")
    public ResponseEntity<PurchaseOrderDTO> rejectStatus(@PathVariable Integer id) throws InvalidInputException {
        return ResponseEntity.ok(purchaseOrderService.rejectPurchaseOrder(id));
    }

    @GetMapping("/cdsid/{cdsid}")
    @Operation(summary = "Get purchase orders by CDSID")
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrdersByCds(@PathVariable String cdsid) throws InvalidInputException {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByCdsid(cdsid));
    }

    @GetMapping("/event/{eventid}")
    @Operation(summary = "Get purchase orders by event ID")
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrdersByEvent(
            @PathVariable  Integer eventid) throws InvalidInputException {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByEvent(eventid));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get purchase orders by year")
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrdersByYear(
            @PathVariable  int year) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByYear(year));
    }

    @GetMapping("/filter")
    @Operation(summary = "Get purchase orders by date range")
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrdersByDateRange(
            @RequestParam @Parameter(description = "Start date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @Parameter(description = "End date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByDateRange(fromDate, toDate));
    }

    @GetMapping
    @Operation(summary = "Get all purchase orders")
    public ResponseEntity<List<PurchaseOrderDTO>> getAllPurchaseOrders()
    {
        return ResponseEntity.ok(purchaseOrderService.getAllPurchaseOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrderById(
            @PathVariable @Parameter(description = "Purchase Order ID") Integer id)
            throws InvalidInputException
    {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get purchase orders by status")
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrdersByStatus(
            @PathVariable @Parameter(description = "Status (COMPLETED, REJECTED)") String status)
            throws InvalidStatusException
    {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByStatus(status));
    }

    @GetMapping("/vendor/{vendorid}")
    @Operation(summary = "Get purchase orders by vendor ID")
    public ResponseEntity<List<PurchaseOrderDTO>> getPurchaseOrdersByVendor(
            @PathVariable @Parameter(description = "Vendor ID") Integer vendorid)
            throws InvalidInputException
    {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrdersByVendor(vendorid));
    }

    @GetMapping("/completed")
    @Operation(summary = "Get all completed purchase orders")
    public ResponseEntity<List<PurchaseOrderDTO>> getCompletedPurchaseOrders() throws DataNotFoundException {
        return ResponseEntity.ok(purchaseOrderService.getCompletedPurchaseOrders());
    }

    @GetMapping("/vendor/{vendorid}/total")
    @Operation(summary = "Get total order amount by vendor")
    public ResponseEntity<Double> getTotalOrderAmountByVendor(
            @PathVariable @Parameter(description = "Vendor ID") Integer vendorid)
            throws InvalidInputException
    {
        return ResponseEntity.ok(purchaseOrderService.getTotalOrderAmountByVendor(vendorid));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark purchase order as completed")
    public ResponseEntity<PurchaseOrderDTO> completePurchaseOrder(@PathVariable Integer id) throws InvalidInputException{
        return ResponseEntity.ok(purchaseOrderService.completePurchaseOrder(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete purchase order")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Integer id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.noContent().build();
    }
}
