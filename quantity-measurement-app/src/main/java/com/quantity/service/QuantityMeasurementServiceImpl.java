package com.quantity.service;

import com.quantity.core.*;
import com.quantity.dto.*;
import com.quantity.exception.QuantityMeasurementException;
import com.quantity.model.QuantityMeasurementEntity;
import com.quantity.repository.QuantityMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

	@Autowired
	private QuantityMeasurementRepository repository;

	// ===== Convert DTO -> Quantity =====
	private Quantity<?> convertDtoToModel(QuantityDTO dto) {
		try {
			return new Quantity<>(dto.getValue(), LengthUnit.valueOf(dto.getUnit()));
		} catch (Exception ignored) {
		}
		try {
			return new Quantity<>(dto.getValue(), WeightUnit.valueOf(dto.getUnit()));
		} catch (Exception ignored) {
		}
		try {
			return new Quantity<>(dto.getValue(), VolumeUnit.valueOf(dto.getUnit()));
		} catch (Exception ignored) {
		}
		try {
			return new Quantity<>(dto.getValue(), TemperatureUnit.valueOf(dto.getUnit()));
		} catch (Exception ignored) {
		}
		throw new QuantityMeasurementException("Unsupported Unit: " + dto.getUnit());
	}

	// ===== Get Measurement Type =====
	private String getMeasurementType(Quantity<?> quantity) {
		IMeasurable unit = quantity.getUnit();
		if (unit instanceof LengthUnit)
			return "LengthUnit";
		if (unit instanceof WeightUnit)
			return "WeightUnit";
		if (unit instanceof VolumeUnit)
			return "VolumeUnit";
		if (unit instanceof TemperatureUnit)
			return "TemperatureUnit";
		return "Unknown";
	}

	// ===== Build Base DTO =====
	private QuantityMeasurementDTO buildBaseDTO(QuantityInputDTO input, Quantity<?> q1, Quantity<?> q2,
			String operation) {
		QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
		dto.setThisValue(input.getThisQuantityDTO().getValue());
		dto.setThisUnit(input.getThisQuantityDTO().getUnit());
		dto.setThisMeasurementType(getMeasurementType(q1));
		dto.setThatValue(input.getThatQuantityDTO().getValue());
		dto.setThatUnit(input.getThatQuantityDTO().getUnit());
		dto.setThatMeasurementType(getMeasurementType(q2));
		dto.setOperation(operation);
		return dto;
	}

	// ===== Save =====
	private void saveToRepository(QuantityMeasurementDTO dto) {
		repository.save(dto.toEntity());
	}

	// ===== COMPARE =====
	@Override
	public QuantityMeasurementDTO compare(QuantityInputDTO input) {
		Quantity<?> q1 = convertDtoToModel(input.getThisQuantityDTO());
		Quantity<?> q2 = convertDtoToModel(input.getThatQuantityDTO());
		QuantityMeasurementDTO dto = buildBaseDTO(input, q1, q2, "compare");
		try {
			dto.setResultString(String.valueOf(q1.equals(q2)));
			dto.setError(false);
		} catch (Exception e) {
			dto.setErrorMessage(e.getMessage());
			dto.setError(true);
		}
		saveToRepository(dto);
		return dto;
	}

	// ===== CONVERT =====
	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO convert(QuantityInputDTO input) {
		Quantity<?> q1 = convertDtoToModel(input.getThisQuantityDTO());
		Quantity<?> q2 = convertDtoToModel(input.getThatQuantityDTO());
		QuantityMeasurementDTO dto = buildBaseDTO(input, q1, q2, "convert");
		try {
			IMeasurable unit = q1.getUnit();
			if (unit instanceof LengthUnit) {
				LengthUnit target = LengthUnit.valueOf(input.getThatQuantityDTO().getUnit());
				Quantity<LengthUnit> result = ((Quantity<LengthUnit>) q1).convertTo(target);
				dto.setResultValue(result.getValue());
				dto.setResultMeasurementType("LengthUnit");
			} else if (unit instanceof WeightUnit) {
				WeightUnit target = WeightUnit.valueOf(input.getThatQuantityDTO().getUnit());
				Quantity<WeightUnit> result = ((Quantity<WeightUnit>) q1).convertTo(target);
				dto.setResultValue(result.getValue());
				dto.setResultMeasurementType("WeightUnit");
			} else if (unit instanceof VolumeUnit) {
				VolumeUnit target = VolumeUnit.valueOf(input.getThatQuantityDTO().getUnit());
				Quantity<VolumeUnit> result = ((Quantity<VolumeUnit>) q1).convertTo(target);
				dto.setResultValue(result.getValue());
				dto.setResultMeasurementType("VolumeUnit");
			} else if (unit instanceof TemperatureUnit) {
				TemperatureUnit target = TemperatureUnit.valueOf(input.getThatQuantityDTO().getUnit());
				Quantity<TemperatureUnit> result = ((Quantity<TemperatureUnit>) q1).convertTo(target);
				dto.setResultValue(result.getValue());
				dto.setResultMeasurementType("TemperatureUnit");
			}
			dto.setError(false);
		} catch (Exception e) {
			dto.setErrorMessage(e.getMessage());
			dto.setError(true);
		}
		saveToRepository(dto);
		return dto;
	}

	// ===== ADD =====
	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO add(QuantityInputDTO input) {
		Quantity<?> q1 = convertDtoToModel(input.getThisQuantityDTO());
		Quantity<?> q2 = convertDtoToModel(input.getThatQuantityDTO());
		QuantityMeasurementDTO dto = buildBaseDTO(input, q1, q2, "add");
		try {
			if (!q1.getUnit().getClass().equals(q2.getUnit().getClass()))
				throw new QuantityMeasurementException(
						"Cannot perform arithmetic between different measurement categories: "
								+ getMeasurementType(q1) + " and " + getMeasurementType(q2));

			// use targetUnit if provided
			if (input.getTargetUnit() != null && !input.getTargetUnit().isEmpty()) {
				// first add in base unit
				Quantity addResult = ((Quantity) q1).add((Quantity) q2);
				double baseValue = addResult.getUnit().convertToBaseUnit(addResult.getValue());

				// then convert to target unit manually
				QuantityDTO targetDTO = new QuantityDTO(0.0, input.getTargetUnit(), input.getThisQuantityDTO().getMeasurementType());
				Quantity targetQty = (Quantity) convertDtoToModel(targetDTO);
				double convertedValue = targetQty.getUnit().convertFromBaseUnit(baseValue);

				dto.setResultValue(convertedValue);
				dto.setResultUnit(targetQty.getUnit().getUnitName());
			} else {
				Quantity result = ((Quantity) q1).add((Quantity) q2);
				dto.setResultValue(result.getValue());
				dto.setResultUnit(result.getUnit().getUnitName());
			}

			dto.setResultMeasurementType(getMeasurementType(q1));
			dto.setError(false);
		} catch (Exception e) {
			dto.setErrorMessage("add Error: " + e.getMessage());
			dto.setError(true);
		}
		saveToRepository(dto);
		return dto;
	}

	// ===== SUBTRACT =====
	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO subtract(QuantityInputDTO input) {
		Quantity<?> q1 = convertDtoToModel(input.getThisQuantityDTO());
		Quantity<?> q2 = convertDtoToModel(input.getThatQuantityDTO());
		QuantityMeasurementDTO dto = buildBaseDTO(input, q1, q2, "subtract");
		try {
			if (!q1.getUnit().getClass().equals(q2.getUnit().getClass()))
				throw new QuantityMeasurementException(
						"Cannot perform arithmetic between different measurement categories: "
								+ getMeasurementType(q1) + " and " + getMeasurementType(q2));

			if (input.getTargetUnit() != null && !input.getTargetUnit().isEmpty()) {
				Quantity<?> target = convertDtoToModel(new QuantityDTO(0.0, input.getTargetUnit(), input.getThisQuantityDTO().getMeasurementType()));
				Quantity result = ((Quantity) q1).subtract((Quantity) q2, target.getUnit());
				dto.setResultValue(result.getValue());
				dto.setResultUnit(result.getUnit().getUnitName());
			} else {
				Quantity result = ((Quantity) q1).subtract((Quantity) q2);
				dto.setResultValue(result.getValue());
				dto.setResultUnit(result.getUnit().getUnitName());
			}

			dto.setResultMeasurementType(getMeasurementType(q1));
			dto.setError(false);
		} catch (Exception e) {
			dto.setErrorMessage("subtract Error: " + e.getMessage());
			dto.setError(true);
		}
		saveToRepository(dto);
		return dto;
	}

	// ===== DIVIDE =====
	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO divide(QuantityInputDTO input) {
		Quantity<?> q1 = convertDtoToModel(input.getThisQuantityDTO());
		Quantity<?> q2 = convertDtoToModel(input.getThatQuantityDTO());
		QuantityMeasurementDTO dto = buildBaseDTO(input, q1, q2, "divide");
		try {
			if (!q1.getUnit().getClass().equals(q2.getUnit().getClass()))
				throw new QuantityMeasurementException(
						"Cannot perform arithmetic between different measurement categories: "
								+ getMeasurementType(q1) + " and " + getMeasurementType(q2));

			double result = ((Quantity) q1).divide((Quantity) q2);

			// convert result to target unit if provided
			if (input.getTargetUnit() != null && !input.getTargetUnit().isEmpty()) {
				QuantityDTO resultDTO = new QuantityDTO(result, input.getThisQuantityDTO().getUnit(), input.getThisQuantityDTO().getMeasurementType());
				QuantityDTO targetDTO = new QuantityDTO(result, input.getTargetUnit(), input.getThisQuantityDTO().getMeasurementType());
				Quantity resultQty = (Quantity) convertDtoToModel(resultDTO);
				Quantity targetQty = (Quantity) convertDtoToModel(targetDTO);
				Quantity converted = resultQty.convertTo(targetQty.getUnit());
				dto.setResultValue(converted.getValue());
				dto.setResultUnit(converted.getUnit().getUnitName());
			} else {
				dto.setResultValue(result);
				dto.setResultUnit(q1.getUnit().getUnitName());
			}

			dto.setResultMeasurementType(getMeasurementType(q1));
			dto.setError(false);
		} catch (ArithmeticException e) {
			dto.setErrorMessage("Divide by zero");
			dto.setError(true);
		} catch (Exception e) {
			dto.setErrorMessage("divide Error: " + e.getMessage());
			dto.setError(true);
		}
		saveToRepository(dto);
		return dto;
	}

	// ===== MULTIPLY =====
	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO multiply(QuantityInputDTO input) {
		Quantity<?> q1 = convertDtoToModel(input.getThisQuantityDTO());
		Quantity<?> q2 = convertDtoToModel(input.getThatQuantityDTO());
		QuantityMeasurementDTO dto = buildBaseDTO(input, q1, q2, "multiply");
		try {
			if (!q1.getUnit().getClass().equals(q2.getUnit().getClass()))
				throw new QuantityMeasurementException(
						"Cannot perform arithmetic between different measurement categories: "
								+ getMeasurementType(q1) + " and " + getMeasurementType(q2));

			double result = ((Quantity) q1).multiply((Quantity) q2);

			// convert result to target unit if provided
			if (input.getTargetUnit() != null && !input.getTargetUnit().isEmpty()) {
				QuantityDTO resultDTO = new QuantityDTO(result, input.getThisQuantityDTO().getUnit(), input.getThisQuantityDTO().getMeasurementType());
				QuantityDTO targetDTO = new QuantityDTO(result, input.getTargetUnit(), input.getThisQuantityDTO().getMeasurementType());
				Quantity resultQty = (Quantity) convertDtoToModel(resultDTO);
				Quantity targetQty = (Quantity) convertDtoToModel(targetDTO);
				Quantity converted = resultQty.convertTo(targetQty.getUnit());
				dto.setResultValue(converted.getValue());
				dto.setResultUnit(converted.getUnit().getUnitName());
			} else {
				dto.setResultValue(result);
				dto.setResultUnit(q1.getUnit().getUnitName());
			}

			dto.setResultMeasurementType(getMeasurementType(q1));
			dto.setError(false);
		} catch (Exception e) {
			dto.setErrorMessage("multiply Error: " + e.getMessage());
			dto.setError(true);
		}
		saveToRepository(dto);
		return dto;
	}

	// ===== HISTORY =====
	@Override
	public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
		return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operation.toLowerCase()));
	}

	@Override
	public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
		return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
	}

	@Override
	public long getOperationCount(String operation) {
		return repository.countByOperationAndIsErrorFalse(operation.toLowerCase());
	}

	@Override
	public List<QuantityMeasurementDTO> getErrorHistory() {
		return QuantityMeasurementDTO.fromEntityList(repository.findByIsErrorTrue());
	}
}