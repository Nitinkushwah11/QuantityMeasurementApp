package com.app.quantitymeasurementapp.unit;

/**
 * Core interface for all measurement units in the application.
 * Defines the contract for conversions and arithmetic validation.
 */
public interface IMeasurable {
    
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double value);

    // Default arithmetic support (can be overridden by specific units)
    SupportsArithmetic supportsArithmetic = () -> true;

    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    /**
     * Throws an exception if the unit does not support the requested operation.
     */
    default void validateOperationSupport(String operationName) {
        if (!this.supportsArithmetic()) {
            throw new UnsupportedOperationException(
                "Arithmetic operations (" + operationName + ") are not supported for " + this.getClass().getSimpleName()
            );
        }
    }

    String getUnitName();
    String getMeasurementType();
    IMeasurable getUnitInstance(String unitName);
}
