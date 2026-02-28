package com.apps.quantitymeasurement;

/**
 * Standalone Enum defining the supported units of weight.
 
 * * Base unit is explicitly KILOGRAM (1.0).
 */
public enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0),
    GRAM(0.001),         // 1 gram is 0.001 kilograms
    POUND(0.453592);     // 1 pound is roughly 0.453592 kilograms

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return this.conversionFactor;
    }

    /**
     * Convert value from this unit to the base unit (KILOGRAM).
     */
    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    /**
     * Convert value from the base unit (KILOGRAM) to this unit.
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