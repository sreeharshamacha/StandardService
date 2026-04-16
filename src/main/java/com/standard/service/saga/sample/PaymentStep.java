package com.standard.service.saga.sample;

import com.standard.service.saga.core.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentStep implements SagaStep<OrderSagaContext> {

    @Override
    public boolean process(OrderSagaContext context) {
        log.info("Processing Payment for Order {}. Amount: {}", context.getOrderId(), context.getAmount());

        if (context.isForceFailForDemo()) {
            log.error("Payment failed intentionally for demo purposes.");
            return false;
        }

        // Simulate payment success
        context.setPaymentProcessed(true);
        return true;
    }

    @Override
    public boolean compensate(OrderSagaContext context) {
        if (context.isPaymentProcessed()) {
            log.info("Compensating Payment for Order {}. Reverting amount: {}", context.getOrderId(),
                    context.getAmount());
            context.setPaymentProcessed(false);
        }
        return true;
    }
}
