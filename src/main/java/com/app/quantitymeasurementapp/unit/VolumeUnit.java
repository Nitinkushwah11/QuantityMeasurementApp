package com.app.quantitymeasurementapp.unit;



public enum VolumeUnit implements IMeasurable {
    LITER(1.0), 
    MILLILITER(0.001), 
    GALLON(3.78541);

    private final SupportsArithmetic arithmeticSupport = () -> true;
    private final double conversionFactor;

    VolumeUnit(double conversionFactor) { 
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
        for (VolumeUnit unit : VolumeUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid volume unit: " + unitName);
    }
}