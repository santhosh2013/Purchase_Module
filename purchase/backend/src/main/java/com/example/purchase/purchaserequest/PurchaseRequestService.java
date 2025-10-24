package com.example.purchase.purchaserequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseRequestService {

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    // Get all purchase requests
    public List<PurchaseRequestDTO> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase request by id
    public PurchaseRequestDTO getPurchaseRequestById(Integer id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + id));
        return convertToDTO(pr);
    }

    // Get purchase requests by status
    public List<PurchaseRequestDTO> getPurchaseRequestsByStatus(Status status) {
        return purchaseRequestRepository.findByPrstatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase requests by vendor
    public List<PurchaseRequestDTO> getPurchaseRequestsByVendor(Integer vendorid) {
        return purchaseRequestRepository.findByVendorid(vendorid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase requests by event
    public List<PurchaseRequestDTO> getPurchaseRequestsByEvent(Integer eventid) {
        return purchaseRequestRepository.findByEventid(eventid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase requests by cdsid (UPDATED)
    public List<PurchaseRequestDTO> getPurchaseRequestsByCdsid(String cdsid) {
        return purchaseRequestRepository.findByCdsid(cdsid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase requests by year
    public List<PurchaseRequestDTO> getPurchaseRequestsByYear(int year) {
        return purchaseRequestRepository.findByYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase requests by date range
    public List<PurchaseRequestDTO> getPurchaseRequestsByDateRange(Date startDate, Date endDate) {
        return purchaseRequestRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get pending purchase requests
    public List<PurchaseRequestDTO> getPendingPurchaseRequests() {
        return purchaseRequestRepository.findPendingRequests().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get approved purchase requests
    public List<PurchaseRequestDTO> getApprovedPurchaseRequests() {
        return purchaseRequestRepository.findApprovedRequests().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Check if event ID already exists
    private void validateEventIdUniqueness(Integer eventid) {
        List<PurchaseRequest> existingEvents = purchaseRequestRepository.findAll().stream()
                .filter(pr -> pr.getEventid().equals(eventid))
                .collect(Collectors.toList());

        if (!existingEvents.isEmpty()) {
            throw new RuntimeException("Event ID already exists. Please use a different Event ID.");
        }
    }

    public PurchaseRequestDTO createPurchaseRequest(PurchaseRequestDTO dto) {
        // Validate event ID uniqueness
        validateEventIdUniqueness(dto.getEventid());

        PurchaseRequest pr = new PurchaseRequest();
        mapDTOToEntity(dto, pr);
        PurchaseRequest saved = purchaseRequestRepository.save(pr);
        return convertToDTO(saved);
    }

    // Update purchase request
    public PurchaseRequestDTO updatePurchaseRequest(Integer id, PurchaseRequestDTO dto) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + id));
        mapDTOToEntity(dto, pr);
        PurchaseRequest updated = purchaseRequestRepository.save(pr);
        return convertToDTO(updated);
    }

    // Approve purchase request
    public PurchaseRequestDTO approvePurchaseRequest(Integer id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + id));
        pr.setPrstatus(Status.APPROVED);
        PurchaseRequest updated = purchaseRequestRepository.save(pr);
        return convertToDTO(updated);
    }

    // Reject purchase request
    public PurchaseRequestDTO rejectPurchaseRequest(Integer id) {
        PurchaseRequest pr = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + id));
        pr.setPrstatus(Status.REJECTED);
        PurchaseRequest updated = purchaseRequestRepository.save(pr);
        return convertToDTO(updated);
    }

    // Delete purchase request
    public void deletePurchaseRequest(Integer id) {
        if (!purchaseRequestRepository.existsById(id)) {
            throw new RuntimeException("Purchase Request not found with id: " + id);
        }
        purchaseRequestRepository.deleteById(id);
    }

    // Convert entity to DTO
    private PurchaseRequestDTO convertToDTO(PurchaseRequest pr) {
        PurchaseRequestDTO dto = new PurchaseRequestDTO();
        dto.setPrid(pr.getPrid());
        dto.setEventid(pr.getEventid());
        dto.setEventname(pr.getEventname());
        dto.setVendorid(pr.getVendorid());
        dto.setVendorname(pr.getVendorname());

        dto.setCdsid(pr.getCdsid());
        dto.setRequestdate(pr.getRequestdate());
        dto.setAllocatedamount(pr.getAllocatedamount());
        dto.setPrstatus(pr.getPrstatus() != null ? pr.getPrstatus().toString() : null);
        return dto;
    }

    // Map DTO to entity
    private void mapDTOToEntity(PurchaseRequestDTO dto, PurchaseRequest pr) {
        pr.setEventid(dto.getEventid());
        pr.setEventname(dto.getEventname());
        pr.setVendorid(dto.getVendorid());
        pr.setVendorname(dto.getVendorname());

        pr.setCdsid(dto.getCdsid());
        pr.setRequestdate(dto.getRequestdate());
        pr.setAllocatedamount(dto.getAllocatedamount());
        if (dto.getPrstatus() != null) {
            pr.setPrstatus(Status.valueOf(dto.getPrstatus()));
        }
    }
}
