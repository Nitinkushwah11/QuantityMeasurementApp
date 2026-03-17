package com.apps.quantitymeasurement.controller;

import java.util.List;

import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;

/**
 * The Controller Layer.
 * This acts as the entry point of the application, taking requests from the 
 * outside world (using DTOs) and passing them to the Service Layer.
 */
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    // Dependency Injection
    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    public void demonstrateApp() {
        System.out.println("==================================================");
        System.out.println("   QUANTITY MEASUREMENT APP - N-TIER EXECUTION    ");
        System.out.println("==================================================\n");

        try {
            // 1. CONVERSION TEST
            QuantityDTO convertInput = new QuantityDTO(1.0, "FEET", "LengthUnit");
            System.out.println(">> Requesting Conversion: 1.0 FEET to INCHES...");
            QuantityDTO convertResult = service.convert(convertInput, "INCHES");
            System.out.println("   Result: " + convertResult.value + " " + convertResult.unit + "\n");

            // 2. ADDITION TEST
            QuantityDTO addInput1 = new QuantityDTO(2.0, "INCHES", "LengthUnit");
            QuantityDTO addInput2 = new QuantityDTO(2.0, "INCHES", "LengthUnit");
            System.out.println(">> Requesting Addition: 2.0 INCHES + 2.0 INCHES (Target: CENTIMETERS)...");
            QuantityDTO addResult = service.add(addInput1, addInput2, "CENTIMETERS");
            System.out.println("   Result: " + addResult.value + " " + addResult.unit + "\n");

            // 3. DIVISION TEST
            QuantityDTO divInput1 = new QuantityDTO(10.0, "GALLON", "VolumeUnit");
            QuantityDTO divInput2 = new QuantityDTO(5.0, "GALLON", "VolumeUnit");
            System.out.println(">> Requesting Division: 10.0 GALLONS / 5.0 GALLONS...");
            QuantityDTO divResult = service.divide(divInput1, divInput2);
            System.out.println("   Result Ratio: " + divResult.value + "\n");

            // 4. TEMPERATURE MATH FAILURE TEST (Proving validation works!)
            System.out.println(">> Requesting Invalid Math (Adding Temperatures)...");
            QuantityDTO temp1 = new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit");
            QuantityDTO temp2 = new QuantityDTO(50.0, "CELSIUS", "TemperatureUnit");
            service.add(temp1, temp2, "FAHRENHEIT");

        } catch (Exception e) {
            System.err.println("   [EXPECTED ERROR CAUGHT]: " + e.getMessage() + "\n");
        }

        // 5. VIEW DATABASE HISTORY
        System.out.println("==================================================");
        System.out.println("          AUDIT HISTORY (FROM DATABASE)           ");
        System.out.println("==================================================");
        List<QuantityMeasurementEntity> history = service.getHistory();
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i).toString());
        }
    }

    /**
     * MAIN ENTRY POINT
     * Wires the application together and runs the Controller.
     */
    public static void main(String[] args) {
        // 1. Initialize the Database (Repository)
        IQuantityMeasurementRepository repository = new QuantityMeasurementCacheRepository();

        // 2. Initialize the Business Logic (Service), injecting the repository
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);

        // 3. Initialize the UI (Controller), injecting the service
        QuantityMeasurementController controller = new QuantityMeasurementController(service);

        // 4. Run the App!
        controller.demonstrateApp();
    }
}