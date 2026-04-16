package com.standard.service.saga.sample;

import com.standard.service.saga.core.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryStep implements SagaStep<OrderSagaContext> {

    @Override
    public boolean process(OrderSagaContext context) {
        log.info("Processing Inventory for Order {}. Product: {}, Quantity: {}", context.getOrderId(),
                context.getProductId(), context.getQuantity());

        // Simulate inventory update logic
        context.setInventoryUpdated(true);
        return true;
    }

    @Override
    public boolean compensate(OrderSagaContext context) {
        if (context.isInventoryUpdated()) {
            log.info("Compensating Inventory for Order {}. Restoring {} units of Product {}", context.getOrderId(),
                    context.getQuantity(), context.getProductId());
            context.setInventoryUpdated(false);
        }
        return true;
    }
}
