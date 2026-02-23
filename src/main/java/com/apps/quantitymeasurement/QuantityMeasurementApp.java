package com.apps.quantitymeasurement;
import com.apps.quantitymeasurement.Length.LengthUnit;
public class QuantityMeasurementApp {


    // Generic method to demonstrate Length equality check
    public static boolean demonstrateLengthEquality(Length length1, Length length2) {
        return length1.equals(length2);
    }

    // Static method to demonstrate Feet equality
    public static void demonstrateFeetEquality() {
        Length feet1 = new Length(1.0, LengthUnit.FEET);
        Length feet2 = new Length(1.0, LengthUnit.FEET);
        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + demonstrateLengthEquality(feet1, feet2) + ")");
    }

    // Static method to demonstrate Inches equality
    public static void demonstrateInchesEquality() {
        Length inch1 = new Length(1.0, LengthUnit.INCHES);
        Length inch2 = new Length(1.0, LengthUnit.INCHES);
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + demonstrateLengthEquality(inch1, inch2) + ")");
    }

    // Static method to demonstrate Cross-Unit comparison!
    public static void demonstrateFeetInchesComparison() {
        Length feet = new Length(1.0, LengthUnit.FEET);
        Length inches = new Length(12.0, LengthUnit.INCHES);
        System.out.println("Input: 1.0 ft and 12.0 inches");
        System.out.println("Output: Equal (" + demonstrateLengthEquality(feet, inches) + ")");
    }

    // Main method
    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateInchesEquality();
        demonstrateFeetInchesComparison(); // The new UC3 feature!
    }
}