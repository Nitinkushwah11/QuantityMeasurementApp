package com.apps.quantitymeasurement;
import com.apps.quantitymeasurement.LengthUnit;

/**
 * UC12 - Generic Quantity Class with Unit Interface
 * Replaces all duplicate category methods with unified generic methods!
 */
public class QuantityMeasurementApp {

    /**
     * Demonstrate Equality Comparison between two generic quantities.
     */
    public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {
        boolean result = q1.equals(q2);
        System.out.println("Input: " + q1.toString() + " equals " + q2.toString());
        System.out.println("Output: " + result + "\n");
        return result;
    }

    /**
     * Demonstrate Conversion of a generic quantity to a target unit.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {
        Quantity<U> converted = quantity.convertTo(targetUnit);
        System.out.println("Input: convert(" + quantity.toString() + " to " + targetUnit + ")");
        System.out.println("Output: " + converted.toString() + "\n");
        return converted;
    }

    /**
     * Demonstrate Addition of two generic quantities (Implicit Target).
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2) {
        Quantity<U> sum = q1.add(q2);
        System.out.println("Input: add(" + q1.toString() + ", " + q2.toString() + ")");
        System.out.println("Output: " + sum.toString() + "\n");
        return sum;
    }

    /**
     * Demonstrate Addition of two generic quantities (Explicit Target Unit).
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> sum = q1.add(q2, targetUnit);
        System.out.println("Input: add(" + q1.toString() + ", " + q2.toString() + ", " + targetUnit + ")");
        System.out.println("Output: " + sum.toString() + "\n");
        return sum;
    }

     // --- NEW UC12: SUBTRACTION & DIVISION WRAPPERS ---

    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2) {
        Quantity<U> diff = q1.subtract(q2);
        System.out.println("Input: subtract(" + q1.toString() + ", " + q2.toString() + ") -> Output: " + diff.toString());
        return diff;
    }

    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> diff = q1.subtract(q2, targetUnit);
        System.out.println("Input: subtract(" + q1.toString() + ", " + q2.toString() + ", " + targetUnit + ") -> Output: " + diff.toString());
        return diff;
    }

    public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
        double ratio = q1.divide(q2);
        System.out.println("Input: divide(" + q1.toString() + ", " + q2.toString() + ") -> Output: " + ratio);
        return ratio;
    }

    // ==========================================
    // --- STANDALONE TESTING (MAIN METHOD) ---
    // ==========================================

    public static void main(String[] args) {
        System.out.println("--- UC12 Subtraction & Division Demonstrations ---\n");
        
        // --- 1. LENGTH DEMONSTRATIONS ---
        System.out.println(">> LENGTH OPERATIONS:");
        Quantity<LengthUnit> tenFeet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> sixInches = new Quantity<>(6.0, LengthUnit.INCHES);
        Quantity<LengthUnit> twoFeet = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> twentyFourInches = new Quantity<>(24.0, LengthUnit.INCHES);
        
        // Subtraction
        demonstrateSubtraction(tenFeet, sixInches); // 10 FEET - 6 INCHES = 9.5 FEET
        demonstrateSubtraction(tenFeet, sixInches, LengthUnit.INCHES); // 10 FEET - 6 INCHES to INCHES = 114.0 INCHES
        
        // Division
        demonstrateDivision(tenFeet, twoFeet); // 10 FEET / 2 FEET = 5.0
        demonstrateDivision(twentyFourInches, twoFeet); // 24 INCHES / 2 FEET = 1.0
        
        System.out.println("\n>> WEIGHT OPERATIONS:");
        Quantity<WeightUnit> tenKg = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> fiveKg = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> fiveThousandGrams = new Quantity<>(5000.0, WeightUnit.GRAM);
        
        // Subtraction
        demonstrateSubtraction(tenKg, fiveThousandGrams); // 10 KG - 5000 G = 5.0 KG
        
        // Division
        demonstrateDivision(tenKg, fiveKg); // 10 KG / 5 KG = 2.0
        
        System.out.println("\n>> VOLUME OPERATIONS:");
        Quantity<VolumeUnit> fiveLiters = new Quantity<>(5.0, VolumeUnit.LITER);
        Quantity<VolumeUnit> tenLiters = new Quantity<>(10.0, VolumeUnit.LITER);
        Quantity<VolumeUnit> fiveHundredMl = new Quantity<>(500.0, VolumeUnit.MILLILITER);
        
        // Subtraction
        demonstrateSubtraction(fiveLiters, fiveHundredMl); // 5 L - 500 ML = 4.5 L
        
        // Division
        demonstrateDivision(fiveLiters, tenLiters); // 5 L / 10 L = 0.5
    }
}