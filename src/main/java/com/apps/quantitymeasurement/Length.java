package com.apps.quantitymeasurement;

public class Length {

    private double value;
    private LengthUnit unit;

    // Enum representing supported length units
    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
    	YARDS(36.0),            // 1 Yard = 36 Inches
        CENTIMETERS(0.393701);  // 1 CM = 0.393701 Inches

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        // Returns conversion factor to inches
        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    // Constructor to initialize value and unit
    public Length(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    // Converts current length to base unit (inches)
    private double convertToBaseUnit() {
        double rawConvertedVlaue=this.value * this.unit.getConversionFactor();
        return Math.round(rawConvertedVlaue*100.0)/100.0;
        
    }

    // Compares two Length objects
    public boolean compare(Length thatLength) {
        return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
    }

    // Overrides equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Length that = (Length) o;
        return this.compare(that);
    }
    
    
    public static void main(String[] args) {
        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0, LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + length1.equals(length2)); // Should print true

        Length length3 = new Length(1.0, LengthUnit.YARDS);
        Length length4 = new Length(36.0, LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + length3.equals(length4)); // Should print true

        Length length5 = new Length(100.0, LengthUnit.CENTIMETERS);
        Length length6 = new Length(39.3701, LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + length5.equals(length6)); // Should print true
    }
}