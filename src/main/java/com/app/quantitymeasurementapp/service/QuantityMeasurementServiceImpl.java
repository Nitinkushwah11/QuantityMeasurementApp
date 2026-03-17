package com.app.quantitymeasurementapp.service;

import java.util.List;
import java.util.logging.Logger;

import com.app.quantitymeasurementapp.entity.QuantityDTO;
import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.entity.QuantityModel;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.app.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurementapp.unit.*;

/**
 * Implementation of the Service Layer.
 * Orchestrates conversions, arithmetic, and persistence logic.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = Logger.getLogger(QuantityMeasurementServiceImpl.class.getName());
    private final IQuantityMeasurementRepository repository;

    // Constructor Injection for the Repository
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("Service Layer initialized with repository: " + repository.getClass().getSimpleName());
    }

    /**
     * Factory Helper: Resolves a String (e.g., "FEET") into an actual Enum object.
     */
    private IMeasurable getUnitEnum(String type, String unitName) {
        try {
            return switch (type.toUpperCase()) {
                case "LENGTHUNIT" -> LengthUnit.FEET.getUnitInstance(unitName);
                case "WEIGHTUNIT" -> WeightUnit.KILOGRAM.getUnitInstance(unitName);
                case "VOLUMEUNIT" -> VolumeUnit.LITER.getUnitInstance(unitName);
                case "TEMPERATUREUNIT" -> TemperatureUnit.CELSIUS.getUnitInstance(unitName);
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        } catch (Exception e) {
            throw new QuantityMeasurementException("Failed to resolve unit: " + unitName, e);
        }
    }

    /**
     * Helper: Wraps raw DTO values into our domain QuantityModel.
     */
    private QuantityModel<IMeasurable> toModel(QuantityDTO dto) {
        return new QuantityModel<>(dto.value, getUnitEnum(dto.measurementType, dto.unit));
    }

    @Override
    public QuantityDTO convert(QuantityDTO dto, String targetUnitName) {
        try {
            QuantityModel<IMeasurable> source = toModel(dto);
            IMeasurable targetUnit = getUnitEnum(dto.measurementType, targetUnitName);

            double baseValue = source.unit.convertToBaseUnit(source.value);
            double targetValue = targetUnit.convertFromBaseUnit(baseValue);
            double rounded = Math.round(targetValue * 100.0) / 100.0;

            QuantityModel<IMeasurable> result = new QuantityModel<>(rounded, targetUnit);
            repository.save(new QuantityMeasurementEntity(source, null, "CONVERSION", result));
            
            return new QuantityDTO(rounded, targetUnitName, dto.measurementType);
        } catch (Exception e) {
            logger.severe("Conversion Error: " + e.getMessage());
            repository.save(new QuantityMeasurementEntity(null, null, "CONVERSION", e.getMessage(), true));
            throw new QuantityMeasurementException("Conversion failed", e);
        }
    }

    @Override
    public boolean compare(QuantityDTO dto1, QuantityDTO dto2) {
        try {
            QuantityModel<IMeasurable> m1 = toModel(dto1);
            QuantityModel<IMeasurable> m2 = toModel(dto2);

            double base1 = Math.round(m1.unit.convertToBaseUnit(m1.value) * 100.0) / 100.0;
            double base2 = Math.round(m2.unit.convertToBaseUnit(m2.value) * 100.0) / 100.0;
            
            boolean isEqual = Double.compare(base1, base2) == 0;

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(m1, m2, "COMPARISON");
            entity.resultUnit = String.valueOf(isEqual);
            repository.save(entity);

            return isEqual;
        } catch (Exception e) {
            logger.severe("Comparison Error: " + e.getMessage());
            throw new QuantityMeasurementException("Comparison failed", e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO d1, QuantityDTO d2, String targetUnitName) {
        try {
            QuantityModel<IMeasurable> m1 = toModel(d1);
            QuantityModel<IMeasurable> m2 = toModel(d2);
            IMeasurable target = getUnitEnum(d1.measurementType, targetUnitName);

            m1.unit.validateOperationSupport("ADDITION");

            double sumBase = m1.unit.convertToBaseUnit(m1.value) + m2.unit.convertToBaseUnit(m2.value);
            double targetValue = Math.round(target.convertFromBaseUnit(sumBase) * 100.0) / 100.0;

            repository.save(new QuantityMeasurementEntity(m1, m2, "ADDITION", new QuantityModel<>(targetValue, target)));
            return new QuantityDTO(targetValue, targetUnitName, d1.measurementType);
        } catch (Exception e) {
            logger.severe("Addition Error: " + e.getMessage());
            throw new QuantityMeasurementException("Addition failed", e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO d1, QuantityDTO d2, String targetUnitName) {
        try {
            QuantityModel<IMeasurable> m1 = toModel(d1);
            QuantityModel<IMeasurable> m2 = toModel(d2);
            IMeasurable target = getUnitEnum(d1.measurementType, targetUnitName);

            m1.unit.validateOperationSupport("SUBTRACTION");

            double diffBase = m1.unit.convertToBaseUnit(m1.value) - m2.unit.convertToBaseUnit(m2.value);
            double targetValue = Math.round(target.convertFromBaseUnit(diffBase) * 100.0) / 100.0;

            repository.save(new QuantityMeasurementEntity(m1, m2, "SUBTRACTION", new QuantityModel<>(targetValue, target)));
            return new QuantityDTO(targetValue, targetUnitName, d1.measurementType);
        } catch (Exception e) {
            logger.severe("Subtraction Error: " + e.getMessage());
            throw new QuantityMeasurementException("Subtraction failed", e);
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO d1, QuantityDTO d2) {
        try {
            QuantityModel<IMeasurable> m1 = toModel(d1);
            QuantityModel<IMeasurable> m2 = toModel(d2);

            m1.unit.validateOperationSupport("DIVISION");
            
            double b1 = m1.unit.convertToBaseUnit(m1.value);
            double b2 = m2.unit.convertToBaseUnit(m2.value);
            
            if (b2 == 0) throw new ArithmeticException("Division by zero");
            double ratio = b1 / b2;

            repository.save(new QuantityMeasurementEntity(m1, m2, "DIVISION", ratio));
            return new QuantityDTO(ratio, "RATIO", "N/A");
        } catch (Exception e) {
            logger.severe("Division Error: " + e.getMessage());
            throw new QuantityMeasurementException("Division failed", e);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getHistory() {
        return repository.getAllMeasurements();
    }
}
