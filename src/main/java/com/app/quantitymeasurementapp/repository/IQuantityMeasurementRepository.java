package com.app.quantitymeasurementapp.repository;

import java.util.List;

import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;

/**
 * Contract for storing and retrieving measurement audit logs.
 */
public interface IQuantityMeasurementRepository {
    
    // Core CRUD
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();
    
    // UC16 Search & Maintenance
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);
    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);
    int getTotalCount();
    void deleteAll();
    
    // Resource Management
    default String getPoolStatistics() {
        return "Pool statistics not available for this repository type.";
    }
    
    default void releaseResources() {
        // Default empty implementation
    }
}
