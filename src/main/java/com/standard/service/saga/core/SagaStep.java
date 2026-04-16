package com.standard.service.saga.core;

public interface SagaStep<T> {

    /**
     * Executes the main business logic for this step.
     * 
     * @param context the saga context
     * @return true if successful, false otherwise
     */
    boolean process(T context);

    /**
     * Executes the compensating transaction if a subsequent step in the saga fails.
     * 
     * @param context the saga context
     * @return true if compensation is successful, false otherwise
     */
    boolean compensate(T context);

    /**
     * Human-readable name of the step for logging.
     * 
     * @return name of the step
     */
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
