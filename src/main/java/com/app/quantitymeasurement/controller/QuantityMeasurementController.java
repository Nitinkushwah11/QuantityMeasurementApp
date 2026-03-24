package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UC 17 Enhancement:
 * Refactored into a Spring Boot @RestController. 
 * This class exposes all Quantity Measurement operations as RESTful APIs.
 */
@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "Endpoints for performing quantity measurement operations")
public class QuantityMeasurementController {

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities to see if they are mathematically equal")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to a different target unit")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities together (returns result in the first quantity's unit)")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-with-target-unit")
    @Operation(summary = "Add two quantities and convert the result to a specified target unit")
    public ResponseEntity<QuantityMeasurementDTO> addQuantitiesWithTarget(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO(), input.getTargetQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract the second quantity from the first")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract-with-target-unit")
    @Operation(summary = "Subtract the second quantity from the first and convert to a target unit")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantitiesWithTarget(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO(), input.getTargetQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide the first quantity by the second (returns a ratio)")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(@Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }

    // --- History & Audit Endpoints ---

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get a history of all calculations for a specific operation (e.g., ADD, CONVERT)")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(@PathVariable String operation) {
        return ResponseEntity.ok(service.getOperationHistory(operation));
    }

    @GetMapping("/history/type/{type}")
    @Operation(summary = "Get a history of all calculations for a specific measurement type (e.g., LengthUnit)")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getMeasurementsByType(type));
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get the total number of times a specific successful operation has occurred")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        return ResponseEntity.ok(service.getOperationCount(operation));
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get a history of all operations that failed or threw exceptions")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        return ResponseEntity.ok(service.getErrorHistory());
    }
}