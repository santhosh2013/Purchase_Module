package com.example.purchase.negotiation;

import com.example.purchase.purchaserequest.PurchaseRequest;
import com.example.purchase.purchaserequest.PurchaseRequestRepository;
import com.example.purchase.purchaserequest.Status;
import com.example.purchase.purchaseorder.PurchaseOrder;
import com.example.purchase.purchaseorder.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NegotiationService {

    @Autowired
    private NegotiationRepository negotiationRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    // Get all negotiations
    public List<NegotiationDTO> getAllNegotiations() {
        return negotiationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiation by id
    public NegotiationDTO getNegotiationById(Integer id) {
        Negotiation negotiation = negotiationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negotiation not found with id: " + id));
        return convertToDTO(negotiation);
    }

    // Get negotiations by status
    public List<NegotiationDTO> getNegotiationsByStatus(String status) {
        return negotiationRepository.findByNegotiationstatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiations by vendor
    public List<NegotiationDTO> getNegotiationsByVendor(Integer vendorid) {
        return negotiationRepository.findByVendorid(vendorid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiations by event
    public List<NegotiationDTO> getNegotiationsByEvent(Integer eventid) {
        return negotiationRepository.findByEventid(eventid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiations by cdsid (UPDATED)
    public List<NegotiationDTO> getNegotiationsByCdsid(String cdsid) {
        return negotiationRepository.findByCdsid(cdsid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiations by year
    public List<NegotiationDTO> getNegotiationsByYear(int year) {
        return negotiationRepository.findByYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiations by date range
    public List<NegotiationDTO> getNegotiationsByDateRange(Date startDate, Date endDate) {
        return negotiationRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get negotiations with savings
    public List<NegotiationDTO> getNegotiationsWithSavings() {
        return negotiationRepository.findNegotiationsWithSavings().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create negotiation
    public NegotiationDTO createNegotiation(NegotiationDTO dto) {
        Negotiation negotiation = new Negotiation();
        mapDTOToEntity(dto, negotiation);
        Negotiation saved = negotiationRepository.save(negotiation);
        return convertToDTO(saved);
    }

    // Create negotiation from Purchase Request (ONLY if PR status is PENDING)
    public NegotiationDTO createNegotiationFromPR(Integer prid) {
        PurchaseRequest pr = purchaseRequestRepository.findById(prid)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + prid));

        // Check if PR status is PENDING
        if (!Status.PENDING.equals(pr.getPrstatus())) {
            throw new RuntimeException(
                    "Can only create negotiation from PENDING purchase requests. Current status: " + pr.getPrstatus());
        }

        // Check if negotiation already exists for this PR
        if (pr.getNegotiation() != null) {
            throw new RuntimeException("Negotiation already exists for this Purchase Request");
        }

        // Create new negotiation with PR data
        Negotiation negotiation = new Negotiation();
        negotiation.setEventid(pr.getEventid());
        negotiation.setEventname(pr.getEventname());
        negotiation.setVendorid(pr.getVendorid());
        negotiation.setVendorname(pr.getVendorname());
       
        negotiation.setCdsid(pr.getCdsid());
        negotiation.setNegotiationdate(new Date());
        negotiation.setInitialquoteamount(pr.getAllocatedamount());
        negotiation.setFinalamount(pr.getAllocatedamount()); // Default to same as initial
        negotiation.setNegotiationstatus("Pending");
        negotiation.setPurchaseRequest(pr);

        Negotiation saved = negotiationRepository.save(negotiation);
        System.out.println("Negotiation created from PR " + prid + " with ID: " + saved.getNegotiationid());

        return convertToDTO(saved);
    }

    // Update negotiation (ONLY allows editing: finalamount, negotiationdate, status, notes)
    public NegotiationDTO updateNegotiation(Integer id, NegotiationDTO dto) {
        System.out.println("=== UPDATE NEGOTIATION STARTED ===");
        System.out.println("Negotiation ID: " + id);
        System.out.println("New Status: " + dto.getNegotiationstatus());

        Negotiation negotiation = negotiationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negotiation not found with id: " + id));

        // Store old status before updating
        String oldStatus = negotiation.getNegotiationstatus();
        System.out.println("Old Status: " + oldStatus);

        // UPDATE ONLY ALLOWED FIELDS (finalamount, negotiationdate, status, notes)
        negotiation.setNegotiationdate(dto.getNegotiationdate());
        negotiation.setFinalamount(dto.getFinalamount());
        negotiation.setNegotiationstatus(dto.getNegotiationstatus());
        negotiation.setNotes(dto.getNotes());
        // DO NOT update: eventid, eventname, vendorid, vendorname, cdsid, initialquoteamount, prid

        Negotiation updated = negotiationRepository.save(negotiation);
        System.out.println("Negotiation saved with new status: " + updated.getNegotiationstatus());

        // Update Purchase Request status based on Negotiation status
        if (negotiation.getPurchaseRequest() != null) {
            PurchaseRequest pr = negotiation.getPurchaseRequest();

            // If Negotiation status changed to "Completed" → PR becomes "APPROVED" + Create PO
            if ("Completed".equals(dto.getNegotiationstatus()) &&
                    !"Completed".equals(oldStatus)) {

                System.out.println("=== UPDATING PURCHASE REQUEST TO APPROVED ===");
                System.out.println("Purchase Request ID: " + pr.getPrid());
                System.out.println("Current PR Status: " + pr.getPrstatus());

                pr.setPrstatus(Status.APPROVED);
                purchaseRequestRepository.save(pr);

                System.out.println("Purchase Request " + pr.getPrid() + " status updated to APPROVED");

                // AUTO-CREATE PURCHASE ORDER
                System.out.println("=== AUTO-CREATING PURCHASE ORDER ===");

                // Check if PO already exists for this negotiation
                if (negotiation.getPurchaseOrder() == null) {
                    PurchaseOrder po = new PurchaseOrder();
                    po.setEventid(negotiation.getEventid());
                    po.setEventname(negotiation.getEventname());
                    po.setVendorid(negotiation.getVendorid());
                    po.setVendorname(negotiation.getVendorname());
                    
                    po.setCdsid(negotiation.getCdsid());
                    po.setOrderdate(new Date());
                    po.setOrderamountINR(negotiation.getFinalamount());
                    po.setOrderamountdollar(negotiation.getFinalamount() / 83.0); // INR to USD conversion
                    po.setPO_status("PENDING");
                    po.setpurchaserequest(pr);
                    po.setnegotiation(updated);

                    PurchaseOrder savedPO = purchaseOrderRepository.save(po);
                    System.out.println("Purchase Order created with ID: " + savedPO.getPO_id());
                } else {
                    System.out.println("Purchase Order already exists for this Negotiation");
                }
            }

            // If Negotiation status changed to "Cancelled" → PR becomes "REJECTED"
            else if ("Cancelled".equals(dto.getNegotiationstatus()) &&
                    !"Cancelled".equals(oldStatus)) {

                System.out.println("=== UPDATING PURCHASE REQUEST TO REJECTED ===");
                System.out.println("Purchase Request ID: " + pr.getPrid());
                System.out.println("Current PR Status: " + pr.getPrstatus());

                pr.setPrstatus(Status.REJECTED);
                purchaseRequestRepository.save(pr);

                System.out.println("Purchase Request " + pr.getPrid() + " status updated to REJECTED");
            }
        }

        System.out.println("=== UPDATE NEGOTIATION ENDED ===");
        return convertToDTO(updated);
    }

    // Delete negotiation
    public void deleteNegotiation(Integer id) {
        if (!negotiationRepository.existsById(id)) {
            throw new RuntimeException("Negotiation not found with id: " + id);
        }
        negotiationRepository.deleteById(id);
    }

    // Convert entity to DTO
    private NegotiationDTO convertToDTO(Negotiation negotiation) {
        NegotiationDTO dto = new NegotiationDTO();
        dto.setNegotiationid(negotiation.getNegotiationid());
        dto.setEventid(negotiation.getEventid());
        dto.setEventname(negotiation.getEventname());
        dto.setVendorid(negotiation.getVendorid());
        dto.setVendorname(negotiation.getVendorname());
       
        dto.setCdsid(negotiation.getCdsid());
        dto.setNegotiationdate(negotiation.getNegotiationdate());
        dto.setInitialquoteamount(negotiation.getInitialquoteamount());
        dto.setFinalamount(negotiation.getFinalamount());
        dto.setNegotiationstatus(negotiation.getNegotiationstatus());
        dto.setNotes(negotiation.getNotes());
        if (negotiation.getPurchaseRequest() != null) {
            dto.setPrid(negotiation.getPurchaseRequest().getPrid());
        }
        return dto;
    }

    // Map DTO to entity
    private void mapDTOToEntity(NegotiationDTO dto, Negotiation negotiation) {
        negotiation.setEventid(dto.getEventid());
        negotiation.setEventname(dto.getEventname());
        negotiation.setVendorid(dto.getVendorid());
        negotiation.setVendorname(dto.getVendorname());
       
        negotiation.setCdsid(dto.getCdsid());
        negotiation.setNegotiationdate(dto.getNegotiationdate());
        negotiation.setInitialquoteamount(dto.getInitialquoteamount());
        negotiation.setFinalamount(dto.getFinalamount());
        negotiation.setNegotiationstatus(dto.getNegotiationstatus());
        negotiation.setNotes(dto.getNotes());

        if (dto.getPrid() != null) {
            PurchaseRequest pr = purchaseRequestRepository.findById(dto.getPrid())
                    .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + dto.getPrid()));
            negotiation.setPurchaseRequest(pr);
        }
    }
}