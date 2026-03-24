package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.OperationType;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.QuantityModel;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

import java.util.List;
import java.util.logging.Logger;
import java.util.function.DoubleBinaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    // Logger for logging information and errors
    private static final Logger logger = Logger.getLogger(
            QuantityMeasurementServiceImpl.class.getName()
    );

    // Quantity Measurement Repository for storing and retrieving quantity data
    @Autowired
    private QuantityMeasurementRepository repository;

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        try {
            QuantityModel<IMeasurable> m1 = convertDtoToModel(thisQuantityDTO);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(thatQuantityDTO);

            // Logic check: Classes must match for comparison
            boolean isEqual = false;
            if (m1.unit.getClass().equals(m2.unit.getClass())) {
                double v1Base = Math.round(m1.unit.convertToBaseUnit(m1.value) * 100.0) / 100.0;
                double v2Base = Math.round(m2.unit.convertToBaseUnit(m2.value) * 100.0) / 100.0;
                isEqual = Double.compare(v1Base, v2Base) == 0;
            }

            String resultStr = isEqual ? "Equal" : "Not Equal";
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(m1, m2, OperationType.COMPARE.name(), resultStr);
            repository.save(entity);
            return QuantityMeasurementDTO.from(entity);

        } catch (Exception e) {
            return handleException(OperationType.COMPARE.name(), thisQuantityDTO, thatQuantityDTO, e);
        }
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        try {
            QuantityModel<IMeasurable> source = convertDtoToModel(thisQuantityDTO);
            IMeasurable targetUnit = getUnitEnum(thisQuantityDTO.measurementType, thatQuantityDTO.unit);
            
            double baseValue = source.unit.convertToBaseUnit(source.value);
            double targetValue = Math.round(targetUnit.convertFromBaseUnit(baseValue) * 100.0) / 100.0;
            
            QuantityModel<IMeasurable> resultModel = new QuantityModel<>(targetValue, targetUnit);
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(source, OperationType.CONVERT.name(), resultModel);
            repository.save(entity);
            return QuantityMeasurementDTO.from(entity);

        } catch (Exception e) {
            return handleException(OperationType.CONVERT.name(), thisQuantityDTO, null, e);
        }
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return this.add(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        return performArithmetic(thisQuantityDTO, thatQuantityDTO, targetUnitDTO, OperationType.ADD);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return this.subtract(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        return performArithmetic(thisQuantityDTO, thatQuantityDTO, targetUnitDTO, OperationType.SUBTRACT);
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return performArithmetic(thisQuantityDTO, thatQuantityDTO, null, OperationType.DIVIDE);
    }

    @Override
    public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
        List<QuantityMeasurementEntity> entities = repository.findByOperation(operation);
        return QuantityMeasurementDTO.fromList(entities);
    }

    @Override
    public List<QuantityMeasurementDTO> getMeasurementsByType(String type) {
        List<QuantityMeasurementEntity> entities = repository.findByThisMeasurementType(type);
        return QuantityMeasurementDTO.fromList(entities);
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation);
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        List<QuantityMeasurementEntity> entities = repository.findByIsErrorTrue();
        return QuantityMeasurementDTO.fromList(entities);
    }

    // --- Private Helper Methods ---

    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO quantity) {
        IMeasurable unit = getUnitEnum(quantity.measurementType, quantity.unit);
        return new QuantityModel<>(quantity.value, unit);
    }

    private IMeasurable getUnitEnum(String type, String unitName) {
        return switch (type) {
            case "LengthUnit" -> LengthUnit.FEET.getUnitInstance(unitName);
            case "WeightUnit" -> WeightUnit.KILOGRAM.getUnitInstance(unitName);
            case "VolumeUnit" -> VolumeUnit.LITER.getUnitInstance(unitName);
            case "TemperatureUnit" -> TemperatureUnit.CELSIUS.getUnitInstance(unitName);
            default -> throw new QuantityMeasurementException("Unknown measurement type: " + type);
        };
    }

    private QuantityMeasurementDTO performArithmetic(QuantityDTO d1, QuantityDTO d2, QuantityDTO target, OperationType opType) {
        try {
            QuantityModel<IMeasurable> m1 = convertDtoToModel(d1);
            QuantityModel<IMeasurable> m2 = convertDtoToModel(d2);
            
            validateArithmeticOperands(m1, m2.unit, opType.name());

            double base1 = m1.unit.convertToBaseUnit(m1.value);
            double base2 = m2.unit.convertToBaseUnit(m2.value);
            
            ArithmeticOperation op = ArithmeticOperation.valueOf(opType.name());
            double resultBase = op.compute(base1, base2);
            
            QuantityMeasurementEntity entity;
            if (target != null) {
                IMeasurable tUnit = getUnitEnum(target.measurementType, target.unit);
                double targetVal = Math.round(tUnit.convertFromBaseUnit(resultBase) * 100.0) / 100.0;
                QuantityModel<IMeasurable> resModel = new QuantityModel<>(targetVal, tUnit);
                entity = new QuantityMeasurementEntity(m1, m2, opType.name(), resModel);
            } else {
                // Division / Ratio case
                entity = new QuantityMeasurementEntity(m1, m2, opType.name(), resultBase);
            }
            
            repository.save(entity);
            return QuantityMeasurementDTO.from(entity);

        } catch (Exception e) {
            return handleException(opType.name(), d1, d2, e);
        }
    }

    private <U extends IMeasurable> void validateArithmeticOperands(QuantityModel<U> q1, IMeasurable unit2, String op) {
        q1.unit.validateOperationSupport(op);
        if (!q1.unit.getClass().equals(unit2.getClass())) {
            throw new QuantityMeasurementException("Cross-category arithmetic not supported: " + 
                q1.unit.getClass().getSimpleName() + " and " + unit2.getClass().getSimpleName());
        }
    }

    private QuantityMeasurementDTO handleException(String op, QuantityDTO d1, QuantityDTO d2, Exception e) {
        logger.severe(op + " failed: " + e.getMessage());
        QuantityModel<IMeasurable> m1 = (d1 != null) ? tryConvert(d1) : null;
        QuantityModel<IMeasurable> m2 = (d2 != null) ? tryConvert(d2) : null;
        
        QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(m1, m2, op, e.getMessage(), true);
        repository.save(errorEntity);
        return QuantityMeasurementDTO.from(errorEntity);
    }

    private QuantityModel<IMeasurable> tryConvert(QuantityDTO dto) {
        try { return convertDtoToModel(dto); } catch (Exception ex) { return null; }
    }

    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (b == 0) throw new ArithmeticException("Cannot divide by zero");
            return a / b;
        }),
        MULTIPLY((a, b) -> a * b);

        private final DoubleBinaryOperator operator;
        ArithmeticOperation(DoubleBinaryOperator operator) { this.operator = operator; }
        public double compute(double a, double b) { return operator.applyAsDouble(a, b); }
    }
}