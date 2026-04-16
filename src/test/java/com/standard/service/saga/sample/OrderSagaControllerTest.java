package com.standard.service.saga.sample;

import com.standard.service.saga.core.SagaOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class OrderSagaControllerTest {

    private OrderSagaController controller;

    @BeforeEach
    void setUp() {
        SagaOrchestrator orchestrator = new SagaOrchestrator();
        InventoryStep inventoryStep = new InventoryStep();
        PaymentStep paymentStep = new PaymentStep();
        controller = new OrderSagaController(orchestrator, inventoryStep, paymentStep);
    }

    @Test
    void testSuccessfulOrder() {
        ResponseEntity<String> response = controller.placeOrder("PROD-1", 10, 100.0, false);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("successfully"));
    }

    @Test
    void testFailedPaymentWithCompensation() {
        ResponseEntity<String> response = controller.placeOrder("PROD-1", 10, 100.0, true);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Order failed:"));
        assertTrue(response.getBody().contains("Compensating transactions applied"));
    }
}
