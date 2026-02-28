package com.apps.quantitymeasurement;
/**
 * Standalone Enum defining the supported units of length.
 * UC8 Refactor: Extracted from QuantityLength to prevent circular dependencies
 * and to enforce the Single Responsibility Principle.
 * * Base unit is now explicitly FEET (1.0).
 */
public enum LengthUnit implements IMeasurable{
    FEET(1.0), 
    INCHES(1.0 / 12.0), 
    YARDS(3.0), 
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return this.conversionFactor;
    }

    /**
     * Convert value from this unit to the base unit (FEET).
     * @param value the value in this unit
     * @return the value converted to FEET
     */
    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    /**
     * Convert value from the base unit (FEET) to this unit.
     * @param baseValue the value in FEET
     * @return the value converted to this unit
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }
 // UC14: Define Lambda Expression for arithmetic support
    private final SupportsArithmetic supportsArithmetic = () -> true;
    
    
 // UC14: Override to use the enum's specific lambda
    @Override
    public boolean supportsArithmetic() {
        return this.supportsArithmetic.isSupported();
    }
}