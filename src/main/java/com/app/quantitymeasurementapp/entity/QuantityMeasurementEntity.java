package com.app.quantitymeasurementapp.entity;

import java.io.Serializable;
import com.app.quantitymeasurementapp.unit.IMeasurable;

/**
 * The Database Entity. Maps directly to the `quantity_measurement_entity` table
 * and represents a single operation (conversion, addition, error, etc.) for the audit log.
 */
public class QuantityMeasurementEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    public String operation;
    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

    public String errorMessage;
    public boolean isError;

    // Default constructor needed for reflections/serialization
    public QuantityMeasurementEntity() { }

    // Constructor 1: Base operation mapping
    public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity, String operation) {
        if (thisQuantity != null && thisQuantity.unit != null) {
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

    // Constructor 2: Standard successful operations (Addition, Conversion, etc.)
    public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity, String operation, QuantityModel<IMeasurable> result) {
        this(thisQuantity, thatQuantity, operation);
        if (result != null && result.unit != null) {
            this.resultValue = result.value;
            this.resultUnit = result.unit.getUnitName();
            this.resultMeasurementType = result.unit.getMeasurementType();
        }
        this.isError = false;
    }

    // Constructor 3: Pure double results (like Division ratios)
    public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity, String operation, double resultValue) {
        this(thisQuantity, thatQuantity, operation);
        this.resultValue = resultValue;
        this.resultUnit = "RATIO";
        this.resultMeasurementType = "N/A";
        this.isError = false;
    }

    // Constructor 4: Error Logging
    public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity, String operation, String errorMessage, boolean isError) {
        this(thisQuantity, thatQuantity, operation);
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    @Override
    public String toString() {
        if (isError) return "Operation: " + operation + " | ERROR: " + errorMessage;
        if ("RATIO".equals(resultUnit)) return "Operation: " + operation + " | Result Ratio: " + resultValue;
        return "Operation: " + operation + " | Result: " + resultValue + " " + resultUnit;
    }
}
