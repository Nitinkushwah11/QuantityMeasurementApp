package com.app.quantitymeasurementapp.unit;

/**
 * Functional interface to define whether a specific unit type 
 * supports arithmetic operations (addition, subtraction, division).
 */
@FunctionalInterface
public interface SupportsArithmetic {
    boolean isSupported();
}