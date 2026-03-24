package com.app.quantitymeasurement.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object representing a single quantity measurement.
 * We rely on the Service layer to validate the exact Enum match, 
 * so we only enforce that the fields are not empty here.
 */
@Data
public class QuantityDTO {

    @NotNull(message = "Value cannot be null")
    public Double value;

    @NotBlank(message = "Unit cannot be empty (e.g., FEET, GALLON, LITER)")
    public String unit;

    @NotBlank(message = "Measurement type cannot be empty (e.g., LengthUnit, VolumeUnit)")
    public String measurementType;
}