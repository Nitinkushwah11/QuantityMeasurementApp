package com.app.quantitymeasurement.model;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * QuantityInputDTO acts as a wrapper for the incoming API requests.
 * It holds the primary quantity, the secondary quantity (for math operations), 
 * and an optional target unit for conversion-based additions/subtractions.
 */
@Data
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "thisQuantityDTO cannot be null")
    private QuantityDTO thisQuantityDTO;

    @Valid
    private QuantityDTO thatQuantityDTO;

    @Valid
    private QuantityDTO targetQuantityDTO;
}