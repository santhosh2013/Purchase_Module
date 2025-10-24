package com.example.purchase.purchaserequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Integer> {

    // Find by status
    List<PurchaseRequest> findByPrstatus(Status status);

    // Find by vendor id
    List<PurchaseRequest> findByVendorid(Integer vendorid);

    // Find by event id
    List<PurchaseRequest> findByEventid(Integer eventid);

    // Find by cdsid (UPDATED)
    List<PurchaseRequest> findByCdsid(String cdsid);


    // Find by year
    @Query("SELECT pr FROM PurchaseRequest pr WHERE YEAR(pr.requestdate) = :year")
    List<PurchaseRequest> findByYear(@Param("year") int year);

    // Find by date range
    @Query("SELECT pr FROM PurchaseRequest pr WHERE pr.requestdate BETWEEN :startDate AND :endDate")
    List<PurchaseRequest> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Find pending requests
    @Query("SELECT pr FROM PurchaseRequest pr WHERE pr.prstatus = 'PENDING'")
    List<PurchaseRequest> findPendingRequests();

    // Find approved requests
    @Query("SELECT pr FROM PurchaseRequest pr WHERE pr.prstatus = 'APPROVED'")
    List<PurchaseRequest> findApprovedRequests();

    // Find by allocated amount greater than
    List<PurchaseRequest> findByAllocatedamountGreaterThan(Double amount);
}
