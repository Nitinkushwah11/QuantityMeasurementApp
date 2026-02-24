package com.apps.quantitymeasurement;

/**
 * IMeasurable interface defines the contract for measurable units.
 * Serves as a common abstraction for different types of measurements (Length, Weight, etc.).
 */
public interface IMeasurable {
    /**
     * @return the conversion factor to the base unit
     */
    double getConversionFactor();

    /**
     * Convert value from this unit to the base unit.
     */
    double convertToBaseUnit(double value);

    /**
     * Convert value from the base unit to this unit.
     */
    double convertFromBaseUnit(double baseValue);
}