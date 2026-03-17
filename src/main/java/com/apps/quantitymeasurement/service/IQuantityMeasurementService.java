package com.apps.quantitymeasurement.service;

import java.util.List;
import com.app.quantitymeasurementapp.model.QuantityDTO;
import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;

/**
 * Contract for performing quantity measurement operations.
 * Accepts input as QuantityDTO Objects and returns results as QuantityDTO Objects.
 */
public interface IQuantityMeasurementService {
    
    QuantityDTO convert(QuantityDTO sourceDto, String targetUnitName) throws QuantityMeasurementException;
    
    boolean compare(QuantityDTO dto1, QuantityDTO dto2) throws QuantityMeasurementException;
    
    QuantityDTO add(QuantityDTO dto1, QuantityDTO dto2, String targetUnitName) throws QuantityMeasurementException;
    
    QuantityDTO subtract(QuantityDTO dto1, QuantityDTO dto2, String targetUnitName) throws QuantityMeasurementException;
    
    QuantityDTO divide(QuantityDTO dto1, QuantityDTO dto2) throws QuantityMeasurementException;
    
    List<QuantityMeasurementEntity> getHistory();
}