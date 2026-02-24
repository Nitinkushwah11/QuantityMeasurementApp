package com.apps.quantitymeasurement;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UC9testCases {
	
	
	 // --- 1. EQUALITY TESTS ---

    @Test
    public void testEquality_KilogramToKilogram_SameValue() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(1.0, WeightUnit.KILOGRAM), 
            new Weight(1.0, WeightUnit.KILOGRAM)
        ));
    }

    @Test
    public void testEquality_KilogramToKilogram_DifferentValue() {
        assertFalse(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(1.0, WeightUnit.KILOGRAM), 
            new Weight(2.0, WeightUnit.KILOGRAM)
        ));
    }

    @Test
    public void testEquality_KilogramToGram_EquivalentValue() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(1.0, WeightUnit.KILOGRAM), 
            new Weight(1000.0, WeightUnit.GRAM)
        ));
    }

    @Test
    public void testEquality_GramToKilogram_EquivalentValue() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(1000.0, WeightUnit.GRAM), 
            new Weight(1.0, WeightUnit.KILOGRAM)
        ));
    }

    @Test
    public void testEquality_WeightVsLength_Incompatible() {
        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);
        Length length = new Length(1.0, LengthUnit.FEET);
        // equals() handles type mismatch safely and returns false
        assertFalse(weight.equals(length), "Weight should never equal Length");
    }

    @Test
    public void testEquality_NullComparison() {
        assertFalse(new Weight(1.0, WeightUnit.KILOGRAM).equals(null));
    }

    @Test
    public void testEquality_SameReference() {
        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);
        assertTrue(weight.equals(weight), "Object must equal itself");
    }

    @Test
    public void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Weight(1.0, null));
    }

    @Test
    public void testEquality_TransitiveProperty() {
        Weight a = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight b = new Weight(1000.0, WeightUnit.GRAM);
        Weight c = new Weight(1.0, WeightUnit.KILOGRAM);

        assertTrue(a.equals(b), "A equals B");
        assertTrue(b.equals(c), "B equals C");
        assertTrue(a.equals(c), "A equals C (Transitive)");
    }

    @Test
    public void testEquality_ZeroValue() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(0.0, WeightUnit.KILOGRAM), 
            new Weight(0.0, WeightUnit.GRAM)
        ));
    }

    @Test
    public void testEquality_NegativeWeight() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(-1.0, WeightUnit.KILOGRAM), 
            new Weight(-1000.0, WeightUnit.GRAM)
        ));
    }

    @Test
    public void testEquality_LargeWeightValue() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(1000.0, WeightUnit.KILOGRAM), 
            new Weight(1_000_000.0, WeightUnit.GRAM)
        ));
    }

    @Test
    public void testEquality_SmallWeightValue() {
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(
            new Weight(0.001, WeightUnit.KILOGRAM), 
            new Weight(1.0, WeightUnit.GRAM)
        ));
    }

    // --- 2. CONVERSION TESTS ---

    @Test
    public void testConversion_PoundToKilogram() {
        Weight pound = new Weight(2.20462, WeightUnit.POUND);
        Weight converted = QuantityMeasurementApp.demonstrateWeightConversion(pound, WeightUnit.KILOGRAM);
        // ~1.0 KG
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(converted, new Weight(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testConversion_KilogramToPound() {
        Weight kg = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight converted = QuantityMeasurementApp.demonstrateWeightConversion(kg, WeightUnit.POUND);
        // ~2.20462 translates to 2.20 in our 2-decimal rounded system
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(converted, new Weight(2.20, WeightUnit.POUND)));
    }

    @Test
    public void testConversion_SameUnit() {
        Weight kg = new Weight(5.0, WeightUnit.KILOGRAM);
        Weight converted = QuantityMeasurementApp.demonstrateWeightConversion(kg, WeightUnit.KILOGRAM);
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(converted, new Weight(5.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testConversion_ZeroValue() {
        Weight kg = new Weight(0.0, WeightUnit.KILOGRAM);
        Weight converted = QuantityMeasurementApp.demonstrateWeightConversion(kg, WeightUnit.GRAM);
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(converted, new Weight(0.0, WeightUnit.GRAM)));
    }

    @Test
    public void testConversion_NegativeValue() {
        Weight kg = new Weight(-1.0, WeightUnit.KILOGRAM);
        Weight converted = QuantityMeasurementApp.demonstrateWeightConversion(kg, WeightUnit.GRAM);
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(converted, new Weight(-1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testConversion_RoundTrip() {
        Weight original = new Weight(1.5, WeightUnit.KILOGRAM);
        Weight toGrams = QuantityMeasurementApp.demonstrateWeightConversion(original, WeightUnit.GRAM);
        Weight backToKg = QuantityMeasurementApp.demonstrateWeightConversion(toGrams, WeightUnit.KILOGRAM);
        
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(original, backToKg));
    }

    // --- 3. ADDITION TESTS ---

    @Test
    public void testAddition_SameUnit_KilogramPlusKilogram() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(1.0, WeightUnit.KILOGRAM), 
            new Weight(2.0, WeightUnit.KILOGRAM)
        );
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(3.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testAddition_CrossUnit_KilogramPlusGram() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(1.0, WeightUnit.KILOGRAM), 
            new Weight(1000.0, WeightUnit.GRAM)
        );
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(2.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testAddition_CrossUnit_PoundPlusKilogram() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(2.20462, WeightUnit.POUND), 
            new Weight(1.0, WeightUnit.KILOGRAM)
        );
        // 1 KG = 2.20462 lbs. So total is ~4.40924 lbs (Rounds to 4.41 lbs)
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(4.41, WeightUnit.POUND)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Gram() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(1.0, WeightUnit.KILOGRAM), 
            new Weight(1000.0, WeightUnit.GRAM), 
            WeightUnit.GRAM
        );
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(2000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testAddition_Commutativity() {
        Weight kg = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight g = new Weight(1000.0, WeightUnit.GRAM);

        Weight sum1 = QuantityMeasurementApp.demonstrateWeightAddition(kg, g); // Returns 2.0 KG
        Weight sum2 = QuantityMeasurementApp.demonstrateWeightAddition(g, kg); // Returns 2000.0 GRAM
        
        // 2.0 KG == 2000.0 GRAM
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum1, sum2));
    }

    @Test
    public void testAddition_WithZero() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(5.0, WeightUnit.KILOGRAM), 
            new Weight(0.0, WeightUnit.GRAM)
        );
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(5.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testAddition_NegativeValues() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(5.0, WeightUnit.KILOGRAM), 
            new Weight(-2000.0, WeightUnit.GRAM)
        );
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(3.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testAddition_LargeValues() {
        Weight sum = QuantityMeasurementApp.demonstrateWeightAddition(
            new Weight(1e6, WeightUnit.KILOGRAM), 
            new Weight(1e6, WeightUnit.KILOGRAM)
        );
        assertTrue(QuantityMeasurementApp.demonstrateWeightEquality(sum, new Weight(2e6, WeightUnit.KILOGRAM)));
    }
}