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
    
    
    
   
    /**
     * Private utility method to perform addition conversion on base unit value.
     * This avoids code duplication and enforces the DRY principle.
     */
    private Length addAndConvert(Length length, LengthUnit targetUnit) {
        // 1. Validations
        if (length == null || targetUnit == null) {
            throw new IllegalArgumentException("Length and target unit cannot be null");
        }
        if (this.unit == null || length.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (Double.isNaN(this.value) || Double.isInfinite(this.value) || 
            Double.isNaN(length.value) || Double.isInfinite(length.value)) {
            throw new IllegalArgumentException("Invalid numerical inputs");
        }

        // 2. Convert both to the base unit (Inches)
        double thisBase = this.convertToBaseUnit();
        double thatBase = length.convertToBaseUnit();
        
        // 3. Add them together
        double totalBaseLength = thisBase + thatBase;
        
        // 4. Convert to the requested target unit
        double converted = convertFromBaseToTargetUnit(totalBaseLength,targetUnit);
        
        // 5. Round to 2 decimal places to match business logic
        converted = Math.round(converted * 100.0) / 100.0;
        
        return new Length(converted, targetUnit);
    }

    /**
     * UC6: Add another length, returning the result in the unit of the FIRST operand.
     */
    public Length add(Length thatLength) {
        // Delegate to the private engine, passing 'this.unit' as the target
        return this.addAndConvert(thatLength, this.unit);
    }

    /**
     * UC7: Add another length, returning the result in the EXPLICITLY specified target unit.
     */
    public Length add(Length thatLength, LengthUnit targetUnit) {
        // Delegate to the private engine, passing the requested 'targetUnit'
        return this.addAndConvert(thatLength, targetUnit);
    }
    
    
    //Converting from base to required value
    /**
     * Converts a length value from the base unit (inches) to the specified target unit.
     * * @param lengthInInches the length value in inches to convert
     * @param targetUnit the unit to convert the length into
     * @return the converted length value in the target unit
     */
    private static double convertFromBaseToTargetUnit(double lengthInInches,LengthUnit targetUnit) {
    	return (lengthInInches/targetUnit.getConversionFactor());
    }
    
    
    
    

    // Main method for standalone testing
    public static void main(String[] args) {
    	System.out.println("--- UC6: Length Addition Standalone Tests ---\n");

        // Setting up some basic lengths
        Length oneFoot = new Length(1.0, LengthUnit.FEET);
        Length twelveInches = new Length(12.0, LengthUnit.INCHES);
        Length oneYard = new Length(1.0, LengthUnit.YARDS);

        // 1. Test the core instance method 
        Length sum1 = oneFoot.add(twelveInches);
        System.out.println("1. Core Instance Add (1 Foot + 12 Inches):");
        System.out.println("   Expected: 2.00 FEET");
        System.out.println("   Actual:   " + sum1 + "\n");

        
    }
}