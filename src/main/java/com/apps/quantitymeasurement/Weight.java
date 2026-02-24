package com.apps.quantitymeasurement;

/**
 * A generic class for representing and comparing weights.
 * UC9: Mirrors the Length class to provide type-safe weight operations.
 */
public class Weight {
    
    private double value;
    private WeightUnit unit;

    public Weight(double value, WeightUnit unit) {
    	 if (unit == null) {
    	        throw new IllegalArgumentException("Weight unit cannot be null");
    	    }
        this.value = value;
        this.unit = unit;
    }

    private double convertToBaseUnit() {
        return this.unit.convertToBaseUnit(this.value);
    }

    private boolean compare(Weight thatWeight) {
        // Rounding happens at the equality check to maintain arithmetic precision
        double thisBaseRounded = Math.round(this.convertToBaseUnit() * 100.0) / 100.0;
        double thatBaseRounded = Math.round(thatWeight.convertToBaseUnit() * 100.0) / 100.0;
        
        return Double.compare(thisBaseRounded, thatBaseRounded) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        // This line physically prevents comparing a Weight to a Length!
        if (o == null || getClass() != o.getClass()) return false;
        
        Weight that = (Weight) o;
        return this.compare(that);
    }

    public Weight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        
        double baseValue = this.convertToBaseUnit();
        double targetValue = targetUnit.convertFromBaseUnit(baseValue);
        double roundedTargetValue = Math.round(targetValue * 100.0) / 100.0;
        
        return new Weight(roundedTargetValue, targetUnit);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", this.value, this.unit.name());
    }
    
    private Weight addAndConvert(Weight weight, WeightUnit targetUnit) {
        if (weight == null || targetUnit == null) {
            throw new IllegalArgumentException("Weight and target unit cannot be null");
        }
        if (this.unit == null || weight.unit == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (Double.isNaN(this.value) || Double.isInfinite(this.value) || 
            Double.isNaN(weight.value) || Double.isInfinite(weight.value)) {
            throw new IllegalArgumentException("Invalid numerical inputs");
        }

        double thisBase = this.convertToBaseUnit();
        double thatBase = weight.convertToBaseUnit();
        
        double totalBaseWeight = thisBase + thatBase;
        
        double converted = targetUnit.convertFromBaseUnit(totalBaseWeight);
        converted = Math.round(converted * 100.0) / 100.0;
        
        return new Weight(converted, targetUnit);
    }

    public Weight add(Weight thatWeight) {
        return this.addAndConvert(thatWeight, this.unit);
    }

    public Weight add(Weight thatWeight, WeightUnit targetUnit) {
        return this.addAndConvert(thatWeight, targetUnit);
    }
}