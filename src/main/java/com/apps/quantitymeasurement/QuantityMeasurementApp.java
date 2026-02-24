package com.apps.quantitymeasurement;
import com.apps.quantitymeasurement.Length.LengthUnit;

/**
 * UC5 - QuantityMeasurementApp - Extended Unit Support with Conversion
 * * <p>Extends UC4 to provide unit-to-unit conversion for values within the same
 * measurement category. Adds explicit conversion logic so callers can obtain a 
 * numeric result in the requested target unit.
 */
public class QuantityMeasurementApp {

    public static boolean demonstrateLengthEquality(Length length1, Length length2) {
        return length1.equals(length2);
    }

    public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {
        Length length1 = new Length(value1, unit1);
        Length length2 = new Length(value2, unit2);
        boolean result = demonstrateLengthEquality(length1, length2);
        System.out.println("Input: Quantity(" + value1 + ", " + unit1 + ") and Quantity(" + value2 + ", " + unit2 + ")");
        System.out.println("Output: Equal (" + result + ")\n");
        return result;
    }

    /**
     * Demonstrate length conversion from one unit to another (Raw values).
     * METHOD OVERLOAD 1
     */
    public static Length demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
        Length initialLength = new Length(value, fromUnit);
        Length convertedLength = initialLength.convertTo(toUnit);
        System.out.println("Input: convert(" + value + ", " + fromUnit + ", " + toUnit + ")");
        System.out.println("Output: " + convertedLength + "\n");
        return convertedLength;
    }

    /**
     * Demonstrate length conversion from one QuantityLength instance to another unit.
     * METHOD OVERLOAD 2
     */
    public static Length demonstrateLengthConversion(Length length, LengthUnit toUnit) {
        Length convertedLength = length.convertTo(toUnit);
        System.out.println("Input: convert(" + length.toString() + " to " + toUnit + ")");
        System.out.println("Output: " + convertedLength + "\n");
        return convertedLength;
    }
    
    
    /**
     * Converts a numeric value from a source unit to a target unit.
     * Internally normalizes to the base unit (inches) and returns the converted value.
     *
     * @param value  the numeric value to convert
     * @param source the unit of the given value
     * @param target the unit to convert to
     * @return the converted numeric value
     * @throws IllegalArgumentException if units are null, or value is NaN/Infinite
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        // 1. Validate that neither unit is null
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units must not be null");
        }
        
        // 2. Validate that the number is actually a valid math number (not NaN or Infinity)
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Value must be a valid, finite number");
        }
        
        // 3. Normalize to the base unit (multiply by the source's conversion factor)
        double baseValue = value * source.getConversionFactor();
        
        // 4. Convert to the target unit (divide by the target's conversion factor)
        return baseValue / target.getConversionFactor();
    }
    
    
    /**
     * Demonstrate addition of second QuantityLength to first QuantityLength.
     *
     * @param length1 the first QuantityLength instance
     * @param length2 the second QuantityLength instance
     *
     * @return a new QuantityLength instance representing the sum of the two lengths
     */
    public static Length demonstrateLengthAddition(Length length1, Length length2) {
        // Delegate to the core math engine you built in Length.java
        Length sumLength = length1.add(length2);
        
     
        
        // Return the object for the test cases to evaluate
        return sumLength;
    }
    
    public static void main(String[] args) {
        System.out.println("--- UC5 Conversion Demonstrations ---\n");
        
        // Using Overload 1 (Raw values)
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(100.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        
        // Using Overload 2 (Object pass-through)
        Length myFeet = new Length(5.0, LengthUnit.FEET);
        demonstrateLengthConversion(myFeet, LengthUnit.INCHES);
    }
}