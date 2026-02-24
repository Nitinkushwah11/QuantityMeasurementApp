package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;




/**
 * UC11 - Generic Quantity Class with Unit Interface Test Suite
 * Proves that the generic <U extends IMeasurable> architecture perfectly handles
 * Length, Weight, and now Volume operations with infinite scalability.
 */
public class QuantityMeasurementAppTest {

    // ==========================================
    // --- 1. IMEASURABLE INTERFACE TESTS ---
    // ==========================================

    @Test
    public void testIMeasurableInterface_LengthUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(LengthUnit.class));
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), "Feet factor should be 1.0");
    }

    @Test
    public void testIMeasurableInterface_WeightUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(WeightUnit.class));
        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), "Kilogram factor should be 1.0");
    }

    @Test
    public void testIMeasurableInterface_ConsistentBehavior() {
        // Proves that both enums have identical method contracts via the interface
        IMeasurable length = LengthUnit.FEET;
        IMeasurable weight = WeightUnit.KILOGRAM;
        assertNotNull(length);
        assertNotNull(weight);
    }

    // ==========================================
    // --- 2. GENERIC QUANTITY - LENGTH OPERATIONS ---
    // ==========================================

    @Test
    public void testGenericQuantity_LengthOperations_Equality() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    public void testGenericQuantity_LengthOperations_Conversion() {
        Quantity<LengthUnit> converted = new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES);
        assertEquals(12.0, converted.getValue());
        assertEquals(LengthUnit.INCHES, converted.getUnit());
    }

    @Test
    public void testGenericQuantity_LengthOperations_Addition() {
        Quantity<LengthUnit> sum = new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertEquals(2.0, sum.getValue());
    }

    // ==========================================
    // --- 3. GENERIC QUANTITY - WEIGHT OPERATIONS ---
    // ==========================================

    @Test
    public void testGenericQuantity_WeightOperations_Equality() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testGenericQuantity_WeightOperations_Conversion() {
        Quantity<WeightUnit> converted = new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, converted.getValue());
    }

    @Test
    public void testGenericQuantity_WeightOperations_Addition() {
        Quantity<WeightUnit> sum = new Quantity<>(1.0, WeightUnit.KILOGRAM).add(new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM);
        assertEquals(2.0, sum.getValue());
    }

    // ==========================================
    // --- 4. CROSS-CATEGORY & TYPE SAFETY ---
    // ==========================================

    @Test
    public void testCrossCategoryPrevention_LengthVsWeight() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    // ==========================================
    // --- 5. VALIDATION & IMMUTABILITY ---
    // ==========================================

    @Test
    public void testGenericQuantity_ConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    public void testGenericQuantity_ConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    public void testImmutability_GenericQuantity() {
        Quantity<LengthUnit> original = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> added = original.add(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(1.0, original.getValue());
        assertNotSame(original, added);
    }

    // ==========================================
    // --- 6. APP CONTROLLER DEMONSTRATION TESTS ---
    // ==========================================

    @Test
    public void testQuantityMeasurementApp_SimplifiedDemonstration_Equality() {
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    public void testQuantityMeasurementApp_SimplifiedDemonstration_Conversion() {
        Quantity<WeightUnit> converted = QuantityMeasurementApp.demonstrateConversion(new Quantity<>(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);
        assertEquals(1000.0, converted.getValue());
    }

    @Test
    public void testQuantityMeasurementApp_SimplifiedDemonstration_Addition() {
        Quantity<LengthUnit> sum = QuantityMeasurementApp.demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertEquals(2.0, sum.getValue());
    }

    // ==========================================
    // --- 7. EXPLICIT VOLUME TEST CASES (UC11) ---
    // ==========================================

    
    // --- Volume Equality Tests ---
    @Test
    public void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITER).equals(new Quantity<>(1.0, VolumeUnit.LITER)));
    }

    @Test
    public void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITER).equals(new Quantity<>(2.0, VolumeUnit.LITER)));
    }

   
    @Test
    public void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITER);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITER);
        Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITER);
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    @Test
    public void testEquality_ZeroValue() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITER).equals(new Quantity<>(0.0, VolumeUnit.MILLILITER)));
    }

    @Test
    public void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITER).equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITER)));
    }

    @Test
    public void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1000000.0, VolumeUnit.MILLILITER).equals(new Quantity<>(1000.0, VolumeUnit.LITER)));
    }

    @Test
    public void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITER).equals(new Quantity<>(1.0, VolumeUnit.MILLILITER)));
    }

    // --- Volume Conversion Tests ---
    

 

    // ==========================================
    // --- 8. AUTOMATED MATRICES (UC4, UC5, UC6/UC7, UC11) ---
    // ==========================================

    @ParameterizedTest(name = "Automated Equality: {0} {1} == {2} {3} -> {4}")
    @MethodSource("provideEqualityData")
    public <U extends IMeasurable> void testAutomatedEqualityMatrix(
            double val1, U unit1, double val2, U unit2, boolean expected) {

        Quantity<U> q1 = new Quantity<>(val1, unit1);
        Quantity<U> q2 = new Quantity<>(val2, unit2);

        assertEquals(expected, q1.equals(q2), "Equality matrix failed");
    }

    @ParameterizedTest(name = "Automated Conversion: {0} {1} to {2} = {3}")
    @MethodSource("provideConversionData")
    public <U extends IMeasurable> void testAutomatedConversionMatrix(
            double val, U fromUnit, U toUnit, double expectedVal) {

        Quantity<U> q = new Quantity<>(val, fromUnit);
        Quantity<U> converted = q.convertTo(toUnit);

        assertEquals(expectedVal, converted.getValue(), "Conversion math failed");
        assertEquals(toUnit, converted.getUnit(), "Target unit mismatch");
    }

    @ParameterizedTest(name = "Automated Addition: {0} {1} + {2} {3} to {4} = {5}")
    @MethodSource("provideAdditionData")
    public <U extends IMeasurable> void testAutomatedAdditionMatrix(
            double val1, U unit1, double val2, U unit2, U targetUnit, double expectedSum) {

        Quantity<U> q1 = new Quantity<>(val1, unit1);
        Quantity<U> q2 = new Quantity<>(val2, unit2);

        Quantity<U> result = QuantityMeasurementApp.demonstrateAddition(q1, q2, targetUnit);

        assertEquals(expectedSum, result.getValue(), "Addition math failed");
        assertEquals(targetUnit, result.getUnit(), "Target unit mismatch");
    }

    // --- DATA FACTORIES FOR THE MATRICES ---

    private static Stream<Arguments> provideEqualityData() {
        List<Arguments> args = new ArrayList<>();

        // 1. Length Combinations
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                double val1 = 2.0;
                double val2 = (val1 * u1.getConversionFactor()) / u2.getConversionFactor();
                args.add(Arguments.of(val1, u1, val2, u2, true));
                args.add(Arguments.of(val1, u1, val2 + 1.0, u2, false));
            }
        }

        // 2. Weight Combinations
        for (WeightUnit u1 : WeightUnit.values()) {
            for (WeightUnit u2 : WeightUnit.values()) {
                double val1 = 2.0;
                double val2 = (val1 * u1.getConversionFactor()) / u2.getConversionFactor();
                args.add(Arguments.of(val1, u1, val2, u2, true));
                args.add(Arguments.of(val1, u1, val2 + 1.0, u2, false));
            }
        }

        // 3. Volume Combinations (UC11)
        for (VolumeUnit u1 : VolumeUnit.values()) {
            for (VolumeUnit u2 : VolumeUnit.values()) {
                double val1 = 2.0;
                double val2 = (val1 * u1.getConversionFactor()) / u2.getConversionFactor();
                args.add(Arguments.of(val1, u1, val2, u2, true));
                args.add(Arguments.of(val1, u1, val2 + 1.0, u2, false));
            }
        }

        return args.stream();
    }

    private static Stream<Arguments> provideConversionData() {
        List<Arguments> args = new ArrayList<>();

        // 1. Length Conversions
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                double val = 2.0;
                double expected = (val * u1.getConversionFactor()) / u2.getConversionFactor();
                expected = Math.round(expected * 100.0) / 100.0;
                args.add(Arguments.of(val, u1, u2, expected));
            }
        }

        // 2. Weight Conversions
        for (WeightUnit u1 : WeightUnit.values()) {
            for (WeightUnit u2 : WeightUnit.values()) {
                double val = 2.0;
                double expected = (val * u1.getConversionFactor()) / u2.getConversionFactor();
                expected = Math.round(expected * 100.0) / 100.0;
                args.add(Arguments.of(val, u1, u2, expected));
            }
        }

        // 3. Volume Conversions (UC11)
        for (VolumeUnit u1 : VolumeUnit.values()) {
            for (VolumeUnit u2 : VolumeUnit.values()) {
                double val = 2.0;
                double expected = (val * u1.getConversionFactor()) / u2.getConversionFactor();
                expected = Math.round(expected * 100.0) / 100.0;
                args.add(Arguments.of(val, u1, u2, expected));
            }
        }

        return args.stream();
    }

    private static Stream<Arguments> provideAdditionData() {
        List<Arguments> args = new ArrayList<>();

        // 1. Length Additions
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                for (LengthUnit target : LengthUnit.values()) {
                    double v1 = 2.0, v2 = 3.0;
                    double totalBase = (v1 * u1.getConversionFactor()) + (v2 * u2.getConversionFactor());
                    double expected = totalBase / target.getConversionFactor();
                    expected = Math.round(expected * 100.0) / 100.0;
                    args.add(Arguments.of(v1, u1, v2, u2, target, expected));
                }
            }
        }

        // 2. Weight Additions
        for (WeightUnit u1 : WeightUnit.values()) {
            for (WeightUnit u2 : WeightUnit.values()) {
                for (WeightUnit target : WeightUnit.values()) {
                    double v1 = 2.0, v2 = 3.0;
                    double totalBase = (v1 * u1.getConversionFactor()) + (v2 * u2.getConversionFactor());
                    double expected = totalBase / target.getConversionFactor();
                    expected = Math.round(expected * 100.0) / 100.0;
                    args.add(Arguments.of(v1, u1, v2, u2, target, expected));
                }
            }
        }

        // 3. Volume Additions (UC11)
        for (VolumeUnit u1 : VolumeUnit.values()) {
            for (VolumeUnit u2 : VolumeUnit.values()) {
                for (VolumeUnit target : VolumeUnit.values()) {
                    double v1 = 2.0, v2 = 3.0;
                    double totalBase = (v1 * u1.getConversionFactor()) + (v2 * u2.getConversionFactor());
                    double expected = totalBase / target.getConversionFactor();
                    expected = Math.round(expected * 100.0) / 100.0;
                    args.add(Arguments.of(v1, u1, v2, u2, target, expected));
                }
            }
        }

        return args.stream();
    }
}