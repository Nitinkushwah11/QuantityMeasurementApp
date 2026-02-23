package com.apps.quantitymeasurement;

/**
 * A generic class for representing and comparing lengths in different units.
 * * <p>This class encapsulates a length value along with its unit of measurement.
 * All conversions and comparisons use inches as the base unit. Values converted
 * to the base unit are rounded to two decimal places for deterministic equality checks.
 * * <p>Equality semantics:
 * Two {@code Length} instances are considered equal if their values,
 * when converted to the base unit (inches) and rounded to two decimal places,
 * are numerically identical.
 * * <p>Instance conversion method (added):
 * <pre>
 * Convert this length to the specified target unit.
 * </pre>
 * * @author Developer
 * @version 1.0
 */
public class Length {
    
    private double value;
    private LengthUnit unit;

    /**
     * Nested enumeration representing different length units and their conversion factors.
     * The base unit for conversion is inches.
     */
    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    public Length(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * Converts this length value to the base unit (inches) with rounding.
     * @return the length value in inches, rounded to two decimal places
     */
    private double convertToBaseUnit() {
        double rawConvertedValue = this.value * this.unit.getConversionFactor();
        return Math.round(rawConvertedValue * 100.0) / 100.0;
    }

    private boolean compare(Length thatLength) {
        return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length that = (Length) o;
        return this.compare(that);
    }

    /**
     * Convert this length to the specified target unit.
     * * <p><b>Public API Method:</b> Provides the primary interface for unit conversion.
     * * @param targetUnit the unit to convert this length into; must not be null
     * @return a new {@code Length} representing the same physical length in targetUnit
     * @throws IllegalArgumentException if targetUnit is null
     */
    public Length convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        
        // 1. Convert current value to base unit (inches)
        double baseValueInInches = this.convertToBaseUnit();
        
        // 2. Convert from inches to the target unit
        double targetValue = baseValueInInches / targetUnit.getConversionFactor();
        
        // 3. Round to 2 decimal places and return a NEW immutable object
        double roundedTargetValue = Math.round(targetValue * 100.0) / 100.0;
        
        return new Length(roundedTargetValue, targetUnit);
    }

    /**
     * Returns a string representation of this {@code Length}.
     * <p><b>Format:</b> {@code "value UNIT"} (e.g., "12.00 INCHES", "3.50 FEET")
     */
    @Override
    public String toString() {
        return String.format("%.2f %s", this.value, this.unit.name());
    }

    // Main method for standalone testing
    public static void main(String[] args) {
        Length length1 = new Length(1.0, LengthUnit.FEET);
        System.out.println("1 Foot converted to Inches: " + length1.convertTo(LengthUnit.INCHES));
        
        Length length2 = new Length(1.0, LengthUnit.YARDS);
        System.out.println("1 Yard converted to Feet: " + length2.convertTo(LengthUnit.FEET));
    }
}