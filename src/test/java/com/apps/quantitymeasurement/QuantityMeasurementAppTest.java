package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//importing class to be tested
import com.apps.quantitymeasurement.Length.LengthUnit;

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

  // Same unit and same value comparison
  @Test
  public void testEquality_FeetToFeet_SameValue() {
      assertEquals(new Length(1.0, LengthUnit.FEET), new Length(1.0, LengthUnit.FEET));
  }

  @Test
  public void testEquality_InchToInch_SameValue() {
      assertEquals(new Length(1.0, LengthUnit.INCHES), new Length(1.0, LengthUnit.INCHES));
  }

  // Cross-unit equivalent comparison
  @Test
  public void testEquality_FeetToInch_EquivalentValue() {
      assertEquals(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCHES));
  }

  @Test
  public void testEquality_InchToFeet_EquivalentValue() {
      assertEquals(new Length(12.0, LengthUnit.INCHES), new Length(1.0, LengthUnit.FEET));
  }

  // Same unit but different value comparison
  @Test
  public void testEquality_FeetToFeet_DifferentValue() {
      assertNotEquals(new Length(1.0, LengthUnit.FEET), new Length(2.0, LengthUnit.FEET));
  }

  @Test
  public void testEquality_InchToInch_DifferentValue() {
      assertNotEquals(new Length(1.0, LengthUnit.INCHES), new Length(2.0, LengthUnit.INCHES));
  }

  // Invalid enum handling
  @Test
  public void testEquality_InvalidUnit() {
      assertThrows(IllegalArgumentException.class, () -> {
          LengthUnit.valueOf("INVALID_UNIT");
      });
  }

  // Null unit handling
  @Test
  public void testEquality_NullUnit() {
      Length validLength = new Length(1.0, LengthUnit.FEET);
      Length invalidLength = new Length(1.0, null);

      assertThrows(NullPointerException.class, () -> {
          validLength.equals(invalidLength);
      });
  }

  @Test
  public void testEquality_SameReference() {
      Length length = new Length(1.0, LengthUnit.FEET);
      assertEquals(length, length);
  }

  @Test
  public void testEquality_NullComparison() {
      assertNotEquals(null, new Length(1.0, LengthUnit.FEET));
  }
  
  //UC4 test cases
  @Test
  public void testEquality_YardsToFeet_EquivalentValue() {
      // Verifies that Quantity(1.0, YARDS) and Quantity(3.0, FEET) are equal
      assertEquals(new Length(1.0, LengthUnit.YARDS), new Length(3.0, LengthUnit.FEET));
  }

  @Test
  public void testEquality_YardsToInches_EquivalentValue() {
      // Verifies that Quantity(1.0, YARDS) and Quantity(36.0, INCHES) are equal
      assertEquals(new Length(1.0, LengthUnit.YARDS), new Length(36.0, LengthUnit.INCHES));
  }

  @Test
  public void testEquality_YardsToYards_SameValue() {
      // Verifies that Quantity(2.0, YARDS) and Quantity(2.0, YARDS) are equal
      assertEquals(new Length(2.0, LengthUnit.YARDS), new Length(2.0, LengthUnit.YARDS));
  }

  @Test
  public void testEquality_CentimetersToCentimeters_SameValue() {
      // Verifies that Quantity(2.0, CENTIMETERS) and Quantity(2.0, CENTIMETERS) are equal
      assertEquals(new Length(2.0, LengthUnit.CENTIMETERS), new Length(2.0, LengthUnit.CENTIMETERS));
  }

  @Test
  public void testEquality_CentimetersToInches_EquivalentValue() {
      // Verifies that Quantity(100.0, CENTIMETERS) and Quantity(39.3701, INCHES) are equal
      assertEquals(new Length(100.0, LengthUnit.CENTIMETERS), new Length(39.3701, LengthUnit.INCHES));
  }
  
// ==========================================
  // --- UC5: EXPLICIT CONVERSION TESTS ---
  // ==========================================

  private static final double EPSILON = 1e-6;

  @Test
  public void testConversion_FeetToInches() {
      double result = QuantityMeasurementApp.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
      assertEquals(12.0, result, EPSILON);
  }

  @Test
  public void testConversion_InchesToFeet() {
      double result = QuantityMeasurementApp.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
      assertEquals(2.0, result, EPSILON);
  }

  @Test
  public void testConversion_YardsToInches() {
      double result = QuantityMeasurementApp.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
      assertEquals(36.0, result, EPSILON);
  }

  @Test
  public void testConversion_InchesToYards() {
      double result = QuantityMeasurementApp.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
      assertEquals(2.0, result, EPSILON);
  }

  @Test
  public void testConversion_CentimetersToInches() {
      double result = QuantityMeasurementApp.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
      // 2.54 cm is roughly 1.0 inch based on our conversion factor
      assertEquals(1.0, result, 0.01); // Slightly larger epsilon for standard CM rounding
  }

  @Test
  public void testConversion_FeatToYard() {
      double result = QuantityMeasurementApp.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS);
      assertEquals(2.0, result, EPSILON);
  }

  @Test
  public void testConversion_RoundTrip_PreservesValue() {
      double originalValue = 5.0;
      // convert(convert(v, A, B), B, A) ≈ v
      double inInches = QuantityMeasurementApp.convert(originalValue, LengthUnit.YARDS, LengthUnit.INCHES);
      double backToYards = QuantityMeasurementApp.convert(inInches, LengthUnit.INCHES, LengthUnit.YARDS);
      assertEquals(originalValue, backToYards, EPSILON);
  }

  @Test
  public void testConversion_ZeroValue() {
      double result = QuantityMeasurementApp.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
      assertEquals(0.0, result, EPSILON);
  }

  @Test
  public void testConversion_NegativeValue() {
      double result = QuantityMeasurementApp.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
      assertEquals(-12.0, result, EPSILON);
  }

  @Test
  public void testConversion_InvalidUnit_Throws() {
      assertThrows(IllegalArgumentException.class, () -> {
          QuantityMeasurementApp.convert(1.0, LengthUnit.FEET, null);
      });
      assertThrows(IllegalArgumentException.class, () -> {
          QuantityMeasurementApp.convert(1.0, null, LengthUnit.INCHES);
      });
  }

  @Test
  public void testConversion_NaNOrInfinite_Throws() {
      assertThrows(IllegalArgumentException.class, () -> {
          QuantityMeasurementApp.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
      });
      assertThrows(IllegalArgumentException.class, () -> {
          QuantityMeasurementApp.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES);
      });
      assertThrows(IllegalArgumentException.class, () -> {
          QuantityMeasurementApp.convert(Double.NEGATIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES);
      });
  }

  @Test
  public void testConversion_PrecisionTolerance() {
      double result = QuantityMeasurementApp.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
      // This explicitly proves the epsilon works. 
      // 12.0 is equal to 12.0000001 within the 1e-6 (0.000001) tolerance.
      assertEquals(12.0000001, result, EPSILON);
  }
}