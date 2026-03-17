package com.apps.quantitymeasurement.model;


/**
 * Data Transfer Object (POJO) for holding quantity measurement input data.
 * It carries the value, unit name, and measurement type.
 */
public class QuantityDTO {
    
    public double value;
    public String unit;
    public String measurementType;

    // Default constructor (required for serialization/JSON mapping)
    public QuantityDTO() {
    }

    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    /**
     * IMeasurableUnit - Interface to represent measurable units for 
     * quantity measurements from an external/DTO perspective.
     */
    public interface IMeasurableUnit {
        public String getUnitName();
        public String getMeasurementType();
    }

    // --- SELF-CONTAINED DTO ENUMS ---
    
    public enum LengthUnit implements IMeasurableUnit {
        FEET, INCHES, YARDS, CENTIMETERS;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM, GRAM, POUND;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    public enum VolumeUnit implements IMeasurableUnit {
        LITER, MILLILITER, GALLON;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }
    
    @Override
    public String toString() {
        return value + " " + unit;
    }
}