package com.apps.quantitymeasurement;
import com.apps.quantitymeasurement.Length.LengthUnit;

public class QuantityMeasurementApp {

	// Generic method to demonstrate Length equality check (returns boolean)
    public static boolean demonstrateLengthEquality(Length length1, Length length2) {
        return length1.equals(length2);
    }

    // Helper method to take in parameters, create objects, and print the comparison clearly
    public static void demonstrateLengthComparison(double value1, LengthUnit unit1, 
                                                   double value2, LengthUnit unit2) {
        Length length1 = new Length(value1, unit1);
        Length length2 = new Length(value2, unit2);
        
        System.out.println("Input: Quantity(" + value1 + ", " + unit1 + ") and Quantity(" + value2 + ", " + unit2 + ")");
        System.out.println("Output: Equal (" + demonstrateLengthEquality(length1, length2) + ")\n");
    }
    // Main method
    public static void main(String[] args) {
    	// Demonstrate Feet and Inches comparison
        demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
        
        // Demonstrate Yards and Inches comparison
        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES);
        
        // Demonstrate Centimeters and Inches comparison
        demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701, LengthUnit.INCHES);
        
        // Demonstrate Feet and Yards comparison
        demonstrateLengthComparison(3.0, LengthUnit.FEET, 1.0, LengthUnit.YARDS);
        
        // Demonstrate Centimeters and Feet comparison
        demonstrateLengthComparison(30.48, LengthUnit.CENTIMETERS, 1.0, LengthUnit.FEET);
    }

  
}