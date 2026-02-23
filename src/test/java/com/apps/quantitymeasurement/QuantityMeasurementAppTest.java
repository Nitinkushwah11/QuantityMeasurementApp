package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//importing class to be tested
import com.apps.quantitymeasurement.Length.LengthUnit;

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
}