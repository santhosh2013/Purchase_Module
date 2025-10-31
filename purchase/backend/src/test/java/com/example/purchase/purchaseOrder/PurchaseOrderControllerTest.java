package com.example.purchase.purchaseOrder;


import com.example.purchase.purchaseorder.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderControllerTest {

    @Mock
    private  PurchaseOrderService purchaseOrderService;

    @InjectMocks
    private PurchaseOrderController purchaseOrderController;

    private PurchaseOrderDTO mockPurchaseOrderDTO;
    private List<PurchaseOrderDTO> mockPurchaseOrderList;
    @BeforeEach
    public void setup() {
        mockPurchaseOrderDTO = new PurchaseOrderDTO();

        mockPurchaseOrderDTO.setPO_id(1);
        mockPurchaseOrderDTO.setPO_status("PENDING");
        mockPurchaseOrderDTO.setCdsid("AK123");
        mockPurchaseOrderDTO.setEventid(121);
        mockPurchaseOrderDTO.setEventname("Java Fullstack");
        mockPurchaseOrderDTO.setOrderdate(new Date(2025-4-03));
        mockPurchaseOrderDTO.setPrid(1);
        mockPurchaseOrderList = new ArrayList<>();
        mockPurchaseOrderList.add(mockPurchaseOrderDTO);

    }

    @Test
    void createPurchaseOrderTest() throws InvalidInputException {
        when(purchaseOrderService.createPurchaseOrder(any(PurchaseOrderDTO.class))).thenReturn(mockPurchaseOrderDTO);

        ResponseEntity<PurchaseOrderDTO> response= purchaseOrderController.createPurchaseOrder(mockPurchaseOrderDTO);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockPurchaseOrderDTO, response.getBody());
    }

    @Test
    void updateStatusTest() throws InvalidInputException {
        when(purchaseOrderService.updatePurchaseOrder(any(Integer.class),any(PurchaseOrderDTO.class))).thenReturn(mockPurchaseOrderDTO);

        ResponseEntity<PurchaseOrderDTO> response=purchaseOrderController.updateStatus(1,mockPurchaseOrderDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPurchaseOrderDTO, response.getBody());
    }

    @Test
    void rejectStatusTest() throws InvalidInputException {
        PurchaseOrderDTO newDTO=new PurchaseOrderDTO();
        newDTO.setPO_id(1);
        newDTO.setPO_status("REJECTED");
        newDTO.setCdsid("AK123");
        newDTO.setEventid(121);
        newDTO.setEventname("Java Fullstack");
        newDTO.setOrderdate(new Date(2025-4-03));
        newDTO.setPrid(1);
        newDTO.setVendorname("Ak");
        when(purchaseOrderService.rejectPurchaseOrder(1)).thenReturn(newDTO);

        ResponseEntity<PurchaseOrderDTO> response=purchaseOrderController.rejectStatus(1);
        assertEquals("REJECTED", response.getBody().getPO_status());
    }

    @Test
    void getPurchaseOrdersByCdsTest() throws InvalidInputException {
        when(purchaseOrderService.getPurchaseOrdersByCdsid("Ak123")).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response = purchaseOrderController.getPurchaseOrdersByCds("Ak123");

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());
    }

    @Test
    void getPurchaseOrdersByEvent() throws InvalidInputException {
        when(purchaseOrderService.getPurchaseOrdersByEvent(121)).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response = purchaseOrderController.getPurchaseOrdersByEvent(121);

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());
    }
    @Test
    void getPurchaseOrdersByEventNameTest() throws InvalidInputException {
        when(purchaseOrderService.getPurchaseOrdersByEvent(1)).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response = purchaseOrderController.getPurchaseOrdersByEvent(1);

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());
    }
    @Test
    void getPurchaseOrdersByYearTest(){
        when(purchaseOrderService.getPurchaseOrdersByYear(2025)).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response = purchaseOrderController.getPurchaseOrdersByYear(2025);

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());

    }

    @Test
    void getPurchaseOrdersByDateRange(){
        when(purchaseOrderService.getPurchaseOrdersByDateRange(any(Date.class), any(Date.class))).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response= purchaseOrderController.getPurchaseOrdersByDateRange(new Date(),new Date());
        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());

    }

    @Test
    void getAllPurchaseOrders(){
        when(purchaseOrderService.getAllPurchaseOrders()).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response= purchaseOrderController.getAllPurchaseOrders();

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(1,response.getBody().size());
    }

    @Test
     void getPurchaseOrderById() throws InvalidInputException {
        when(purchaseOrderService.getPurchaseOrderById(1)).thenReturn(mockPurchaseOrderDTO);

        ResponseEntity<PurchaseOrderDTO> response = purchaseOrderController.getPurchaseOrderById(1);

        assertEquals("PENDING", response.getBody().getPO_status());
        assertEquals(mockPurchaseOrderDTO, response.getBody());
    }
    @Test
    void getPurchaseOrdersByStatus() throws InvalidStatusException {
        when(purchaseOrderService.getPurchaseOrdersByStatus("PENDING")).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response = purchaseOrderController.getPurchaseOrdersByStatus("PENDING");

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());
    }

    @Test
    void getPurchaseOrdersByVendor() throws InvalidInputException {
        when(purchaseOrderService.getPurchaseOrdersByVendor(1)).thenReturn(mockPurchaseOrderList);

        ResponseEntity<List<PurchaseOrderDTO>> response = purchaseOrderController.getPurchaseOrdersByVendor(1);

        assertEquals("PENDING", response.getBody().get(0).getPO_status());
        assertEquals(mockPurchaseOrderList, response.getBody());

    }
    @Test
  void completePurchaseOrder() throws InvalidInputException {
        PurchaseOrderDTO completedDTO=new PurchaseOrderDTO();
        completedDTO.setPO_id(1);
        completedDTO.setPO_status("COMPLETED");
        completedDTO.setCdsid("AK123");
        completedDTO.setEventid(121);
        completedDTO.setEventname("Java Fullstack");
        completedDTO.setOrderdate(new Date(2025-4-03));
        completedDTO.setPrid(1);
        when(purchaseOrderService.completePurchaseOrder(1)).thenReturn(completedDTO);

        ResponseEntity<PurchaseOrderDTO> response = purchaseOrderController.completePurchaseOrder(1);

        assertEquals("COMPLETED", response.getBody().getPO_status());
        assertEquals(completedDTO, response.getBody());
        assertEquals("Java Fullstack", response.getBody().getEventname());
    }

    @Test
    void deletePurchaseOrder(){
        ResponseEntity<Void> response = purchaseOrderController.deletePurchaseOrder(1);
        assertEquals(204, response.getStatusCodeValue());
    }





}