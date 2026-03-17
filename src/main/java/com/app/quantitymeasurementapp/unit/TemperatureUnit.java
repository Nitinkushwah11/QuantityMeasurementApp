package com.app.quantitymeasurementapp.unit;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {
    
    CELSIUS(c -> c, c -> c),
    FAHRENHEIT(f -> (f - 32.0) * 5.0 / 9.0, c -> (c * 9.0 / 5.0) + 32.0);

    // Temperature requires mathematical formulas instead of simple ratio factors
    private final Function<Double, Double> toBase;
    private final Function<Double, Double> fromBase;
    
    // Explicitly disabling arithmetic for temperature
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

    @Override
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