package com.standard.service.saga.sample;

import com.standard.service.utils.MdcUtils;
import com.standard.service.saga.core.SagaOrchestrator;
import com.standard.service.saga.core.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.standard.service.utils.MdcUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/sagas/orders")
@RequiredArgsConstructor
public class OrderSagaController {

    private final SagaOrchestrator sagaOrchestrator;
    private final InventoryStep inventoryStep;
    private final PaymentStep paymentStep;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestParam String productId, @RequestParam int quantity,
            @RequestParam double amount, @RequestParam(defaultValue = "false") boolean forcePaymentFailure) {

        OrderSagaContext context = new OrderSagaContext();
        String traceId = MdcUtils.getTraceId();
        context.setCorrelationId(
                traceId != null ? traceId : UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        context.setOrderId(UUID.randomUUID().toString());
        context.setProductId(productId);
        context.setQuantity(quantity);
        context.setAmount(amount);
        context.setForceFailForDemo(forcePaymentFailure);

        List<SagaStep<OrderSagaContext>> steps = Arrays.asList(inventoryStep, paymentStep);

        log.info("Initiating Order Saga. Correlation ID: {}, Order ID: {}", context.getCorrelationId(),
                context.getOrderId());
        SagaOrchestrator.SagaResult result = sagaOrchestrator.execute(context, steps);

        if (result.isSuccess()) {
            return ResponseEntity.ok("Order processed successfully. Order ID: " + context.getOrderId());
        } else {
            return ResponseEntity.badRequest()
                    .body("Order failed: " + result.getErrorMessage() + " (Compensating transactions applied)");
        }
    }
}
