package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.entity.QuantityModel;
import com.app.quantitymeasurement.unit.IMeasurable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * UC 17 Enhancement:
 * Refactoring QuantityMeasurementEntity - Refactor the QuantityMeasurementEntity class
 * to improve its design and maintainability. This includes adding JPA annotations for
 * database mapping, implementing constructors for different use cases, and ensuring that
 * the class is properly annotated with Lombok to reduce boilerplate code.
 */
@Entity
@Table(name = "quantity_measurement_entity", indexes = {
    @Index(name = "idx_operation", columnList = "operation"),
    @Index(name = "idx_measurement_type", columnList = "this_measurement_type"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "this_value", nullable = false)
    public double thisValue;

    @Column(name = "this_unit", nullable = false)
    public String thisUnit;

    @Column(name = "this_measurement_type", nullable = false)
    public String thisMeasurementType;

    @Column(name = "that_value")
    public double thatValue;

    @Column(name = "that_unit")
    public String thatUnit;

    @Column(name = "that_measurement_type")
    public String thatMeasurementType;

    // e.g., "COMPARE", "CONVERT", "ADD", "SUBTRACT", "DIVIDE"
    @Column(name = "operation", nullable = false)
    public String operation;

    @Column(name = "result_value")
    public double resultValue;

    @Column(name = "result_unit")
    public String resultUnit;

    @Column(name = "result_measurement_type")
    public String resultMeasurementType;

    // For comparison results like "Equal" or "Not Equal"
    @Column(name = "result_string")
    public String resultString;

    // Flag to indicate if an error occurred during the operation
    @Column(name = "is_error")
    public boolean isError;

    // For capturing any error messages during operations
    @Column(name = "error_message")
    public String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor for single operand operation (e.g., comparison and conversion).
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, String operation, QuantityModel<?> result) {
        this.thisValue = thisQuantity.value;
        this.thisUnit = thisQuantity.unit.toString();
        this.thisMeasurementType = thisQuantity.unit.getClass().getSimpleName();
        this.operation = operation;
        this.resultValue = result.value;
        this.resultUnit = result.unit.toString();
        this.resultMeasurementType = result.unit.getClass().getSimpleName();
    }

    /**
     * Constructor for double operand operation (e.g., addition, subtraction, division).
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity, String operation, QuantityModel<?> result) {
        this(thisQuantity, operation, result);
        this.thatValue = thatQuantity.value;
        this.thatUnit = thatQuantity.unit.toString();
        this.thatMeasurementType = thatQuantity.unit.getClass().getSimpleName();
    }

    /**
     * Constructor for comparison results.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity, String operation, String resultString) {
        this.thisValue = thisQuantity.value;
        this.thisUnit = thisQuantity.unit.toString();
        this.thisMeasurementType = thisQuantity.unit.getClass().getSimpleName();
        this.thatValue = thatQuantity.value;
        this.thatUnit = thatQuantity.unit.toString();
        this.thatMeasurementType = thatQuantity.unit.getClass().getSimpleName();
        this.operation = operation;
        this.resultString = resultString;
    }
    
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
    
    public QuantityMeasurementEntity(QuantityModel<IMeasurable> thisQuantity, QuantityModel<IMeasurable> thatQuantity, String operation, double resultValue) {
        this(thisQuantity, thatQuantity, operation);
        this.resultValue = resultValue;
        this.resultUnit = "RATIO";
        this.resultMeasurementType = "N/A";
        this.isError = false;
    }

    /**
     * Constructor for error cases.
     */
    public QuantityMeasurementEntity(QuantityModel<?> thisQuantity, QuantityModel<?> thatQuantity, String operation, String errorMessage, boolean isError) {
        if (thisQuantity != null) {
            this.thisValue = thisQuantity.value;
            this.thisUnit = thisQuantity.unit.toString();
        }
        if (thatQuantity != null) {
            this.thatValue = thatQuantity.value;
            this.thatUnit = thatQuantity.unit.toString();
        }
        this.operation = operation;
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

	
}