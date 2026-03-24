package com.app.quantitymeasurement.exception;
/**
 * Base exception class for the Quantity Measurement Application.
 * Extends RuntimeException to allow unchecked exception handling across layers.
 */
public class QuantityMeasurementException extends RuntimeException {
    
    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}