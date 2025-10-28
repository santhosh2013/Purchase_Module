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
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private NegotiationRepository negotiationRepository;


    // Currency conversion rate (example: 1 USD = 83 INR)
    private static final double USD_TO_INR_RATE = 83.0;

    // Get purchase orders by event
    public List<PurchaseOrderDTO> getPurchaseOrdersByEvent(Integer eventid) {
        return purchaseOrderRepository.findByEventid(eventid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by cdsid (UPDATED)
    public List<PurchaseOrderDTO> getPurchaseOrdersByCdsid(String cdsid) {
        return purchaseOrderRepository.findByCdsid(cdsid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase orders by year
    public List<PurchaseOrderDTO> getPurchaseOrdersByYear(Integer year) {
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
    public List<PurchaseOrderDTO> getCompletedPurchaseOrders() throws DataNotFoundException {
        if(purchaseOrderRepository.findCompletedOrders().isEmpty()){
            throw new DataNotFoundException("No completed Purchase Orders found.");
        }
        return purchaseOrderRepository.findCompletedOrders().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

    // Update purchase order
    public PurchaseOrderDTO updatePurchaseOrder(Integer id, PurchaseOrderDTO dto) {


        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        mapDTOToEntity(dto, po);

        // Auto-calculate currency conversion if one amount is provided
        if (dto.getOrderamountINR() != null && dto.getOrderamountdollar() == null) {
            po.setOrderamountdollar(dto.getOrderamountINR() / USD_TO_INR_RATE);
        } else if (dto.getOrderamountdollar() != null && dto.getOrderamountINR() == null) {
            po.setOrderamountINR(dto.getOrderamountdollar() * USD_TO_INR_RATE);
        }

        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return convertToDTO(updated);
    }



    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get purchase order by id
    public PurchaseOrderDTO getPurchaseOrderById(Integer id)throws InvalidInputException {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Purchase Order not found with id: " + id));
        return convertToDTO(po);
    }

    // Get purchase orders by status
    public List<PurchaseOrderDTO> getPurchaseOrdersByStatus(String status)throws InvalidStatusException {
        if(status.equals("COMPLETED") || status.equals("REJECTED") || status.equals("PENDING")) {
            return purchaseOrderRepository.findByStatus(status).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        throw new InvalidStatusException("Invalid Status");
    }

    // Get purchase orders by vendorgetTotalOrderAmountByVendor
    public List<PurchaseOrderDTO> getPurchaseOrdersByVendor(Integer vendorid) throws InvalidInputException{
        if(purchaseOrderRepository.findByVendorid(vendorid).isEmpty()){
            throw new InvalidInputException("Purchas Order not found with VendorId: \" + vendorid)");
        }
        return purchaseOrderRepository.findByVendorid(vendorid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get total order amount by vendor
    public Double getTotalOrderAmountByVendor(Integer vendorid)throws InvalidInputException {
        if(purchaseOrderRepository.findByVendorid(vendorid).isEmpty()){
            throw new InvalidInputException("Purchase Order not found with VendorId: " + vendorid);
        }
        Double total = purchaseOrderRepository.getTotalOrderAmountByVendor(vendorid);
        return total != null ? total : 0.0;
    }

    // Complete purchase order
    public PurchaseOrderDTO completePurchaseOrder(Integer id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        po.setPO_status("COMPLETED");
        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return convertToDTO(updated);
    }

    // Reject purchase order
    public PurchaseOrderDTO rejectPurchaseOrder(Integer id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id: " + id));
        po.setPO_status("REJECTED");
        PurchaseOrder updated = purchaseOrderRepository.save(po);
        return convertToDTO(updated);
    }

    // Delete purchase order
    public void deletePurchaseOrder(Integer id) {
        if (!purchaseOrderRepository.existsById(id)) {
            throw new RuntimeException("Purchase Order not found with id: " + id);
        }
        purchaseOrderRepository.deleteById(id);
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
