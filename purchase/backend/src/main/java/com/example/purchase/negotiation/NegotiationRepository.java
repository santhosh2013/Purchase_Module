package com.example.purchase.negotiation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Integer> {

    // Find by status
    List<Negotiation> findByNegotiationstatus(String status);

    // Find by vendor id
    List<Negotiation> findByVendorid(Integer vendorid);

    // Find by event id
    List<Negotiation> findByEventid(Integer eventid);

    // Find by cdsid (UPDATED)
    List<Negotiation> findByCdsid(String cdsid);

    // Find by purchase request id
    Optional<Negotiation> findByPurchaseRequest_Prid(Integer prid);

    // Find by year
    @Query("SELECT n FROM Negotiation n WHERE YEAR(n.negotiationdate) = :year")
    List<Negotiation> findByYear(@Param("year") int year);

    // Find by date range
    @Query("SELECT n FROM Negotiation n WHERE n.negotiationdate BETWEEN :startDate AND :endDate")
    List<Negotiation> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Find negotiations with savings (final amount < initial quote)
    @Query("SELECT n FROM Negotiation n WHERE n.finalamount < n.initialquoteamount")
    List<Negotiation> findNegotiationsWithSavings();
}
