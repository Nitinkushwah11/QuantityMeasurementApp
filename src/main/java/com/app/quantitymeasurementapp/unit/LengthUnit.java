package com.app.quantitymeasurementapp.unit;

public enum LengthUnit implements IMeasurable {
    FEET(1.0), 
    INCHES(1.0 / 12.0), 
    YARDS(3.0), 
    CENTIMETERS(1.0 / 30.48);

    private final SupportsArithmetic arithmeticSupport = () -> true;
    private final double conversionFactor;

    LengthUnit(double conversionFactor) { 
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
        for (LengthUnit unit : LengthUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid length unit: " + unitName);
    }
}
