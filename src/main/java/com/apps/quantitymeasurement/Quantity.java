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
        
        double thisBase = this.unit.convertToBaseUnit(this.value);
        double thatBase = typedThat.unit.convertToBaseUnit(typedThat.value);
        
        return Math.abs(thisBase - thatBase) <= 1e-6;
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        
        double baseValue = this.unit.convertToBaseUnit(this.value);
        double targetValue = targetUnit.convertFromBaseUnit(baseValue);
        double roundedTargetValue = Math.round(targetValue * 100.0) / 100.0;
        
        return new Quantity<>(roundedTargetValue, targetUnit);
    }

    // ==========================================
    // --- UC13: REFACTORED PUBLIC MATH API ---
    // ==========================================

    public Quantity<U> add(Quantity<U> other) {
        return this.add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(Math.round(converted * 100.0) / 100.0, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return this.subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(Math.round(converted * 100.0) / 100.0, targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    // ==========================================
    // --- UC13: CENTRALIZED HELPERS (THE DRY MAGIC) ---
    // ==========================================

    /**
     * Centralized validation to prevent duplicate null and cross-category checks.
     */
    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {
        if (other == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (targetUnitRequired && targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) throw new IllegalArgumentException("Incompatible categories");
    }

    /**
     * Centralized conversion and execution engine.
     */
    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        double thisBase = this.unit.convertToBaseUnit(this.value);
        double otherBase = other.unit.convertToBaseUnit(other.value);
        return operation.compute(thisBase, otherBase);
    }

    // ==========================================
    // --- UC13: THE STRATEGY ENUM (IMAGE 1) ---
    // ==========================================

    private enum ArithmeticOperation {
        ADD {
            @Override
            public double compute(double thisBase, double otherBase) {
                return thisBase + otherBase;
            }
        },
        SUBTRACT {
            @Override
            public double compute(double thisBase, double otherBase) {
                return thisBase - otherBase;
            }
        },
        DIVIDE {
            @Override
            public double compute(double thisBase, double otherBase) {
                if (otherBase == 0.0) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                return thisBase / otherBase;
            }
        };

        public abstract double compute(double thisBase, double otherBase);
    }

   
    @Override
    public String toString() {
        // e.g., "12.00 INCHES" or "1.00 KILOGRAM"
        return String.format("%.2f %s", this.value, ((Enum<?>) this.unit).name());
    }
}