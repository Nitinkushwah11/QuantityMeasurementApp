package com.apps.quantitymeasurement;

public class Length {

    private double value;
    private LengthUnit unit;

    // Enum representing supported length units
    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0);

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
        return this.value * this.unit.getConversionFactor();
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
}