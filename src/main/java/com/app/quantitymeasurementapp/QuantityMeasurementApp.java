package com.app.quantitymeasurementapp;

import java.util.List;
import java.util.logging.Logger;

import com.app.quantitymeasurementapp.controller.QuantityMeasurementController;

import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurementapp.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurementapp.util.ApplicationConfig;

/**
 * The main entry point for the Quantity Measurement Application.
 * Responsible for wiring the layers together based on configuration
 * and managing the application lifecycle.
 */
public class QuantityMeasurementApp {

    private static final Logger logger = Logger.getLogger(QuantityMeasurementApp.class.getName());
    private static QuantityMeasurementApp instance;

    private IQuantityMeasurementRepository repository;
    public QuantityMeasurementController controller;

    /**
     * Singleton constructor that performs Dependency Injection.
     * It reads 'repository.type' from application.properties to decide
     * whether to use the SQL Database or the local Cache.
     */
    private QuantityMeasurementApp() {
        // Step 9: Determine repository type from configuration
        String repoType = ApplicationConfig.getInstance().getProperty("repository.type", "cache");
        
        if ("database".equalsIgnoreCase(repoType)) {
            this.repository = QuantityMeasurementDatabaseRepository.getInstance();
            logger.info("UC16: System initialized with DATABASE Repository.");
        } else {
            this.repository = QuantityMeasurementCacheRepository.getInstance();
            logger.info("UC16: System initialized with CACHE Repository.");
        }

        // Wire: Repository -> Service -> Controller
        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(this.repository);
        this.controller = new QuantityMeasurementController(service);
    }

    public static synchronized QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    /**
     * Safely releases database connections and pool resources.
     */
    public void closeResources() {
        if (this.repository != null) {
            this.repository.releaseResources();
            logger.info("Application resources closed gracefully.");
        }
    }

    /**
     * Utility to wipe the audit history (useful for clean demo runs).
     */
    public void deleteAllMeasurements() {
        if (this.repository != null) {
            this.repository.deleteAll();
            logger.info("Audit history cleared.");
        }
    }

    /**
     * Main method to execute the application.
     */
    public static void main(String[] args) {
        logger.info("--- Starting Quantity Measurement App (UC16) ---");
        
        QuantityMeasurementApp app = getInstance();
        
        // 1. Reset state for a clean demonstration
        app.deleteAllMeasurements();

        // 2. Run the Demonstration Logic via the Controller
        app.controller.demonstrateApp();

        // 3. Report Final Statistics from the Repository
        reportStats(app);

        // 4. Graceful Shutdown
        app.closeResources();
        logger.info("--- Application Shutdown Complete ---");
    }

    /**
     * Helper to print final database/cache status to the console.
     */
    private static void reportStats(QuantityMeasurementApp app) {
        List<QuantityMeasurementEntity> history = app.repository.getAllMeasurements();
        
        logger.info("=== FINAL AUDIT REPORT ===");
        history.forEach(entity -> logger.info(entity.toString()));
        
        logger.info("Total Records: " + app.repository.getTotalCount());
        logger.info("Final Resource Stats: " + app.repository.getPoolStatistics());
    }
}