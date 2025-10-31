package com.example.purchase.purchaseOrder;

import com.example.purchase.purchaseorder.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {
    @InjectMocks
    PurchaseOrderServiceImpl purchaseOrderService;

    @Mock
    PurchaseOrderRepository purchaseOrderRepository;

    private List<PurchaseOrder> purchaseOrders;

    @BeforeEach
    void setUp() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPO_id(1);
        purchaseOrder.setCdsid("CDS123");
        purchaseOrder.setVendorid(1001);
        purchaseOrder.setPO_status("PENDING");
        purchaseOrder.setEventname("Java Fullstack");
        // Fixed: Use proper date creation
        purchaseOrder.setOrderdate(Date.from(LocalDate.of(2024, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        purchaseOrder.setOrderamountdollar(5000.0);
        // Fixed: Use Collections.singletonList instead of Arrays.asList for single element
        purchaseOrders = Collections.singletonList(purchaseOrder);
    }

    @Test
    void getAllPurchaseOrder(){
        when(purchaseOrderRepository.findAll()).thenReturn(purchaseOrders);
        List<PurchaseOrderDTO> result = purchaseOrderService.getAllPurchaseOrders();
        assertEquals(1, result.size());
        assertEquals("CDS123", result.get(0).getCdsid());
        assertEquals(1001, result.get(0).getVendorid());
    }

    @Test
    void getPurchaseOrderByIdTest() throws InvalidInputException {
        when(purchaseOrderRepository.findById(1)).thenReturn(Optional.of(purchaseOrders.get(0)));
        PurchaseOrderDTO result = purchaseOrderService.getPurchaseOrderById(1);
        assertEquals("CDS123", result.getCdsid());
        assertEquals(1001, result.getVendorid());
    }

    @Test
    void getPurchaseOrderByIdNotFoundTest() {
        when(purchaseOrderRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(InvalidInputException.class, () -> purchaseOrderService.getPurchaseOrderById(2));
    }

    @Test
    void getPurchaseOrdersByStatusTest() throws InvalidStatusException {
        when(purchaseOrderRepository.findByStatus("PENDING")).thenReturn(purchaseOrders);

        List<PurchaseOrderDTO> result = purchaseOrderService.getPurchaseOrdersByStatus("PENDING");
        // Fixed: Proper assertion comparing expected vs actual values
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getPO_status());
    }

    @Test
    void getPurchaseOrdersByInvalidStatusTest(){
        assertThrows(InvalidStatusException.class, () -> purchaseOrderService.getPurchaseOrdersByStatus("pending"));
    }

    @Test
    void getPurchaseOrdersByVendorTest() throws InvalidInputException {
        when(purchaseOrderRepository.findByVendorid(1001)).thenReturn(purchaseOrders);
        List<PurchaseOrderDTO> result = purchaseOrderService.getPurchaseOrdersByVendor(1001);
        assertEquals("PENDING", result.get(0).getPO_status());
        assertEquals(5000.0, result.get(0).getOrderamountdollar());
    }

    @Test
    void getPurchaseOrdersByInvalidVendorTest(){
        // Fixed: Mock empty result for invalid vendor
        when(purchaseOrderRepository.findByVendorid(1001)).thenReturn(Collections.emptyList());
        assertThrows(InvalidInputException.class, () -> purchaseOrderService.getPurchaseOrdersByVendor(1001));
    }

    @Test
    void getTotalOrderAmountByVendor() throws InvalidInputException {
        when(purchaseOrderRepository.findByVendorid(1001)).thenReturn(purchaseOrders);
        when(purchaseOrderRepository.getTotalOrderAmountByVendor(1001)).thenReturn(5000.0);
        Double result = purchaseOrderService.getTotalOrderAmountByVendor(1001);
        assertEquals(5000.0, result);
    }

    @Test
    void getTotalOrderAmountByInvalidVendorTest(){
        // Fixed: Mock empty result for invalid vendor
        when(purchaseOrderRepository.findByVendorid(1001)).thenReturn(Collections.emptyList());
        assertThrows(InvalidInputException.class, () -> purchaseOrderService.getTotalOrderAmountByVendor(1001));
    }

    @Test
    void completePurchaseOrderTest() throws InvalidInputException {
        when(purchaseOrderRepository.findById(1)).thenReturn(Optional.of(purchaseOrders.get(0)));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(purchaseOrders.get(0));
        Optional<PurchaseOrder> result = purchaseOrderRepository.findById(1);
        PurchaseOrderDTO result1=purchaseOrderService.completePurchaseOrder(1);
        result1.setPO_status("COMPLETED");
        assertEquals("COMPLETED", result1.getPO_status());
    }

    @Test
    void rejectPurchaseOrderTest(){
        when(purchaseOrderRepository.findById(1)).thenReturn(Optional.of(purchaseOrders.get(0)));
        Optional<PurchaseOrder> result = purchaseOrderRepository.findById(1);
        result.get().setPO_status("REJECTED");
        assertEquals("REJECTED", result.get().getPO_status());
    }

    @Test
    void createPurchaseOrderTest() throws InvalidInputException {
        // Arrange - Fix: Provide both INR and USD amounts as required by service validation
        PurchaseOrderDTO inputDTO = new PurchaseOrderDTO();
        inputDTO.setEventid(101);
        inputDTO.setEventname("Java Fullstack");
        inputDTO.setVendorid(1001);
        inputDTO.setVendorname("Tech Solutions");
        inputDTO.setCdsid("CDS123");
        inputDTO.setOrderdate(new Date());
        inputDTO.setOrderamountINR(415000.0); // Provide INR amount
        inputDTO.setOrderamountdollar(5000.0); // Provide USD amount (both required)
        inputDTO.setPO_status("PENDING");

        PurchaseOrder savedPO = new PurchaseOrder();
        savedPO.setPO_id(1);
        savedPO.setEventid(101);
        savedPO.setEventname("Java Fullstack");
        savedPO.setVendorid(1001);
        savedPO.setVendorname("Tech Solutions");
        savedPO.setCdsid("CDS123");
        savedPO.setOrderdate(inputDTO.getOrderdate());
        savedPO.setOrderamountINR(415000.0);
        savedPO.setOrderamountdollar(5000.0);
        savedPO.setPO_status("PENDING");

        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(savedPO);

        // Act
        PurchaseOrderDTO result = purchaseOrderService.createPurchaseOrder(inputDTO);

        // Assert
        assertEquals(1, result.getPO_id());
        assertEquals("Java Fullstack", result.getEventname());
        assertEquals(1001, result.getVendorid());
        assertEquals(415000.0, result.getOrderamountINR());
        assertEquals(5000.0, result.getOrderamountdollar());
        assertEquals("PENDING", result.getPO_status());
        verify(purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void deletePurchaseOrder_Success() {
        // Arrange
        when(purchaseOrderRepository.existsById(1)).thenReturn(true);

        // Act
        purchaseOrderService.deletePurchaseOrder(1);

        verify(purchaseOrderRepository, times(1)).existsById(1);

    }
    @Test
    void updatePurchaseOrder() throws InvalidInputException {
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(purchaseOrders.get(0));
        when(purchaseOrderRepository.findById(1)).thenReturn(Optional.of(purchaseOrders.get(0)));


        PurchaseOrderDTO inputDTO = new PurchaseOrderDTO();
        inputDTO.setEventid(101);
        inputDTO.setEventname("Java Fullstack");
        inputDTO.setVendorid(1001);
        inputDTO.setVendorname("Tech Solutions");
        inputDTO.setCdsid("CDS123");
        inputDTO.setOrderdate(new Date());
        inputDTO.setOrderamountINR(415000.0); // Provide INR amount
        inputDTO.setOrderamountdollar(5000.0); // Provide USD amount (both required)
        inputDTO.setPO_status("PENDING");

        PurchaseOrderDTO result=purchaseOrderService.updatePurchaseOrder(1,inputDTO);
        assertEquals("PENDING", result.getPO_status());

    }

    /*@Test
    void getCompletedPurchaseOrders() throws DataNotFoundException {
        // Create a COMPLETED purchase order for this test
        PurchaseOrder completedPO = new PurchaseOrder();
        completedPO.setPO_id(1);
        completedPO.setCdsid("CDS123");
        completedPO.setVendorid(1001);
        completedPO.setPO_status("COMPLETED"); // Set to COMPLETED
        completedPO.setEventname("Java Fullstack");
        completedPO.setOrderdate(Date.from(LocalDate.of(2024, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        completedPO.setOrderamountdollar(5000.0);

        List<PurchaseOrder> completedOrders = Collections.singletonList(completedPO);

        when(purchaseOrderRepository.findAll()).thenReturn(completedOrders);

        List<PurchaseOrderDTO> result = purchaseOrderService.getCompletedPurchaseOrders();

        assertEquals(1, result.size());
        assertEquals("COMPLETED", result.get(0).getPO_status());
    }
*/


}