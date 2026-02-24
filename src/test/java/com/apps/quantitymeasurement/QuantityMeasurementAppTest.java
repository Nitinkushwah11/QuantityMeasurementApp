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

	//=========================================
	//UC-4 test cases with @MethodSource to provide data automatically to test all possible test cases
	//=========================================
	
	
	@ParameterizedTest(name = "Equality Matrix {index}: {0} {1} should equal {2} {3}")
    @MethodSource("provideLengthsForEqualityMatrix")
    public void testCrossUnitEqualityMatrix(double val1, Length.LengthUnit unit1, double val2, Length.LengthUnit unit2) {
        // 1. Arrange: Create the two lengths based on the arguments handed to us by the Stream
        Length length1 = new Length(val1, unit1);
        Length length2 = new Length(val2, unit2);
        
        // 2. Assert: Check that your UC4 equals() method recognizes them as equal
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(length1, length2), 
                   val1 + " " + unit1 + " should equal " + val2 + " " + unit2);
    }

    // The DYNAMIC Data Factory Method
    private static Stream<Arguments> provideLengthsForEqualityMatrix() {
       List<Arguments> arguments = new ArrayList<>();

        // Loop through EVERY unit for the left side
        for (Length.LengthUnit unit1 : Length.LengthUnit.values()) {
            // Loop through EVERY unit for the right side
            for (Length.LengthUnit unit2 : Length.LengthUnit.values()) {
                
                double val1 = 1.0; // We always start with 1.0 of the first unit
                
                // Calculate exactly what val2 should be based on their conversion factors
                double val2 = (val1 * unit1.getConversionFactor()) / unit2.getConversionFactor();
                
                
                
                arguments.add(Arguments.of(val1, unit1, val2, unit2));
            }
        }
        
        return arguments.stream();
    }
    
    
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
    
    
    // ==========================================
    // --- UC5: EXPLICIT CONVERSION TESTS ---
    // ==========================================

    private static final double EPSILON = 1e-6;

   // Automated tests
    @ParameterizedTest(name="{index}: {0} {1} -> {2} {3}")
    @MethodSource("dataProviderForConversionTest")
    
    //test for checking the conversions
    public void testConversions(double length1,LengthUnit unit1,double length2, LengthUnit unit2) {
    	//calling conversion from static utility
    	
    	double converted=QuantityMeasurementApp.convert(length1, unit1, unit2);
    	
    	//asserting equal with output
    	
    	assertEquals(converted,length2,EPSILON);
    }
    
    
    //method to provide data for the test
    public static Stream<Arguments> dataProviderForConversionTest(){
    	List<Arguments> args=new ArrayList<>();
    	
    	//using 12.0 as it will multiply and divide properly for some units
    	
    	double initialValue=12.0;
    	
    	//looping to attach all possible conversions in args
    	for(LengthUnit source:LengthUnit.values()) {
    		//looping again to collect to all values
    		for(LengthUnit target:LengthUnit.values()) {
    			
    			//converting to target 
    			double converted=(initialValue*source.getConversionFactor())/target.getConversionFactor();
    			
    			
    			//adding to the target
    			args.add(Arguments.of(initialValue,source,converted,target));
    		}
    	}
    	
    	
    	// returning the stream
    	return args.stream();
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
    
    
    //=================================
    //UC 6 Specific comments
    //=================================
    
   //automated tests for all available values
    
    @ParameterizedTest(name = "Addition {index}: {0} {1} + {2} {3} = {4} {1}")
    @MethodSource("provideDataForAdditionMatrix")
    public void testCrossUnitAdditionMatrix(double val1, Length.LengthUnit unit1,
                                            double val2, Length.LengthUnit unit2,
                                            double expectedSumValue) {

        // Create input and expected objects
        Length length1 = new Length(val1, unit1);
        Length length2 = new Length(val2, unit2);
        Length expectedLength = new Length(expectedSumValue, unit1);

        // Perform addition
        Length actualSumLength =
                QuantityMeasurementApp.demonstrateLengthAddition(length1, length2);

        // Verify result
        assertTrue(
            QuantityMeasurementApp.demonstrateLengthEquality(actualSumLength, expectedLength),
            "Failed addition: " + val1 + " " + unit1 + " + " + val2 + " " + unit2
        );
    }

    // Data provider for addition test
    private static Stream<Arguments> provideDataForAdditionMatrix() {
        java.util.List<Arguments> arguments = new java.util.ArrayList<>();

        double val1 = 2.0;
        double val2 = 3.0;

        for (Length.LengthUnit unit1 : Length.LengthUnit.values()) {
            for (Length.LengthUnit unit2 : Length.LengthUnit.values()) {

                // Convert both values to base unit
                double totalBaseUnits =
                        (val1 * unit1.getConversionFactor()) +
                        (val2 * unit2.getConversionFactor());

                // Convert back to target unit
                double rawExpected =
                        totalBaseUnits / unit1.getConversionFactor();

                // Round to 2 decimal places
                double expectedSumValue =
                        Math.round(rawExpected * 100.0) / 100.0;

                arguments.add(
                    Arguments.of(val1, unit1, val2, unit2, expectedSumValue)
                );
            }
        }

        return arguments.stream();
    }
    
    @Test
    public void testAddition_Commutativity() {
        Length sum1 = QuantityMeasurementApp.demonstrateLengthAddition(new Length(1.0, Length.LengthUnit.FEET), new Length(12.0, Length.LengthUnit.INCHES));
        Length sum2 = QuantityMeasurementApp.demonstrateLengthAddition(new Length(12.0, Length.LengthUnit.INCHES), new Length(1.0, Length.LengthUnit.FEET));
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum1, sum2));
    }

    @Test
    public void testAddition_WithZero() {
        Length sumLength = QuantityMeasurementApp.demonstrateLengthAddition(new Length(5.0, Length.LengthUnit.FEET), new Length(0.0, Length.LengthUnit.INCHES));
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sumLength, new Length(5.0, Length.LengthUnit.FEET)));
    }

    @Test
    public void testAddition_NegativeValues() {
        Length sumLength = QuantityMeasurementApp.demonstrateLengthAddition(new Length(5.0, Length.LengthUnit.FEET), new Length(-2.0, Length.LengthUnit.FEET));
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sumLength, new Length(3.0, Length.LengthUnit.FEET)));
    }

    @Test
    public void testAddition_NullSecondOperand() {
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateLengthAddition(new Length(1.0, Length.LengthUnit.FEET), null));
    }

    @Test
    public void testAddition_LargeValues() {
        Length sumLength = QuantityMeasurementApp.demonstrateLengthAddition(new Length(1e6, Length.LengthUnit.FEET), new Length(1e6, Length.LengthUnit.FEET));
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sumLength, new Length(2e6, Length.LengthUnit.FEET)));
    }

    @Test
    public void testAddition_SmallValues() {
        Length sumLength = QuantityMeasurementApp.demonstrateLengthAddition(new Length(0.01, Length.LengthUnit.FEET), new Length(0.02, Length.LengthUnit.FEET));
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sumLength, new Length(0.03, Length.LengthUnit.FEET)));
    }
}