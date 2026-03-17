package com.app.quantitymeasurementapp.entity;

import com.app.quantitymeasurementapp.unit.IMeasurable;

/**
 * The internal domain model that ties a numeric value to a strongly-typed Enum unit.
 */
public class QuantityModel<U extends IMeasurable> {
    
    public double value;
    public U unit;

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return value + " " + (unit != null ? unit.getClass().getSimpleName() : "null");
    }
}