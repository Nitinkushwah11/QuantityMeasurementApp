package com.apps.quantitymeasurement;
import com.apps.quantitymeasurement.LengthUnit;


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
  
  
  /**
   * Demonstrate addition of second QuantityLength to first QuantityLength
   * with an explicitly specified target unit.
   *
   * @param length1 the first QuantityLength instance
   * @param length2 the second QuantityLength instance
   * @param targetUnit the target unit for the result
   * @return a new Length instance representing the sum of the two lengths in the target unit
   */
  public static Length demonstrateLengthAddition(Length length1, Length length2, LengthUnit targetUnit) {
      
      // 1. Call the new overloaded UC7 add() method from your Length class
      Length sumLength = length1.add(length2, targetUnit);
      
      // 2. Print the operation to the console to match the UC7 Example Output
      System.out.println("Input: add(" + length1.toString() + ", " + length2.toString() + ", " + targetUnit + ") -> Output: " + sumLength.toString());
      
      // 3. Return the result
      return sumLength;
  }
  
// ==========================================
  // --- WEIGHT FUNCTIONALITY (UC9) ---
  // ==========================================

  public static boolean demonstrateWeightEquality(Weight weight1, Weight weight2) {
      return weight1.equals(weight2);
  }

  public static boolean demonstrateWeightComparison(double value1, WeightUnit unit1, double value2, WeightUnit unit2) {
      Weight weight1 = new Weight(value1, unit1);
      Weight weight2 = new Weight(value2, unit2);
      boolean result = demonstrateWeightEquality(weight1, weight2);
      System.out.println("Input: Quantity(" + value1 + ", " + unit1 + ") and Quantity(" + value2 + ", " + unit2 + ")");
      System.out.println("Output: Equal (" + result + ")\n");
      return result;
  }

  public static Weight demonstrateWeightConversion(double value, WeightUnit fromUnit, WeightUnit toUnit) {
      Weight initialWeight = new Weight(value, fromUnit);
      Weight convertedWeight = initialWeight.convertTo(toUnit);
      System.out.println("Input: convert(" + value + ", " + fromUnit + ", " + toUnit + ")");
      System.out.println("Output: " + convertedWeight + "\n");
      return convertedWeight;
  }

  public static Weight demonstrateWeightConversion(Weight weight, WeightUnit toUnit) {
      Weight convertedWeight = weight.convertTo(toUnit);
      System.out.println("Input: convert(" + weight.toString() + " to " + toUnit + ")");
      System.out.println("Output: " + convertedWeight + "\n");
      return convertedWeight;
  }

  public static double convert(double value, WeightUnit source, WeightUnit target) {
      if (source == null || target == null) {
          throw new IllegalArgumentException("Source and target units must not be null");
      }
      if (Double.isNaN(value) || Double.isInfinite(value)) {
          throw new IllegalArgumentException("Value must be a valid, finite number");
      }
      double baseValue = source.convertToBaseUnit(value);
      return target.convertFromBaseUnit(baseValue);
  }

  public static Weight demonstrateWeightAddition(Weight weight1, Weight weight2) {
      return weight1.add(weight2);
  }

  public static Weight demonstrateWeightAddition(Weight weight1, Weight weight2, WeightUnit targetUnit) {
      Weight sumWeight = weight1.add(weight2, targetUnit);
      System.out.println("Input: add(" + weight1.toString() + ", " + weight2.toString() + ", " + targetUnit + ") -> Output: " + sumWeight.toString());
      return sumWeight;
  }
  
  // ==========================================
  // --- STANDALONE TESTING ---
  // ==========================================

  public static void main(String[] args) {
      System.out.println("--- UC8 Standalone Length Demonstrations ---\n");
      Length oneFoot = new Length(1.0, LengthUnit.FEET);
      Length twelveInches = new Length(12.0, LengthUnit.INCHES);
      demonstrateLengthAddition(oneFoot, twelveInches, LengthUnit.FEET);
      demonstrateLengthConversion(oneFoot, LengthUnit.INCHES);

      System.out.println("\n--- UC9 Standalone Weight Demonstrations ---\n");
      Weight oneKg = new Weight(1.0, WeightUnit.KILOGRAM);
      Weight thousandGrams = new Weight(1000.0, WeightUnit.GRAM);
      Weight pounds = new Weight(2.20462, WeightUnit.POUND);
      
      System.out.println("1. Equality Demo:");
      demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
      
      System.out.println("2. Conversion Demo:");
      demonstrateWeightConversion(pounds, WeightUnit.KILOGRAM);
      
      System.out.println("3. Addition Demo:");
      demonstrateWeightAddition(oneKg, thousandGrams, WeightUnit.KILOGRAM);
  }
}