package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UC 17 Enhancement:
 * Spring Data JPA Repository for QuantityMeasurementEntity.
 * Extends JpaRepository to inherit standard CRUD operations.
 * Defines custom query methods based on naming conventions and JPQL.
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    // Find all measurements by operation type (e.g., "ADD", "CONVERT")
    List<QuantityMeasurementEntity> findByOperation(String operation);

    // Find all measurements by measurement type (e.g., "LengthUnit", "VolumeUnit")
    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    // Find measurements created after a specific date
    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

    // Custom JPQL query to find only successful operations of a specific type
    @Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.operation = :operation AND e.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulOperations(@Param("operation") String operation);

    // Automatically generates a COUNT query for successful operations
    long countByOperationAndIsErrorFalse(String operation);

    // Find all records where an error occurred
    List<QuantityMeasurementEntity> findByIsErrorTrue();
}