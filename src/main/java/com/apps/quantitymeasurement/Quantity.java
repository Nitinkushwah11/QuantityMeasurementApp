package com.apps.quantitymeasurement;

/**
 * A generic class for representing, comparing, and adding quantities.
 * UC10 Refactor: Replaces category-specific classes (Length, Weight) with a single generic engine.
 */
public class Quantity<U extends IMeasurable> {
    
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (Double.isNaN(value) || Double.isInfinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public U getUnit() { return unit; }

    private double convertToBaseUnit() {
        return this.unit.convertToBaseUnit(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Quantity<?> that = (Quantity<?>) o;
        
        // CRITICAL UC10 SAFETY: Physically prevents comparing Lengths to Weights!
        if (this.unit.getClass() != that.unit.getClass()) return false;
        
        // Safe cast now that we verified classes match
        @SuppressWarnings("unchecked")
        Quantity<U> typedThat = (Quantity<U>) that;
        
        double thisBase = this.convertToBaseUnit();
        double thatBase = typedThat.convertToBaseUnit();
        
        return Math.abs(thisBase - thatBase) <= 1e-6;
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        
        double baseValue = this.convertToBaseUnit();
        double targetValue = targetUnit.convertFromBaseUnit(baseValue);
        double roundedTargetValue = Math.round(targetValue * 100.0) / 100.0;
        
        return new Quantity<>(roundedTargetValue, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return this.add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) throw new IllegalArgumentException("Incompatible categories");

        double thisBase = this.convertToBaseUnit();
        double thatBase = other.convertToBaseUnit();
        
        double totalBase = thisBase + thatBase;
        
        double converted = targetUnit.convertFromBaseUnit(totalBase);
        converted = Math.round(converted * 100.0) / 100.0;
        
        return new Quantity<>(converted, targetUnit);
    }

    @Override
    public String toString() {
        // e.g., "12.00 INCHES" or "1.00 KILOGRAM"
        return String.format("%.2f %s", this.value, ((Enum<?>) this.unit).name());
    }
}