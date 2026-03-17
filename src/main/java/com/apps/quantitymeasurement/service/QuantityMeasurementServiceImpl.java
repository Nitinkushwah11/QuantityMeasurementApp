package com.apps.quantitymeasurement.service;

import java.util.List;

import  com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import  com.apps.quantitymeasurement.model.QuantityDTO;
import  com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import  com.apps.quantitymeasurement.model.QuantityModel;
import  com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import  com.apps.quantitymeasurement.units.IMeasurable;
import  com.apps.quantitymeasurement.units.LengthUnit;
import  com.apps.quantitymeasurement.units.TemperatureUnit;
import  com.apps.quantitymeasurement.units.VolumeUnit;
import  com.apps.quantitymeasurement.units.WeightUnit;

/**
 * Implementation of IQuantityMeasurementService.
 * Handles measurement operations, adheres to SRP and OCP, and tracks auditing history.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    // Dependency Injection
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    /**
     * Helper: Translates "dumb" DTO strings into "smart" Java Enums based on type.
     * @throws QuantityMeasurementException 
     */
    private IMeasurable getUnitEnum(String measurementType, String unitName) throws QuantityMeasurementException {
        try {
            switch (measurementType.toUpperCase()) {
                case "LENGTHUNIT": return LengthUnit.FEET.getUnitInstance(unitName);
                case "WEIGHTUNIT": return WeightUnit.KILOGRAM.getUnitInstance(unitName);
                case "VOLUMEUNIT": return VolumeUnit.LITER.getUnitInstance(unitName);
                case "TEMPERATUREUNIT": return TemperatureUnit.CELSIUS.getUnitInstance(unitName);
                default: throw new IllegalArgumentException("Unknown measurement type: " + measurementType);
            }
        } catch (Exception e) {
            throw new QuantityMeasurementException("Failed to resolve unit: " + unitName, e);
        }
    }

    /**
     * Helper: Maps a raw DTO into the internal QuantityModel data carrier.
     * @throws QuantityMeasurementException 
     */
    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) throws QuantityMeasurementException {
        IMeasurable unit = getUnitEnum(dto.measurementType, dto.unit);
        return new QuantityModel<>(dto.value, unit);
    }

    // ==========================================
    // --- BUSINESS LOGIC OPERATIONS ---
    // ==========================================

    @Override
    public QuantityDTO convert(QuantityDTO dto, String targetUnitName) throws QuantityMeasurementException {
        try {
            QuantityModel<IMeasurable> sourceModel = convertDtoToModel(dto);
            IMeasurable targetUnit = getUnitEnum(dto.measurementType, targetUnitName);

            // 1. Do the Math natively in the Service
            double baseValue = sourceModel.unit.convertToBaseUnit(sourceModel.value);
            double targetValue = targetUnit.convertFromBaseUnit(baseValue);
            double roundedTargetValue = Math.round(targetValue * 100.0) / 100.0;
            
            // 2. Package into Model & DTO
            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(roundedTargetValue, targetUnit);
            QuantityDTO resultDto = new QuantityDTO(roundedTargetValue, targetUnitName, dto.measurementType);

            // 3. Save the Receipt
            QuantityMeasurementEntity receipt = new QuantityMeasurementEntity(
                    sourceModel, null, "CONVERSION", resultModel);
            repository.save(receipt);

            return resultDto;

        } catch (Exception e) {
            saveErrorReceipt(dto, null, "CONVERSION", e.getMessage());
            throw new QuantityMeasurementException("Conversion failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean compare(QuantityDTO dto1, QuantityDTO dto2) throws QuantityMeasurementException {
        try {
            QuantityModel<IMeasurable> m1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(dto2);

            // Validate Cross-Category
            if (!m1.unit.getClass().equals(m2.unit.getClass())) {
                return false;
            }

            // Do the Math
            double baseVal1 = Math.round(m1.unit.convertToBaseUnit(m1.value) * 100.0) / 100.0;
            double baseVal2 = Math.round(m2.unit.convertToBaseUnit(m2.value) * 100.0) / 100.0;
            boolean isEqual = Double.compare(baseVal1, baseVal2) == 0;

            // Save the Receipt
            QuantityMeasurementEntity receipt = new QuantityMeasurementEntity(m1, m2, "COMPARISON");
            receipt.resultUnit = String.valueOf(isEqual);
            repository.save(receipt);

            return isEqual;

        } catch (Exception e) {
            saveErrorReceipt(dto1, dto2, "COMPARISON", e.getMessage());
            throw new QuantityMeasurementException("Comparison failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO dto1, QuantityDTO dto2, String targetUnitName) throws QuantityMeasurementException {
        try {
            QuantityModel<IMeasurable> m1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(dto2);
            IMeasurable targetUnit = getUnitEnum(dto1.measurementType, targetUnitName);

            // Validation
            m1.unit.validateOperationSupport("ADDITION");
            m2.unit.validateOperationSupport("ADDITION");
            if (!m1.unit.getClass().equals(m2.unit.getClass())) {
                throw new IllegalArgumentException("Cannot add cross-category units.");
            }

            // Do the Math
            double baseVal1 = m1.unit.convertToBaseUnit(m1.value);
            double baseVal2 = m2.unit.convertToBaseUnit(m2.value);
            double targetVal = targetUnit.convertFromBaseUnit(baseVal1 + baseVal2);
            double roundedTargetVal = Math.round(targetVal * 100.0) / 100.0;

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(roundedTargetVal, targetUnit);
            QuantityDTO resultDto = new QuantityDTO(roundedTargetVal, targetUnitName, dto1.measurementType);

            QuantityMeasurementEntity receipt = new QuantityMeasurementEntity(m1, m2, "ADDITION", resultModel);
            repository.save(receipt);

            return resultDto;
        } catch (Exception e) {
            saveErrorReceipt(dto1, dto2, "ADDITION", e.getMessage());
            throw new QuantityMeasurementException("Addition failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO dto1, QuantityDTO dto2, String targetUnitName) throws QuantityMeasurementException {
        try {
            QuantityModel<IMeasurable> m1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(dto2);
            IMeasurable targetUnit = getUnitEnum(dto1.measurementType, targetUnitName);

            // Validation
            m1.unit.validateOperationSupport("SUBTRACTION");
            m2.unit.validateOperationSupport("SUBTRACTION");
            if (!m1.unit.getClass().equals(m2.unit.getClass())) {
                throw new IllegalArgumentException("Cannot subtract cross-category units.");
            }

            // Do the Math
            double baseVal1 = m1.unit.convertToBaseUnit(m1.value);
            double baseVal2 = m2.unit.convertToBaseUnit(m2.value);
            double targetVal = targetUnit.convertFromBaseUnit(baseVal1 - baseVal2);
            double roundedTargetVal = Math.round(targetVal * 100.0) / 100.0;

            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(roundedTargetVal, targetUnit);
            QuantityDTO resultDto = new QuantityDTO(roundedTargetVal, targetUnitName, dto1.measurementType);

            QuantityMeasurementEntity receipt = new QuantityMeasurementEntity(m1, m2, "SUBTRACTION", resultModel);
            repository.save(receipt);

            return resultDto;
        } catch (Exception e) {
            saveErrorReceipt(dto1, dto2, "SUBTRACTION", e.getMessage());
            throw new QuantityMeasurementException("Subtraction failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO dto1, QuantityDTO dto2) throws QuantityMeasurementException {
        try {
            QuantityModel<IMeasurable> m1 = convertDtoToModel(dto1);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(dto2);

            // Validation
            m1.unit.validateOperationSupport("DIVISION");
            m2.unit.validateOperationSupport("DIVISION");
            if (!m1.unit.getClass().equals(m2.unit.getClass())) {
                throw new IllegalArgumentException("Cannot divide cross-category units.");
            }

            // Do the Math
            double baseVal1 = m1.unit.convertToBaseUnit(m1.value);
            double baseVal2 = m2.unit.convertToBaseUnit(m2.value);
            
            if (baseVal2 == 0.0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            double ratio = baseVal1 / baseVal2;
            
            QuantityDTO resultDto = new QuantityDTO(ratio, "RATIO", "N/A");

            QuantityMeasurementEntity receipt = new QuantityMeasurementEntity(m1, m2, "DIVISION", ratio);
            repository.save(receipt);

            return resultDto;
        } catch (Exception e) {
            saveErrorReceipt(dto1, dto2, "DIVISION", e.getMessage());
            throw new QuantityMeasurementException("Division failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getHistory() {
        return repository.getAllMeasurements();
    }

    /**
     * Helper to cleanly save an error to the database without throwing another exception.
     */
    private void saveErrorReceipt(QuantityDTO dto1, QuantityDTO dto2, String operation, String errorMessage) {
        QuantityModel<IMeasurable> qm1 = null;
        QuantityModel<IMeasurable> qm2 = null;
        
        try { if (dto1 != null) qm1 = convertDtoToModel(dto1); } catch (Exception ignored) {}
        try { if (dto2 != null) qm2 = convertDtoToModel(dto2); } catch (Exception ignored) {}

        repository.save(new QuantityMeasurementEntity(qm1, qm2, operation, errorMessage, true));
    }
}