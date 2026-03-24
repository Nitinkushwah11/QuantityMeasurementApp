package com.app.quantitymeasurement.model;

import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * QuantityMeasurementDTO is a Data Transfer Object (DTO) class that serves as a
 * data carrier for quantity measurement operations out to the user.
 */
@Data
public class QuantityMeasurementDTO {

    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    public String operation;
    public String resultString;
    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

    // Explicitly mapping 'error' for JSON so Jackson doesn't get confused by boolean getters
    @JsonProperty("error")
    public boolean error;

    public String errorMessage;

    /**
     * Convert Entity (Database record) to DTO (API Response)
     */
    public static QuantityMeasurementDTO from(QuantityMeasurementEntity entity) {
        if (entity == null) return null;
        
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.thisValue = entity.thisValue;
        dto.thisUnit = entity.thisUnit;
        dto.thisMeasurementType = entity.thisMeasurementType;
        dto.thatValue = entity.thatValue;
        dto.thatUnit = entity.thatUnit;
        dto.thatMeasurementType = entity.thatMeasurementType;
        dto.operation = entity.operation;
        dto.resultString = entity.resultString;
        dto.resultValue = entity.resultValue;
        dto.resultUnit = entity.resultUnit;
        dto.resultMeasurementType = entity.resultMeasurementType;
        dto.error = entity.isError();
        dto.errorMessage = entity.errorMessage;
        
        return dto;
    }

    /**
     * Convert DTO to Entity
     */
    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.thisValue = this.thisValue;
        entity.thisUnit = this.thisUnit;
        entity.thisMeasurementType = this.thisMeasurementType;
        entity.thatValue = this.thatValue;
        entity.thatUnit = this.thatUnit;
        entity.thatMeasurementType = this.thatMeasurementType;
        entity.operation = this.operation;
        entity.resultString = this.resultString;
        entity.resultValue = this.resultValue;
        entity.resultUnit = this.resultUnit;
        entity.resultMeasurementType = this.resultMeasurementType;
        entity.setError(this.error);
        entity.errorMessage = this.errorMessage;
        
        return entity;
    }

    /**
     * Convert a whole List of Entities to a List of DTOs (Great for History endpoints)
     */
    public static List<QuantityMeasurementDTO> fromList(List<QuantityMeasurementEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(QuantityMeasurementDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Convert a whole List of DTOs to Entities
     */
    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(QuantityMeasurementDTO::toEntity)
                .collect(Collectors.toList());
    }
}