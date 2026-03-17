package com.app.quantitymeasurementapp.controller;

import java.util.logging.Logger;
import com.app.quantitymeasurementapp.entity.QuantityDTO;
import com.app.quantitymeasurementapp.service.IQuantityMeasurementService;

/**
 * Controller Layer for the Quantity Measurement Application.
 * Handles request routing and delegates business logic to the Service layer.
 */
public class QuantityMeasurementController {

    // Logger for logging information and errors in the controller
    private static final Logger logger = Logger.getLogger(QuantityMeasurementController.class.getName());

    private final IQuantityMeasurementService quantityMeasurementService;

    /**
     * Constructor using Dependency Injection for the Service.
     * @param quantityMeasurementService implementation of the service interface
     */
    public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
        this.quantityMeasurementService = quantityMeasurementService;
        logger.info("QuantityMeasurementController initialized with service: " + 
                    quantityMeasurementService.getClass().getSimpleName());
    }

    /**
     * Routes a comparison request to the service layer.
     */
    public boolean performComparison(QuantityDTO q1, QuantityDTO q2) {
        logger.info("Controller: Comparison requested for " + q1 + " and " + q2);
        return quantityMeasurementService.compare(q1, q2);
    }

    /**
     * Routes an addition request to the service layer.
     */
    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        logger.info("Controller: Addition requested for " + q1 + " + " + q2 + " to " + targetUnit);
        return quantityMeasurementService.add(q1, q2, targetUnit);
    }

    /**
     * Routes a conversion request to the service layer.
     */
    public QuantityDTO performConversion(QuantityDTO source, String targetUnit) {
        logger.info("Controller: Conversion requested for " + source + " to " + targetUnit);
        return quantityMeasurementService.convert(source, targetUnit);
    }

    /**
     * Demonstrates the application flow with various operations.
     * Useful for manual testing and verification of the audit history.
     */
    public void demonstrateApp() {
        logger.info("Starting Application Demonstration...");

        try {
            // 1. Comparison Test
            QuantityDTO feet = new QuantityDTO(1.0, "FEET", "LengthUnit");
            QuantityDTO inches = new QuantityDTO(12.0, "INCHES", "LengthUnit");
            boolean isEqual = performComparison(feet, inches);
            logger.info("Comparison (1 ft == 12 in): " + isEqual);

            // 2. Addition Test
            QuantityDTO inch1 = new QuantityDTO(2.0, "INCHES", "LengthUnit");
            QuantityDTO result = performAddition(inch1, inch1, "CENTIMETERS");
            logger.info("Addition (2 in + 2 in): " + result.value + " " + result.unit);

            // 3. Expected Error Test (Cross-category addition)
            logger.info("Testing Expected Error (Feet + Kilograms)...");
            QuantityDTO weight = new QuantityDTO(1.0, "KILOGRAM", "WeightUnit");
            performAddition(feet, weight, "FEET");

        } catch (Exception e) {
            logger.warning("Demonstration gracefully caught expected logic error: " + e.getMessage());
        }

        logger.info("Demonstration Complete. Check Audit History for details.");
    }
}
