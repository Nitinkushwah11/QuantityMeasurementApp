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

    public Quantity<U> add(Quantity<U> other) {
        return this.add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) throw new IllegalArgumentException("Incompatible categories");

        double thisBase = this.unit.convertToBaseUnit(this.value);
        double thatBase = other.unit.convertToBaseUnit(other.value);
        
        double totalBase = thisBase + thatBase;
        
        double converted = targetUnit.convertFromBaseUnit(totalBase);
        converted = Math.round(converted * 100.0) / 100.0;
        
        return new Quantity<>(converted, targetUnit);
    }
    
   
    /**
     * UC12: Subtracts another Quantity from this Quantity.
     * Implicitly returns the result in the unit of THIS quantity.
     * * @param other the Quantity to subtract
     * @return a new Quantity representing the difference
     */
    public Quantity<U> subtract(Quantity<U> other) {
        return this.subtract(other, this.unit);
    }

    /**
     * UC12: Subtracts another Quantity from this Quantity.
     * Explicitly returns the result in the specified target unit.
     * * @param other the Quantity to subtract
     * @param targetUnit the unit to convert the final answer into
     * @return a new Quantity representing the difference in the target unit
     * @throws IllegalArgumentException if arguments are null or incompatible
     */
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        
    	if (other == null || targetUnit == null) throw new IllegalArgumentException("Arguments cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) throw new IllegalArgumentException("Incompatible categories");
        
        //converting the values
        double thisBase=this.unit.convertToBaseUnit(this.value);
        double otherBase=other.unit.convertToBaseUnit(other.value);
        
        double subtractedValue=thisBase-otherBase;
        
        double converted=targetUnit.convertFromBaseUnit(subtractedValue);
        
        
        converted=Math.round(converted*100.0)/100.0;
  
        return new Quantity<>(converted,targetUnit); 
    }
    
    
    /**
     * UC12: Divides this Quantity by another Quantity.
     * Returns a dimensionless scalar (pure number) representing the ratio.
     * * @param other the Quantity to act as the divisor
     * @return a primitive double representing how many times 'other' fits into 'this'
     * @throws IllegalArgumentException if 'other' is null, incompatible, or zero
     */
    public double divide(Quantity<U> other) {
    	
        //checking for null
    	if(other==null) {
    		throw new IllegalArgumentException("Arguments cannot be null");
    	}
    	
    	if(this.unit.getClass()!=other.unit.getClass()) {
    		throw new IllegalArgumentException("Incompatible categories");
    	}
    	
        //converting to base Units
        double thisBase=this.unit.convertToBaseUnit(this.value);
        double otherBase=other.unit.convertToBaseUnit(other.value);
        
        
        //checking for zero denominator
        if(Double.compare(otherBase, 0.0)==0) {
        	throw new IllegalArgumentException("Cannot divide by zero");
        }
        
        
        double result= thisBase/otherBase; //Divide thisBase / otherBase
        
        
        //Return the raw double result (no rounding needed for pure scalars)
        
        return result; // Replace with your logic
    }

    @Override
    public String toString() {
        // e.g., "12.00 INCHES" or "1.00 KILOGRAM"
        return String.format("%.2f %s", this.value, ((Enum<?>) this.unit).name());
    }
}