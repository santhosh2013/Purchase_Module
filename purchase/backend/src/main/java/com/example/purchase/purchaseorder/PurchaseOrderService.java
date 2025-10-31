package com.example.purchase.purchaseorder;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto) throws InvalidInputException ;
    PurchaseOrderDTO updatePurchaseOrder(Integer id,PurchaseOrderDTO dto) throws InvalidInputException;
    PurchaseOrderDTO rejectPurchaseOrder(Integer id) throws InvalidInputException ;
    List<PurchaseOrderDTO> getPurchaseOrdersByCdsid(String cdsid) throws InvalidInputException;
    List<PurchaseOrderDTO> getPurchaseOrdersByEvent(Integer eventid) throws InvalidInputException;
    List<PurchaseOrderDTO> getPurchaseOrdersByYear(Integer year);
    List<PurchaseOrderDTO> getPurchaseOrdersByDateRange(Date fromDate, Date toDate);
    PurchaseOrderDTO completePurchaseOrder(Integer id)throws InvalidInputException;
    List<PurchaseOrderDTO> getAllPurchaseOrders() ;
    PurchaseOrderDTO getPurchaseOrderById(Integer id) throws InvalidInputException;
    List<PurchaseOrderDTO> getPurchaseOrdersByStatus(String status)throws InvalidStatusException;
    List<PurchaseOrderDTO> getPurchaseOrdersByVendor(Integer vendorid)throws InvalidInputException;
    Double getTotalOrderAmountByVendor(Integer vendorid)throws InvalidInputException;
    List<PurchaseOrderDTO> getCompletedPurchaseOrders() throws DataNotFoundException;
    void deletePurchaseOrder(Integer id);







}
