package com.apps.quantitymeasurement;

/**
 * UC14: Functional Interface to dictate if a unit supports arithmetic.
 */
@FunctionalInterface
public interface SupportsArithmetic {
    boolean isSupported();
}