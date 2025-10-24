package com.example.purchase.negotiation;

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
@RequestMapping("/api/negotiations")
@Tag(name = "Negotiation Management", description = "APIs for managing negotiations with vendors")
public class NegotiationController {

    @Autowired
    private NegotiationService negotiationService;

    @GetMapping
    @Operation(summary = "Get all negotiations")
    public ResponseEntity<List<NegotiationDTO>> getAllNegotiations() {
        return ResponseEntity.ok(negotiationService.getAllNegotiations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get negotiation by ID")
    public ResponseEntity<NegotiationDTO> getNegotiationById(
            @PathVariable @Parameter(description = "Negotiation ID") Integer id) {
        return ResponseEntity.ok(negotiationService.getNegotiationById(id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get negotiations by status")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsByStatus(
            @PathVariable @Parameter(description = "Status (e.g., Pending, Completed, Cancelled)") String status) {
        return ResponseEntity.ok(negotiationService.getNegotiationsByStatus(status));
    }

    @GetMapping("/vendor/{vendorid}")
    @Operation(summary = "Get negotiations by vendor ID")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsByVendor(
            @PathVariable @Parameter(description = "Vendor ID") Integer vendorid) {
        return ResponseEntity.ok(negotiationService.getNegotiationsByVendor(vendorid));
    }

    @GetMapping("/event/{eventid}")
    @Operation(summary = "Get negotiations by event ID")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsByEvent(
            @PathVariable @Parameter(description = "Event ID") Integer eventid) {
        return ResponseEntity.ok(negotiationService.getNegotiationsByEvent(eventid));
    }

    @GetMapping("/cdsid/{cdsid}")
    @Operation(summary = "Get negotiations by CDSID")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsByCdsid(
            @PathVariable @Parameter(description = "CDSID") String cdsid) {
        return ResponseEntity.ok(negotiationService.getNegotiationsByCdsid(cdsid));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get negotiations by year")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsByYear(
            @PathVariable @Parameter(description = "Year (e.g., 2024)") int year) {
        return ResponseEntity.ok(negotiationService.getNegotiationsByYear(year));
    }

    @GetMapping("/filter")
    @Operation(summary = "Get negotiations by date range")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsByDateRange(
            @RequestParam @Parameter(description = "Start date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @Parameter(description = "End date (yyyy-MM-dd)")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        return ResponseEntity.ok(negotiationService.getNegotiationsByDateRange(fromDate, toDate));
    }

    @GetMapping("/savings")
    @Operation(summary = "Get negotiations with cost savings")
    public ResponseEntity<List<NegotiationDTO>> getNegotiationsWithSavings() {
        return ResponseEntity.ok(negotiationService.getNegotiationsWithSavings());
    }

    @PostMapping
    @Operation(summary = "Create new negotiation")
    public ResponseEntity<NegotiationDTO> createNegotiation(@RequestBody NegotiationDTO negotiationDTO) {
        return new ResponseEntity<>(negotiationService.createNegotiation(negotiationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/from-pr/{prid}")
    @Operation(summary = "Create negotiation from Purchase Request")
    public ResponseEntity<NegotiationDTO> createFromPurchaseRequest(
            @PathVariable @Parameter(description = "Purchase Request ID") Integer prid) {
        return new ResponseEntity<>(negotiationService.createNegotiationFromPR(prid), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update negotiation")
    public ResponseEntity<NegotiationDTO> updateNegotiation(
            @PathVariable Integer id, @RequestBody NegotiationDTO negotiationDTO) {
        return ResponseEntity.ok(negotiationService.updateNegotiation(id, negotiationDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete negotiation")
    public ResponseEntity<Void> deleteNegotiation(@PathVariable Integer id) {
        negotiationService.deleteNegotiation(id);
        return ResponseEntity.noContent().build();
    }
}
