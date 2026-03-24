package com.app.quantitymeasurement.service;

import java.util.List;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;

public interface IQuantityMeasurementService {

    /**
     * Compares two quantities and returns a DTO containing the result.
     */
    public QuantityMeasurementDTO compare(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO
    );

    /**
     * Converts a quantity to a target unit and returns the result DTO.
     */
    public QuantityMeasurementDTO convert(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO
    );

    /**
     * Adds two quantities and returns the result in the unit of the first operand.
     */
    public QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO
    );

    /**
     * Adds two quantities and converts the sum to a specific target unit.
     */
    public QuantityMeasurementDTO add(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO
    );

    /**
     * Subtracts the second quantity from the first.
     */
    public QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO
    );

    /**
     * Subtracts the second quantity from the first and converts to a target unit.
     */
    public QuantityMeasurementDTO subtract(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO
    );

    /**
     * Divides the first quantity by the second, returning a ratio.
     */
    public QuantityMeasurementDTO divide(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO
    );

    /**
     * Retrieve the history of quantity measurement operations for a specific operation type.
     *
     * @param operation the type of operation for which to retrieve the history (e.g., "conversion", "comparison")
     * @return a list of {@code QuantityMeasurementDTO} representing the history of operations for the specified type
     */
    List<QuantityMeasurementDTO> getOperationHistory(String operation);

    /**
     * Retrieve the history of quantity measurement operations for a specific measurement type.
     *
     * @param type the measurement type for which to retrieve the history (e.g., "length", "weight", "volume", "temperature")
     * @return a list of {@code QuantityMeasurementDTO} representing the history of operations for the specified type
     */
    List<QuantityMeasurementDTO> getMeasurementsByType(String type);

    /**
     * Get the count of quantity measurement operations for a specific operation type.
     *
     * @param operation the type of operation for which to count operations
     * @return the number of operations of the specified type
     */
    long getOperationCount(String operation);

    /**
     * Retrieve the history of quantity measurement operations that resulted in errors.
     *
     * @return a list of {@code QuantityMeasurementDTO} representing the history of operations that resulted in errors
     */
    List<QuantityMeasurementDTO> getErrorHistory();

    // Main method for testing purposes
    public static void main(String[] args) {
        System.out.println("IQuantityMeasurementService interface refactored for UC17.");
    }
}