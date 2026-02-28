package com.apps.quantitymeasurement;

import java.util.function.Function;

/**
 * UC14: Temperature Units using Lambda Functions for non-linear conversion.
 * Base Unit: CELSIUS
 */
public enum TemperatureUnit implements IMeasurable {
    
    // Celsius is the base unit, so it just returns its own value
    CELSIUS(
        celsius -> celsius, 
        celsius -> celsius
    ),
    
    
    FAHRENHEIT(
        fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0,
        celsius -> (celsius * 9.0 / 5.0) + 32.0
    );

    private final Function<Double, Double> toBase;
    private final Function<Double, Double> fromBase;

    TemperatureUnit(Function<Double, Double> toBase, Function<Double, Double> fromBase) {
        this.toBase = toBase;
        this.fromBase = fromBase;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toBase.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return fromBase.apply(value);
    }

    // --- UC14: DISABLE ARITHMETIC ---
    
    @Override
    public boolean supportsArithmetic() {
        return false; // Temperature cannot be added, subtracted, or divided!
    }
}