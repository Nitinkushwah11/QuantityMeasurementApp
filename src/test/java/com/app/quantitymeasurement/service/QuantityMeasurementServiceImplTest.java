package com.app.quantitymeasurement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

/**
 * Unit tests for the Service layer.
 * @ExtendWith(MockitoExtension.class) tells JUnit to process the @Mock 
 * and @InjectMocks annotations before running the tests.
 */
@ExtendWith(MockitoExtension.class)
public class QuantityMeasurementServiceImplTest {

    // 1. We MOCK the Repository so we don't touch the real database
    @Mock
    private QuantityMeasurementRepository repository;

    // 2. We INJECT that fake repository into our real Service
    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    private QuantityDTO feetDTO;
    private QuantityDTO inchesDTO;
    private QuantityDTO gallonDTO;
    private QuantityDTO literDTO;

    // 3. This runs BEFORE EVERY test to give us fresh data
    @BeforeEach
    void setUp() {
        feetDTO = new QuantityDTO();
        feetDTO.setValue(1.0);
        feetDTO.setUnit("FEET");
        feetDTO.setMeasurementType("LengthUnit");

        inchesDTO = new QuantityDTO();
        inchesDTO.setValue(12.0);
        inchesDTO.setUnit("INCHES");
        inchesDTO.setMeasurementType("LengthUnit");

        gallonDTO = new QuantityDTO();
        gallonDTO.setValue(1.0);
        gallonDTO.setUnit("GALLON");
        gallonDTO.setMeasurementType("VolumeUnit");

        literDTO = new QuantityDTO();
        literDTO.setValue(0.0);
        literDTO.setUnit("LITER");
        literDTO.setMeasurementType("VolumeUnit");
    }

    // 4. Test 1: Does 1 Foot == 12 Inches?
    @Test
    void givenOneFootAndTwelveInches_whenCompared_thenShouldReturnEqual() {
        // Arrange (We mock the save method to just do nothing and return null, simulating success)
        when(repository.save(any(QuantityMeasurementEntity.class))).thenReturn(null);

        // Act (Call the real method in the service)
        QuantityMeasurementDTO result = service.compare(feetDTO, inchesDTO);

        // Assert (Check the results)
        assertNotNull(result);
        assertEquals("COMPARE", result.getOperation());
        assertEquals("Equal", result.getResultString());
        
        // Verify that the repository's save method was called exactly 1 time
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    // 5. Test 2: Convert 1 Gallon to Liters
    @Test
    void givenOneGallon_whenConvertedToLiters_thenShouldReturnThreePointSevenNine() {
        when(repository.save(any(QuantityMeasurementEntity.class))).thenReturn(null);

        QuantityMeasurementDTO result = service.convert(gallonDTO, literDTO);

        assertNotNull(result);
        assertEquals("CONVERT", result.getOperation());
        assertEquals("LITER", result.getResultUnit());
        assertEquals(3.79, result.getResultValue(), 0.01); // 0.01 is the acceptable margin of error
        
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    // 6. Test 3: Add 1 Foot and 12 Inches (Expected: 2.0 Feet)
    @Test
    void givenOneFootAndTwelveInches_whenAdded_thenShouldReturnTwoFeet() {
        when(repository.save(any(QuantityMeasurementEntity.class))).thenReturn(null);

        QuantityMeasurementDTO result = service.add(feetDTO, inchesDTO);

        assertNotNull(result);
        assertEquals("ADD", result.getOperation());
        assertEquals("FEET", result.getResultUnit());
        assertEquals(2.0, result.getResultValue(), 0.01);
    }

    // --- NEW COMPREHENSIVE TESTS ---

    // 7. Test 4: Subtract 12 Inches from 2 Feet
    @Test
    void givenTwoFeetAndTwelveInches_whenSubtracted_thenShouldReturnOneFoot() {
        when(repository.save(any(QuantityMeasurementEntity.class))).thenReturn(null);
        
        QuantityDTO twoFeetDTO = new QuantityDTO();
        twoFeetDTO.setValue(2.0);
        twoFeetDTO.setUnit("FEET");
        twoFeetDTO.setMeasurementType("LengthUnit");

        QuantityMeasurementDTO result = service.subtract(twoFeetDTO, inchesDTO);

        assertNotNull(result);
        assertEquals("SUBTRACT", result.getOperation());
        assertEquals(1.0, result.getResultValue(), 0.01);
    }

    // 8. Test 5: Divide 10 Feet by 2 Feet
    @Test
    void givenTenFeetAndTwoFeet_whenDivided_thenShouldReturnFive() {
        when(repository.save(any(QuantityMeasurementEntity.class))).thenReturn(null);

        QuantityDTO tenFeetDTO = new QuantityDTO();
        tenFeetDTO.setValue(10.0);
        tenFeetDTO.setUnit("FEET");
        tenFeetDTO.setMeasurementType("LengthUnit");

        QuantityDTO twoFeetDTO = new QuantityDTO();
        twoFeetDTO.setValue(2.0);
        twoFeetDTO.setUnit("FEET");
        twoFeetDTO.setMeasurementType("LengthUnit");

        QuantityMeasurementDTO result = service.divide(tenFeetDTO, twoFeetDTO);

        assertNotNull(result);
        assertEquals("DIVIDE", result.getOperation());
        assertEquals(5.0, result.getResultValue(), 0.01);
        assertEquals("RATIO", result.getResultUnit());
    }

    // 9. Test 6: Divide by Zero Exception handling
    @Test
    void givenTenFeetAndZeroFeet_whenDivided_thenShouldCatchExceptionAndSaveError() {
        when(repository.save(any(QuantityMeasurementEntity.class))).thenReturn(null);

        QuantityDTO tenFeetDTO = new QuantityDTO();
        tenFeetDTO.setValue(10.0);
        tenFeetDTO.setUnit("FEET");
        tenFeetDTO.setMeasurementType("LengthUnit");

        QuantityDTO zeroFeetDTO = new QuantityDTO();
        zeroFeetDTO.setValue(0.0);
        zeroFeetDTO.setUnit("FEET");
        zeroFeetDTO.setMeasurementType("LengthUnit");

        // Act
        QuantityMeasurementDTO result = service.divide(tenFeetDTO, zeroFeetDTO);

        // Assert that our Service gracefully caught the error, didn't crash, and marked it as an error!
        assertNotNull(result);
        assertTrue(result.isError());
        assertTrue(result.getErrorMessage().contains("Cannot divide by zero"));
        
        // Verify an error receipt was still saved to the repository!
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    // 10. Test 7: Get History
    @Test
    void givenSavedOperations_whenGetOperationHistory_thenShouldReturnList() {
        java.util.List<QuantityMeasurementEntity> mockList = java.util.Arrays.asList(
            new QuantityMeasurementEntity(null, null, "ADD", 0.0)
        );
        // We tell our fake database to return our mock list when asked for "ADD" history
        when(repository.findByOperation("ADD")).thenReturn(mockList);

        java.util.List<QuantityMeasurementDTO> history = service.getOperationHistory("ADD");

        assertEquals(1, history.size());
        verify(repository, times(1)).findByOperation("ADD");
    }
}