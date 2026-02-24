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
     * Adds another {@code Length} to this one.
     *
     * <p><b>Public API Method:</b> This method allows adding two lengths of the same category.
     * The result is returned in the unit of the first operand, with rounding applied for consistency.
     * <p><b>Addition Pipeline:</b>
     * <ol>
     * <li>Convert both lengths to the base unit (inches)</li>
     * <li>Sum the base unit values</li>
     * <li>Convert the sum back to the unit of this instance</li>
     * <li>Round the result to two decimal places</li>
     * <li>Return a new {@code Length} instance with the summed value</li>
     * </ol>
     *
     * @param thatLength the {@code Length} to add
     * @return a new {@code Length} representing the sum in this instance's unit
     * @throws IllegalArgumentException if the provided length or units are null, or if values are invalid
     */
    public Length add(Length thatLength) {
    	
    	if(thatLength==null) {
    		throw new IllegalArgumentException("Object cannot cant be null");
    	}
    	
    	if(this.unit==null || thatLength.unit==null) {
    		throw new IllegalArgumentException("Units canot be null");
    	}
    	
    	if(Double.isNaN(this.value)||Double.isInfinite(this.value) || Double.isNaN(thatLength.value) || Double.isInfinite(thatLength.value)) {
    		throw new IllegalArgumentException("INVALID inputs");
    	}
    	
    	double thisBase=this.convertToBaseUnit();
    	double thatBase=thatLength.convertToBaseUnit();
    	double totalLength=thisBase+thatBase;
    	
    	double  converted=convertFromBaseToTargetUnit(totalLength,this.unit);
    	converted=Math.round(converted*100.0)/100.0;
    	return new Length(converted,this.unit);
    }
    
    
    //Converting from base to required value
    /**
     * Converts a length value from the base unit (inches) to the specified target unit.
     * * @param lengthInInches the length value in inches to convert
     * @param targetUnit the unit to convert the length into
     * @return the converted length value in the target unit
     */
    private double convertFromBaseToTargetUnit(double lengthInInches,LengthUnit targetUnit) {
    	return (lengthInInches/targetUnit.getConversionFactor());
    }
    
    
    /**
     * Overloaded addition: Adds two Length objects and returns the result in a specified target unit.
     * * @param length1 the first length operand
     * @param length2 the second length operand to add
     * @param targetUnit the desired unit for the final result
     * @return a new Length object representing the sum in the target unit
     */
    public static Length add(Length length1, Length length2, LengthUnit targetUnit) {
        if (length1 == null || length2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Lengths and target unit cannot be null");
        }
        // Call core instance method, then convert the result to the requested unit
        Length sumInFirstUnit = length1.add(length2);
        return sumInFirstUnit.convertTo(targetUnit);
    }
    
    
    /**
     * Overloaded addition: Adds two raw length values with their units and returns the result in a target unit.
     * * @param val1 the numeric value of the first length
     * @param unit1 the unit of the first length
     * @param val2 the numeric value of the second length
     * @param unit2 the unit of the second length
     * @param targetUnit the desired unit for the final result
     * @return a new Length object representing the sum in the target unit
     */
    public static Length add(double val1, LengthUnit unit1, double val2, LengthUnit unit2, LengthUnit targetUnit) {
        // Create the objects from raw data, then pass them to the method right above this one!
        Length l1 = new Length(val1, unit1);
        Length l2 = new Length(val2, unit2);
        return add(l1, l2, targetUnit);
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

        // 2. Test commutativity (Reverse the order, returns in INCHES this time)
        Length sum2 = twelveInches.add(oneFoot);
        System.out.println("2. Commutativity Test (12 Inches + 1 Foot):");
        System.out.println("   Expected: 24.00 INCHES");
        System.out.println("   Actual:   " + sum2 + "\n");

        // 3. Test the 3-argument static overloaded method (Force output to YARDS)
        
        Length sum3 = Length.add(oneFoot, twelveInches, LengthUnit.YARDS);
        System.out.println("3. Static Object Add (Force Target to YARDS):");
        System.out.println("   Expected: 0.67 YARDS");
        System.out.println("   Actual:   " + sum3 + "\n");

        // 4. Test the 5-argument static overloaded method (Raw data)
        
        Length sum4 = Length.add(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET, LengthUnit.INCHES);
        System.out.println("4. Static Raw Data Add (1 Yard + 3 Feet to INCHES):");
        System.out.println("   Expected: 72.00 INCHES");
        System.out.println("   Actual:   " + sum4 + "\n");
    }
}