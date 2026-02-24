package com.apps.quantitymeasurement;

/**
 * Standalone Enum defining the supported units of weight.
 * UC9: Proves architectural scalability by mirroring LengthUnit.
 * * Base unit is explicitly KILOGRAM (1.0).
 */
public enum WeightUnit {
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
}