package com.standard.service.saga.sample;

import lombok.Data;

@Data
public class OrderSagaContext {
    private String correlationId;
    private String orderId;
    private String productId;
    private int quantity;
    private double amount;
    private boolean inventoryUpdated;
    private boolean paymentProcessed;
    private boolean forceFailForDemo; // Used for sample demonstration implementation
}
