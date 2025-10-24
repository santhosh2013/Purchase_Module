package com.example.purchase.purchaseorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {

    @Query("SELECT po FROM PurchaseOrder po WHERE po.PO_status = :status")
    List<PurchaseOrder> findByStatus(@Param("status") String status);

    List<PurchaseOrder> findByVendorid(Integer vendorid);

    List<PurchaseOrder> findByEventid(Integer eventid);

    // UPDATED: Find by cdsid
    List<PurchaseOrder> findByCdsid(String cdsid);

    Optional<PurchaseOrder> findByPurchaserequest_Prid(Integer prid);

    Optional<PurchaseOrder> findByNegotiation_Negotiationid(Integer negotiationid);

    @Query("SELECT po FROM PurchaseOrder po WHERE YEAR(po.orderdate) = :year")
    List<PurchaseOrder> findByYear(@Param("year") int year);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.orderdate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.PO_status = 'COMPLETED'")
    List<PurchaseOrder> findCompletedOrders();

    @Query("SELECT po FROM PurchaseOrder po WHERE po.orderamountINR BETWEEN :minAmount AND :maxAmount")
    List<PurchaseOrder> findByAmountRangeINR(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);

    @Query("SELECT SUM(po.orderamountINR) FROM PurchaseOrder po WHERE po.vendorid = :vendorid")
    Double getTotalOrderAmountByVendor(@Param("vendorid") Integer vendorid);
}
