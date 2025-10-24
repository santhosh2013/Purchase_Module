package com.example.purchase.purchaseorder;

import com.example.purchase.negotiation.Negotiation;
import com.example.purchase.negotiation.NegotiationRepository;
import com.example.purchase.purchaserequest.PurchaseRequest;
import com.example.purchase.purchaserequest.PurchaseRequestRepository;
import com.example.purchase.purchaserequest.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private NegotiationRepository negotiationRepository;

    // Currency conversion rate (example: 1 USD = 83 INR)
    private static final double USD_TO_INR_RATE = 83.0;

    // Get all purchase orders
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase order by id
    public PurchaseOrderDTO getPurchaseOrderById(Integer id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        return convertToDTO(po);
    }

    // Get purchase orders by status
    public List<PurchaseOrderDTO> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by vendor
    public List<PurchaseOrderDTO> getPurchaseOrdersByVendor(Integer vendorid) {
        return purchaseOrderRepository.findByVendorid(vendorid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by event
    public List<PurchaseOrderDTO> getPurchaseOrdersByEvent(Integer eventid) {
        return purchaseOrderRepository.findByEventid(eventid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by cdsid
    public List<PurchaseOrderDTO> getPurchaseOrdersByCdsid(String cdsid) {
        return purchaseOrderRepository.findByCdsid(cdsid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by year
    public List<PurchaseOrderDTO> getPurchaseOrdersByYear(int year) {
        return purchaseOrderRepository.findByYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by date range
    public List<PurchaseOrderDTO> getPurchaseOrdersByDateRange(Date startDate, Date endDate) {
        return purchaseOrderRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get completed purchase orders
    public List<PurchaseOrderDTO> getCompletedPurchaseOrders() {
        return purchaseOrderRepository.findCompletedOrders().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get total order amount by vendor
    public Double getTotalOrderAmountByVendor(Integer vendorid) {
        Double total = purchaseOrderRepository.getTotalOrderAmountByVendor(vendorid);
        return total != null ? total : 0.0;
    }

    // Create purchase order
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto) {
        PurchaseOrder po = new PurchaseOrder();
        mapDTOToEntity(dto, po);

        // Auto-calculate currency conversion if one amount is provided
        if (dto.getOrderamountINR() != null && dto.getOrderamountdollar() == null) {
            po.setOrderamountdollar(dto.getOrderamountINR() / USD_TO_INR_RATE);
        } else if (dto.getOrderamountdollar() != null && dto.getOrderamountINR() == null) {
            po.setOrderamountINR(dto.getOrderamountdollar() * USD_TO_INR_RATE);
        }

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return convertToDTO(saved);
    }

    // Update purchase order with cascade rejection
    public PurchaseOrderDTO updatePurchaseOrder(Integer id, PurchaseOrderDTO dto) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        
        // Store old status to check if it changed to REJECTED
        String oldStatus = po.getPO_status();
        
        mapDTOToEntity(dto, po);

        // Auto-calculate currency conversion if one amount is provided
        if (dto.getOrderamountINR() != null && dto.getOrderamountdollar() == null) {
            po.setOrderamountdollar(dto.getOrderamountINR() / USD_TO_INR_RATE);
        } else if (dto.getOrderamountdollar() != null && dto.getOrderamountINR() == null) {
            po.setOrderamountINR(dto.getOrderamountdollar() * USD_TO_INR_RATE);
        }

        PurchaseOrder updated = purchaseOrderRepository.save(po);
        
        // If status changed to REJECTED, cascade the rejection
        if ("REJECTED".equals(dto.getPO_status()) && !"REJECTED".equals(oldStatus)) {
            cascadeRejection(po);
        }
        
        return convertToDTO(updated);
    }

    // Cascade rejection to PR and Negotiation
    private void cascadeRejection(PurchaseOrder po) {
        // Update Negotiation status to Cancelled
        if (po.getnegotiation() != null) {
            Negotiation negotiation = po.getnegotiation();
            negotiation.setNegotiationstatus("Cancelled");
            negotiationRepository.save(negotiation);
            System.out.println("Negotiation " + negotiation.getNegotiationid() + " status updated to Cancelled");
        }
        
        // Update Purchase Request status to REJECTED
        if (po.getpurchaserequest() != null) {
            PurchaseRequest pr = po.getpurchaserequest();
            pr.setPrstatus(Status.REJECTED);
            purchaseRequestRepository.save(pr);
            System.out.println("Purchase Request " + pr.getPrid() + " status updated to REJECTED");
        }
    }

    // Complete purchase order
    public PurchaseOrderDTO completePurchaseOrder(Integer id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        po.setPO_status("COMPLETED");
        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return convertToDTO(updated);
    }

    // Reject purchase order with cascade
    public PurchaseOrderDTO rejectPurchaseOrder(Integer id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        
        String oldStatus = po.getPO_status();
        po.setPO_status("REJECTED");
        PurchaseOrder updated = purchaseOrderRepository.save(po);
        
        // Cascade rejection if status changed
        if (!"REJECTED".equals(oldStatus)) {
            cascadeRejection(po);
        }
        
        return convertToDTO(updated);
    }

        // Delete purchase order - cascade status update to PR and Negotiation
    public void deletePurchaseOrder(Integer id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        
        // Cascade status updates before deletion
        cascadeRejection(po);
        
        // Now delete the purchase order
        purchaseOrderRepository.deleteById(id);
        System.out.println("Purchase Order " + id + " deleted successfully");
    }


    // Convert entity to DTO
    private PurchaseOrderDTO convertToDTO(PurchaseOrder po) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setPO_id(po.getPO_id());
        dto.setEventid(po.getEventid());
        dto.setEventname(po.getEventname());
        dto.setVendorid(po.getVendorid());
        dto.setVendorname(po.getVendorname());
      
        dto.setCdsid(po.getCdsid());
        dto.setOrderdate(po.getOrderdate());
        dto.setOrderamountINR(po.getOrderamountINR());
        dto.setOrderamountdollar(po.getOrderamountdollar());
        dto.setPO_status(po.getPO_status());
        if (po.getpurchaserequest() != null) {
            dto.setPrid(po.getpurchaserequest().getPrid());
        }
        if (po.getnegotiation() != null) {
            dto.setNegotiationid(po.getnegotiation().getNegotiationid());
        }
        return dto;
    }

    // Map DTO to entity
    private void mapDTOToEntity(PurchaseOrderDTO dto, PurchaseOrder po) {
        po.setEventid(dto.getEventid());
        po.setEventname(dto.getEventname());
        po.setVendorid(dto.getVendorid());
        po.setVendorname(dto.getVendorname());
      
        po.setCdsid(dto.getCdsid());
        po.setOrderdate(dto.getOrderdate());
        po.setOrderamountINR(dto.getOrderamountINR());
        po.setOrderamountdollar(dto.getOrderamountdollar());
        po.setPO_status(dto.getPO_status());

        if (dto.getPrid() != null) {
            PurchaseRequest pr = purchaseRequestRepository.findById(dto.getPrid())
                    .orElseThrow(() -> new RuntimeException("Purchase Request not found with id: " + dto.getPrid()));
            po.setpurchaserequest(pr);
        }

        if (dto.getNegotiationid() != null) {
            Negotiation negotiation = negotiationRepository.findById(dto.getNegotiationid())
                    .orElseThrow(() -> new RuntimeException("Negotiation not found with id: " + dto.getNegotiationid()));
            po.setnegotiation(negotiation);
        }
    }
}
