package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;

public class UC7TestCases {
	
	// ==========================================
    // --- UC7: DYNAMIC TARGET UNIT ADDITION MATRIX ---
    // ==========================================

    @ParameterizedTest(name = "UC7 {index}: {0} {1} + {2} {3} to {5} = {4} {5}")
    @MethodSource("provideDataForUC7AdditionMatrix")
    public void testAdditionWithTargetUnitMatrix(double val1, Length.LengthUnit unit1, double val2, Length.LengthUnit unit2, double expectedSum, Length.LengthUnit targetUnit) {
        
        // 1. Arrange
        Length length1 = new Length(val1, unit1);
        Length length2 = new Length(val2, unit2);
        Length expectedLength = new Length(expectedSum, targetUnit);
        
        // 2. Act: Call your new UC7 demonstration method
        Length actualSumLength = QuantityMeasurementApp.demonstrateLengthAddition(length1, length2, targetUnit);
        
        // 3. Assert: Verify the app's math matches the test's math
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(actualSumLength, expectedLength),
            "Failed UC7 addition: " + val1 + " " + unit1 + " + " + val2 + " " + unit2 + " targeting " + targetUnit);
    }

    // The Data Factory generating 64 combinations
    private static Stream<Arguments> provideDataForUC7AdditionMatrix() {
       List<Arguments> arguments = new ArrayList<>();

        // Using 2.0 and 3.0 to ensure the math is thoroughly tested
        double val1 = 2.0; 
        double val2 = 3.0;

        // Loop 1: First operand's unit
        for (Length.LengthUnit unit1 : Length.LengthUnit.values()) {
            // Loop 2: Second operand's unit
            for (Length.LengthUnit unit2 : Length.LengthUnit.values()) {
                // Loop 3: The requested TARGET unit
                for (Length.LengthUnit targetUnit : Length.LengthUnit.values()) {
                    
                    // Step A: Calculate total inches mathematically
                    double totalBaseUnits = (val1 * unit1.getConversionFactor()) + (val2 * unit2.getConversionFactor());
                    
                    // Step B: Convert to the explicit target unit
                    double rawExpected = totalBaseUnits / targetUnit.getConversionFactor();
                    
                    // Step C: Round to 2 decimals to match the core Length.java logic
                    double expectedSumValue = Math.round(rawExpected * 100.0) / 100.0;
                    
                    // Add it to the test queue
                    arguments.add(org.junit.jupiter.params.provider.Arguments.of(val1, unit1, val2, unit2, expectedSumValue, targetUnit));
                }
            }
        }
        
        return arguments.stream();
    }
	
    @Test
    public void testAddition_ExplicitTargetUnit_Commutativity() {
        Length length1 = new Length(1.0, Length.LengthUnit.FEET);
        Length length2 = new Length(12.0, Length.LengthUnit.INCHES);
        
        // A + B -> YARDS
        Length sum1 = QuantityMeasurementApp.demonstrateLengthAddition(length1, length2, Length.LengthUnit.YARDS);
        // B + A -> YARDS
        Length sum2 = QuantityMeasurementApp.demonstrateLengthAddition(length2, length1, Length.LengthUnit.YARDS);
        
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum1, sum2), 
            "Commutativity should hold with explicit target unit");
    }

    @Test
    public void testAddition_ExplicitTargetUnit_WithZero() {
        Length sum = QuantityMeasurementApp.demonstrateLengthAddition(
            new Length(5.0, Length.LengthUnit.FEET), 
            new Length(0.0, Length.LengthUnit.INCHES), 
            Length.LengthUnit.YARDS
        );
        // 5.0 Feet + 0.0 Inches = 5.0 Feet. 5.0 Feet / 3.0 = 1.666... (Rounds to 1.67)
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum, new Length(1.67, Length.LengthUnit.YARDS)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NegativeValues() {
        Length sum = QuantityMeasurementApp.demonstrateLengthAddition(
            new Length(5.0, Length.LengthUnit.FEET), 
            new Length(-2.0, Length.LengthUnit.FEET), 
            Length.LengthUnit.INCHES
        );
        // 5 Feet - 2 Feet = 3 Feet = 36 Inches
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum, new Length(36.0, Length.LengthUnit.INCHES)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuantityMeasurementApp.demonstrateLengthAddition(
                new Length(1.0, Length.LengthUnit.FEET), 
                new Length(12.0, Length.LengthUnit.INCHES), 
                null
            );
        }, "Should throw IllegalArgumentException when target unit is null");
    }

    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        Length sum = QuantityMeasurementApp.demonstrateLengthAddition(
            new Length(1000.0, Length.LengthUnit.FEET), 
            new Length(500.0, Length.LengthUnit.FEET), 
            Length.LengthUnit.INCHES
        );
        // 1500 Feet * 12 = 18000 Inches
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum, new Length(18000.0, Length.LengthUnit.INCHES)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        Length sum = QuantityMeasurementApp.demonstrateLengthAddition(
            new Length(12.0, Length.LengthUnit.INCHES), 
            new Length(12.0, Length.LengthUnit.INCHES), 
            Length.LengthUnit.YARDS
        );
        // 24 Inches = 2 Feet = 0.666... Yards (Rounds to 0.67)
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum, new Length(0.67, Length.LengthUnit.YARDS)));
    }
}