package com.example.purchase.purchaseorder;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseOrderDTO> getAllPurchaseOrders() ;
    PurchaseOrderDTO getPurchaseOrderById(Integer id) throws InvalidInputException;
    List<PurchaseOrderDTO> getPurchaseOrdersByStatus(String status)throws InvalidStatusException;
    List<PurchaseOrderDTO> getPurchaseOrdersByVendor(Integer vendorid)throws InvalidInputException;
    Double getTotalOrderAmountByVendor(Integer vendorid)throws InvalidInputException;
    List<PurchaseOrderDTO>getCompletedPurchaseOrders() throws DataNotFoundException;

    List<PurchaseOrderDTO> getPurchaseOrdersByEvent(Integer eventid);
    List<PurchaseOrderDTO> getPurchaseOrdersByCdsid(String cdsid);
    List<PurchaseOrderDTO> getPurchaseOrdersByYear(Integer year);
    List<PurchaseOrderDTO> getPurchaseOrdersByDateRange(Date fromDate, Date toDate);
    PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO dto) ;
    PurchaseOrderDTO updatePurchaseOrder(Integer id,PurchaseOrderDTO dto) ;
    PurchaseOrderDTO completePurchaseOrder(Integer id);
    PurchaseOrderDTO rejectPurchaseOrder(Integer id) ;
    void deletePurchaseOrder(Integer id);


}
