package com.app.quantitymeasurementapp.unit;

public enum WeightUnit implements IMeasurable {
    KILOGRAM(1.0), 
    GRAM(0.001), 
    POUND(0.453592);

    private final SupportsArithmetic arithmeticSupport = () -> true;
    private final double conversionFactor;

    WeightUnit(double conversionFactor) { 
        this.conversionFactor = conversionFactor; 
    }
    
    public double getConversionFactor() { 
        return this.conversionFactor; 
    }

    @Override 
    public double convertToBaseUnit(double value) { 
        return value * this.conversionFactor; 
    }
    
    @Override 
    public double convertFromBaseUnit(double baseValue) { 
        return baseValue / this.conversionFactor; 
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
        for (WeightUnit unit : WeightUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid weight unit: " + unitName);
    }
}