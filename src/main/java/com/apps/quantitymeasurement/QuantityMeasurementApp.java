package com.apps.quantitymeasurement;
import com.apps.quantitymeasurement.LengthUnit;

/**
 * UC10 - Generic Quantity Class with Unit Interface
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

    // ==========================================
    // --- STANDALONE TESTING ---
    // ==========================================

    public static void main(String[] args) {
        System.out.println("--- UC10 Generic Architecture Demonstrations ---\n");
        
        // --- 1. LENGTH DEMO (Using the Generic Quantity Class) ---
        System.out.println(">> LENGTH OPERATIONS:");
        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> twelveInches = new Quantity<>(12.0, LengthUnit.INCHES);
        
        demonstrateEquality(oneFoot, twelveInches);
        demonstrateAddition(oneFoot, twelveInches, LengthUnit.FEET);

        // --- 2. WEIGHT DEMO (Using the exact same Generic methods!) ---
        System.out.println(">> WEIGHT OPERATIONS:");
        Quantity<WeightUnit> oneKg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> thousandGrams = new Quantity<>(1000.0, WeightUnit.GRAM);
        Quantity<WeightUnit> pounds = new Quantity<>(2.20462, WeightUnit.POUND);
        
        demonstrateEquality(oneKg, thousandGrams);
        demonstrateConversion(pounds, WeightUnit.KILOGRAM);
        demonstrateAddition(oneKg, thousandGrams, WeightUnit.GRAM);
        
        // --- 3. CROSS-CATEGORY PREVENTION (Proving Type Safety) ---
        System.out.println(">> CROSS-CATEGORY PREVENTION:");
        System.out.println("Can 1.0 FOOT equal 1.0 KILOGRAM?");
        System.out.println("Output: " + oneFoot.equals(oneKg)); 
        // Note: We can't use demonstrateEquality(oneFoot, oneKg) because the compiler 
        // physically stops us from passing mismatched <U> types to the method! That is true safety.
    }
}