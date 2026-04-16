package com.standard.service.saga.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SagaOrchestratorTest {

    private SagaOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new SagaOrchestrator();
    }

    @Test
    void execute_AllStepsSucceed() {
        SagaStep<String> step1 = new TestStep(true, true, "Step1");
        SagaStep<String> step2 = new TestStep(true, true, "Step2");

        SagaOrchestrator.SagaResult result = orchestrator.execute("Context", Arrays.asList(step1, step2));

        assertTrue(result.isSuccess());
        assertNull(result.getErrorMessage());
    }

    @Test
    void execute_StepFails_TriggersCompensation() {
        TestStep step1 = new TestStep(true, true, "Step1");
        TestStep step2 = new TestStep(false, true, "Step2");

        SagaOrchestrator.SagaResult result = orchestrator.execute("Context", Arrays.asList(step1, step2));

        assertFalse(result.isSuccess());
        assertEquals("Step failed: Step2", result.getErrorMessage());
        assertTrue(step1.isCompensated()); // Previous step must be compensated
        assertFalse(step2.isCompensated()); // Failed step itself shouldn't be compensated
    }

    @Test
    void execute_StepThrowsException_TriggersCompensation() {
        TestStep step1 = new TestStep(true, true, "Step1");
        SagaStep<String> throwsStep = new SagaStep<String>() {
            @Override
            public boolean process(String context) {
                throw new RuntimeException("DB Error");
            }

            @Override
            public boolean compensate(String context) {
                return true;
            }

            @Override
            public String getName() {
                return "ThrowsStep";
            }
        };

        SagaOrchestrator.SagaResult result = orchestrator.execute("Context", Arrays.asList(step1, throwsStep));

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessage().contains("Exception in step ThrowsStep"));
        assertTrue(step1.isCompensated());
    }

    private static class TestStep implements SagaStep<String> {
        private final boolean processSuccess;
        private final boolean compensateSuccess;
        private final String name;
        private boolean compensated = false;

        public TestStep(boolean processSuccess, boolean compensateSuccess, String name) {
            this.processSuccess = processSuccess;
            this.compensateSuccess = compensateSuccess;
            this.name = name;
        }

        @Override
        public boolean process(String context) {
            return processSuccess;
        }

        @Override
        public boolean compensate(String context) {
            this.compensated = true;
            return compensateSuccess;
        }

        @Override
        public String getName() {
            return name;
        }

        public boolean isCompensated() {
            return compensated;
        }
    }
}
