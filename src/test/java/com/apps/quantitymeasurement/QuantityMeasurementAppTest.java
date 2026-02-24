package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;


//importing class to be tested
import com.apps.quantitymeasurement.LengthUnit;

/**
 * QuantityMeasurementAppUC6Test
 *
 * Test class for UC6 – Addition Operations Between Length Measurements
 * Validates the addition operations performed on different length measurements.
 *
 * @author Developer
 * @version 6.0
 */

public class QuantityMeasurementAppTest {

	// ==========================================
    // --- 1. IMEASURABLE INTERFACE TESTS ---
    // ==========================================

    @Test
    public void testIMeasurableInterface_LengthUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(LengthUnit.class), "LengthUnit must implement IMeasurable");
        IMeasurable unit = LengthUnit.FEET;
        assertEquals(1.0, unit.getConversionFactor(), "Feet factor should be 1.0");
    }

    @Test
    public void testIMeasurableInterface_WeightUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(WeightUnit.class), "WeightUnit must implement IMeasurable");
        IMeasurable unit = WeightUnit.KILOGRAM;
        assertEquals(1.0, unit.getConversionFactor(), "Kilogram factor should be 1.0");
    }

    // ==========================================
    // --- 8. AUTOMATED MATRICES (UC4, UC5, UC6/UC7) ---
    // ==========================================

    @ParameterizedTest(name = "Automated Equality: {0} {1} == {2} {3} -> {4}")
    @MethodSource("provideEqualityData")
    public <U extends IMeasurable> void testAutomatedEqualityMatrix(double val1, U unit1, double val2, U unit2, boolean expected) {
        Quantity<U> q1 = new Quantity<>(val1, unit1);
        Quantity<U> q2 = new Quantity<>(val2, unit2);
        assertEquals(expected, q1.equals(q2), "Equality matrix failed");
    }

    @ParameterizedTest(name = "Automated Conversion: {0} {1} to {2} = {3}")
    @MethodSource("provideConversionData")
    public <U extends IMeasurable> void testAutomatedConversionMatrix(double val, U fromUnit, U toUnit, double expectedVal) {
        Quantity<U> q = new Quantity<>(val, fromUnit);
        Quantity<U> converted = q.convertTo(toUnit);

        assertEquals(expectedVal, converted.getValue(), "Conversion math failed");
        assertEquals(toUnit, converted.getUnit(), "Target unit mismatch");
    }

    @ParameterizedTest(name = "Automated Addition: {0} {1} + {2} {3} to {4} = {5}")
    @MethodSource("provideAdditionData")
    public <U extends IMeasurable> void testAutomatedAdditionMatrix(double val1, U unit1, double val2, U unit2, U targetUnit, double expectedSum) {
        Quantity<U> q1 = new Quantity<>(val1, unit1);
        Quantity<U> q2 = new Quantity<>(val2, unit2);

        Quantity<U> result = QuantityMeasurementApp.demonstrateAddition(q1, q2, targetUnit);

        assertEquals(expectedSum, result.getValue(), "Addition math failed");
        assertEquals(targetUnit, result.getUnit(), "Target unit mismatch");
    }

    // --- DATA FACTORIES FOR THE MATRICES ---

    private static Stream<Arguments> provideEqualityData() {
        List<Arguments> args = new ArrayList<>();

        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                double val1 = 2.0;
                double val2 = (val1 * u1.getConversionFactor()) / u2.getConversionFactor();
                args.add(Arguments.of(val1, u1, val2, u2, true));
                args.add(Arguments.of(val1, u1, val2 + 1.0, u2, false));
            }
        }

        for (WeightUnit u1 : WeightUnit.values()) {
            for (WeightUnit u2 : WeightUnit.values()) {
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

        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                double val = 2.0;
                double expected = (val * u1.getConversionFactor()) / u2.getConversionFactor();
                expected = Math.round(expected * 100.0) / 100.0;
                args.add(Arguments.of(val, u1, u2, expected));
            }
        }

        for (WeightUnit u1 : WeightUnit.values()) {
            for (WeightUnit u2 : WeightUnit.values()) {
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

        return args.stream();
    }
}