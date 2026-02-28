package com.apps.quantitymeasurement;

/**
 * IMeasurable interface defines the contract for measurable units.
 * Serves as a common abstraction for different types of measurements (Length, Weight, etc.).
 */
public interface IMeasurable {
    
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double value);

    /**
     * Functional Interface pattern conceptually built-in.
     * By default, all standard measurement units support arithmetic.
     */
    default boolean supportsArithmetic() {
        return true;
    }

    /**
     * Validates if the operation is supported. Throws an exception if not.
     */
    default void validateOperationSupport(String operationName) {
        if (!supportsArithmetic()) {
            throw new UnsupportedOperationException(
                "Arithmetic operations (" + operationName + ") are not supported for " + this.getClass().getSimpleName()
            );
        }
    }
}