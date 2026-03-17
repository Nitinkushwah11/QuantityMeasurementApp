package com.apps.quantitymeasurement.units;

public interface IMeasurable {
	 double convertToBaseUnit(double value);
	    double convertFromBaseUnit(double value);

	    // UC14: Default Lambda Expression mapping to TRUE
	    SupportsArithmetic supportsArithmetic = () -> true;

	    default boolean supportsArithmetic() {
	        return supportsArithmetic.isSupported();
	    }

	    default void validateOperationSupport(String operationName) {
	        if (!this.supportsArithmetic()) {
	            throw new UnsupportedOperationException(
	                "Arithmetic operations (" + operationName + ") are not supported for " + this.getClass().getSimpleName()
	            );
	        }
	    }

	    // --- UC15 NEW METHODS ---
	    
	    /**
	     * Get the name of this unit (e.g., "FEET", "CELSIUS").
	     */
	    public String getUnitName();
	    
	    /**
	     * Get the Measurement Type (e.g., LengthUnit, WeightUnit) for this unit.
	     * Essential for ensuring comparisons and conversions are between compatible types.
	     */
	    public String getMeasurementType();

	    /**
	     * Given the unit name, return the IMeasurable unit instance.
	     * Essential for converting QuantityDTO to IMeasurable units.
	     */
	    public IMeasurable getUnitInstance(String unitName);
}