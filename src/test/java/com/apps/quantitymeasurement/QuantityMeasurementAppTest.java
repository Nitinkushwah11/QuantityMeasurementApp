package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//importing class to be tested
import com.apps.quantitymeasurement.QuantityMeasurementApp.Feet;

//import inches class
import com.apps.quantitymeasurement.QuantityMeasurementApp.Inches;

public class QuantityMeasurementAppTest {
	@Test
	public void testFeetEquality_SameValue()
	{
		//Verifies taht two numerical values of 1.0 ft are considered equal.
		assertEquals(new Feet(1.0), new Feet(1.0));
		
	}
	
	@Test
	public void estEquality_DifferentValue()
	{
	    // Verifies that two numerical values of 1.0 ft and 2.0 ft are not equal.
		assertNotEquals(new Feet(1.0), new Feet(2.0));
	}
	
	@Test
	public void testFeetEquality_NullComparison()
	{
		   // Verifies that a numerical value is not equal to null.
		   assertNotEquals(null, new Feet(1.0));
	}
	
	@Test
	public void testFeetEquality_DifferentClass()
	{
		// Verifies that non-numeric inputs (or different object types) are handled appropriately.
		assertNotEquals(new Feet(1.0), "1.0");
	}
	
	@Test
    public void testFeetEquality_SameReference() {
        // Verifies that a numerical value is equal to itself (reflexive property).
        Feet feet = new Feet(1.0);
        assertEquals(feet, feet);
    }
	
	 //Tests for inches
    @Test
    public void testInchesEquality_SameValue() {
    	assertEquals(new Inches(1.0),new Inches(1.0));
    	
    }
    
    @Test
    public void testInchesEquality_DifferentVlaue() {
    	assertNotEquals(new Inches(1.0),new Inches(2.0));
    }
    
    @Test
    public void testInchesEquality_NullComparison() {
        assertNotEquals(null, new Inches(1.0));
    }

    @Test
    public void testInchesEquality_DifferentClass() {
        assertNotEquals(new Inches(1.0), "1.0");
    }

    @Test
    public void testInchesEquality_SameReference() {
        Inches inches = new Inches(1.0);
        assertEquals(inches, inches);

}
}
