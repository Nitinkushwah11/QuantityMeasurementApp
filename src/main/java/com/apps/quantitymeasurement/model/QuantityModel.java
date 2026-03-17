package com.apps.quantitymeasurement.model;

import com.apps.quantitymeasurement.units.IMeasurable;
/**
 * A generic POJO model class for representing a quantity with its associated unit 
 * of measurement derived from IMeasurable.
 * * The purpose of this DTO class is to be used within the service layer for 
 * performing operations on quantities (conversion, comparison, arithmetic).
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