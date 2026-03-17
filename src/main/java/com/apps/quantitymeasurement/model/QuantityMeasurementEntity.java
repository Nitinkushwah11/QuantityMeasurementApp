package com.apps.quantitymeasurement.model;

import java.io.Serializable;
import com.app.quantitymeasurementapp.units.IMeasurable;

/**
 * A comprehensive data holder for all aspects of a quantity measurement operation,
 * including operands, the operation type, the result, and error handling.
 *Designed to be serialized and stored  in the repository.
 */
public class QuantityMeasurementEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // First Operand
    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    // Second Operand
    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    // Operation Details
    public String operation;

    // Result Details
    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

    // Error Tracking
    public String errorMessage;
    public boolean isError;

    // Base Constructor
    public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity, String operation) {
        if (thisQuantity != null) {
            this.thisValue = thisQuantity.value;
            this.thisUnit = thisQuantity.unit.getUnitName();
            this.thisMeasurementType = thisQuantity.unit.getMeasurementType();
        }
        if (thatQuantity != null && thatQuantity.unit != null) {
            this.thatValue = thatQuantity.value;
            this.thatUnit = thatQuantity.unit.getUnitName();
            this.thatMeasurementType = thatQuantity.unit.getMeasurementType();
        }
        this.operation = operation;
    }

    // Constructor for Successful Operations (Addition, Subtraction, Conversion)
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity, 
            QuantityModel<IMeasurable> thatQuantity, 
            String operation, 
            QuantityModel<IMeasurable> result) {
        
        this(thisQuantity, thatQuantity, operation);
        
        if (result != null && result.unit != null) {
            this.resultValue = result.value;
            this.resultUnit = result.unit.getUnitName();
            this.resultMeasurementType = result.unit.getMeasurementType();
        }
        this.isError = false;
    }

    // Constructor for Division Operations (Returns a Double ratio, no unit)
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity, 
            QuantityModel<IMeasurable> thatQuantity, 
            String operation, 
            double resultValue) {
        
        this(thisQuantity, thatQuantity, operation);
        this.resultValue = resultValue;
        this.resultUnit = "RATIO";
        this.resultMeasurementType = "N/A";
        this.isError = false;
    }

    /**
     * To record Error while performing Operations
     */
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation, 
            String errorMessage, 
            boolean isError) {
        
        this(thisQuantity, thatQuantity, operation);
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    @Override
    public String toString() {
        if (isError) {
            return "Operation: " + operation + " | ERROR: " + errorMessage;
        }
        return "Operation: " + operation + " | Result: " + resultValue + " " + resultUnit;
    }
}