package com.apps.quantitymeasurement;

/**
 * Standalone Enum defining the supported units of volume.
 * UC11: Proves infinite scalability. Simply creating this Enum instantly unlocks
 * Equality, Conversion, and Addition for Volume without writing any new logic!
 * Base unit is explicitly LITER (1.0).
 */
public enum VolumeUnit implements IMeasurable {
    LITER(1.0),
    MILLILITER(0.001),       // 1000 ML in 1 Liter
    GALLON(3.78541);         // 1 Gallon is ~3.78541 Liters
	
	// UC14: Define Lambda Expression for arithmetic support
    private final SupportsArithmetic supportsArithmetic = () -> true;

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    
    public double getConversionFactor() {
        return this.conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }
 // UC14: Override to use the enum's specific lambda
    @Override
    public boolean supportsArithmetic() {
        return this.supportsArithmetic.isSupported();
    }
}