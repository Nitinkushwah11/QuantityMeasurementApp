package com.app.quantitymeasurementapp.entity;

/**
 * Data Transfer Object for carrying raw input strings and values 
 * from the Controller to the Service Layer.
 */
public class QuantityDTO {
    
    public double value;
    public String unit;
    public String measurementType;

    public QuantityDTO() { }

    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    // Explicitly public interfaces and enums for visibility outside the package
    public interface IMeasurableUnit {
        String getUnitName();
        String getMeasurementType();
    }

    public enum LengthUnit implements IMeasurableUnit {
        FEET, INCHES, YARDS, CENTIMETERS;
        @Override public String getUnitName() { return this.name(); }
        @Override public String getMeasurementType() { return this.getClass().getSimpleName(); }
    }

    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM, GRAM, POUND;
        @Override public String getUnitName() { return this.name(); }
        @Override public String getMeasurementType() { return this.getClass().getSimpleName(); }
    }

    public enum VolumeUnit implements IMeasurableUnit {
        LITER, MILLILITER, GALLON;
        @Override public String getUnitName() { return this.name(); }
        @Override public String getMeasurementType() { return this.getClass().getSimpleName(); }
    }

    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT;
        @Override public String getUnitName() { return this.name(); }
        @Override public String getMeasurementType() { return this.getClass().getSimpleName(); }
    }
    
    @Override
    public String toString() {
        return value + " " + unit;
    }
}