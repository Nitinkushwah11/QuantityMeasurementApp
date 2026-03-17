package com.app.quantitymeasurementapp.service;

import java.util.List;
import com.app.quantitymeasurementapp.entity.QuantityDTO;
import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;

/**
 * Contract for performing quantity measurement operations.
 * Acts as the bridge between the API (Controller) and Data (Repository).
 */
public interface IQuantityMeasurementService {
    
    QuantityDTO convert(QuantityDTO sourceDto, String targetUnitName) throws QuantityMeasurementException;
    
    boolean compare(QuantityDTO dto1, QuantityDTO dto2) throws QuantityMeasurementException;
    
    QuantityDTO add(QuantityDTO dto1, QuantityDTO dto2, String targetUnitName) throws QuantityMeasurementException;
    
    QuantityDTO subtract(QuantityDTO dto1, QuantityDTO dto2, String targetUnitName) throws QuantityMeasurementException;
    
    QuantityDTO divide(QuantityDTO dto1, QuantityDTO dto2) throws QuantityMeasurementException;
    
    List<QuantityMeasurementEntity> getHistory();
}