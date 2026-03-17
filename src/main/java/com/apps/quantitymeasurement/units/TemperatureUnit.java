package com.apps.quantitymeasurement.units;

import java.util.function.Function;


public enum TemperatureUnit implements IMeasurable {
    
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
    
    // UC14/15: Lambda expression indicating arithmetic is NOT supported!
    private final SupportsArithmetic arithmeticSupport = () -> false;

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

    @Override
    public boolean supportsArithmetic() {
        return this.arithmeticSupport.isSupported();
    }

    // --- UC15 NEW METHODS ---

    public String getUnitName() {
        return this.name();
    }

    @Override
    public String getMeasurementType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid temperature unit: " + unitName);
    }
}