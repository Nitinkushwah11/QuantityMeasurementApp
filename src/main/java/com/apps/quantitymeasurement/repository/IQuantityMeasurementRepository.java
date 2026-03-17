package com.apps.quantitymeasurement.repository;

import java.util.List;
import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {
    
    /**
     * Saves a QuantityMeasurementEntity to the repository.
     *
     * @param entity the QuantityMeasurementEntity to be saved
     * @return void
     */
    void save(QuantityMeasurementEntity entity);

    /**
     * Retrieves all QuantityMeasurementEntity instances from the repository.
     * @return a list of all QuantityMeasurementEntity instances
     */
    List<QuantityMeasurementEntity> getAllMeasurements();

    // Main method for testing purposes
    public static void main(String[] args) {
        System.out.println("Testing IQuantityMeasurementRepository interface");
    }
}