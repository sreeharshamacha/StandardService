package com.standard.service.saga.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Slf4j
@Component
public class SagaOrchestrator {

    public <T> SagaResult execute(T context, List<SagaStep<T>> steps) {
        log.info("Starting saga execution with {} steps", steps.size());
        List<SagaStep<T>> completedSteps = new ArrayList<>();

        for (SagaStep<T> step : steps) {
            log.info("Executing step: {}", step.getName());
            try {
                boolean success = step.process(context);
                if (success) {
                    completedSteps.add(step);
                } else {
                    log.error("Step {} reported failure without throwing exception", step.getName());
                    rollback(context, completedSteps);
                    return SagaResult.failure("Step failed: " + step.getName());
                }
            } catch (Exception e) {
                log.error("Exception in step {}: {}", step.getName(), e.getMessage());
                rollback(context, completedSteps);
                return SagaResult.failure("Exception in step " + step.getName() + ": " + e.getMessage());
            }
        }

        log.info("Saga executed successfully completed all {} steps", steps.size());
        return SagaResult.success();
    }

    private <T> void rollback(T context, List<SagaStep<T>> completedSteps) {
        log.info("Starting compensation for {} completed steps", completedSteps.size());
        ListIterator<SagaStep<T>> iterator = completedSteps.listIterator(completedSteps.size());

        while (iterator.hasPrevious()) {
            SagaStep<T> step = iterator.previous();
            log.info("Compensating step: {}", step.getName());
            try {
                boolean compSuccess = step.compensate(context);
                if (!compSuccess) {
                    log.error("Compensation failed for step: {}. Manual intervention may be required.", step.getName());
                }
            } catch (Exception e) {
                log.error("Exception during compensation in step: {}. Err: {}", step.getName(), e.getMessage());
                // In a production system, failed compensations should trigger alerts and
                // perhaps mark the saga for manual review
            }
        }
        log.info("Compensation phase completed");
    }

    public static class SagaResult {
        private final boolean success;
        private final String errorMessage;

        private SagaResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public static SagaResult success() {
            return new SagaResult(true, null);
        }

        public static SagaResult failure(String errorMessage) {
            return new SagaResult(false, errorMessage);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
