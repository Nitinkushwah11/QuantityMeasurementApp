package com.apps.quantitymeasurement;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.model.QuantityModel;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.apps.quantitymeasurement.units.IMeasurable;
import com.apps.quantitymeasurement.units.LengthUnit;

/**
 * Simplified N-Tier Architecture Test Suite.
 * Contains 20 essential tests to verify Service Logic, Layer Decoupling, and Database History.
 */
public class QuantityMeasurementLayerTest {

    private IQuantityMeasurementService service;
    private InMemoryRepository mockRepository;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Helper: A Mock Repository to isolate the Service from the Hard Drive.
     */
    private static class InMemoryRepository implements IQuantityMeasurementRepository {
        public List<QuantityMeasurementEntity> db = new ArrayList<>();
        @Override public void save(QuantityMeasurementEntity entity) { db.add(entity); }
        @Override public List<QuantityMeasurementEntity> getAllMeasurements() { return db; }
    }

    @BeforeEach
    public void setUp() {
        mockRepository = new InMemoryRepository();
        service = new QuantityMeasurementServiceImpl(mockRepository);
        System.setOut(new PrintStream(outContent));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    // --- 1. ENTITY (DATA RECORD) TESTS ---

    @Test
    public void testEntityCreation_Success() {
        QuantityModel<IMeasurable> q1 = new QuantityModel<>(1.0, LengthUnit.FEET);
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(q1, null, "CONVERSION", new QuantityModel<>(12.0, LengthUnit.INCHES));
        assertFalse(entity.isError);
        assertEquals(12.0, entity.resultValue);
        assertTrue(entity.toString().contains("Result: 12.0 INCHES"));
    }

    @Test
    public void testEntityCreation_Error() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(null, null, "DIVIDE", "Zero Error", true);
        assertTrue(entity.isError);
        assertTrue(entity.toString().contains("ERROR: Zero Error"));
    }

    // --- 2. SERVICE LOGIC TESTS ---

    @Test
    public void testService_Convert_Success() throws QuantityMeasurementException {
        QuantityDTO result = service.convert(new QuantityDTO(1.0, "FEET", "LengthUnit"), "INCHES");
        assertEquals(12.0, result.value);
        assertEquals("INCHES", result.unit);
    }

    @Test
    public void testService_Compare_SameCategory() throws QuantityMeasurementException {
        assertTrue(service.compare(new QuantityDTO(1.0, "FEET", "LengthUnit"), new QuantityDTO(12.0, "INCHES", "LengthUnit")));
    }

    @Test
    public void testService_Compare_CrossCategoryRejection() throws QuantityMeasurementException {
        assertFalse(service.compare(new QuantityDTO(1.0, "FEET", "LengthUnit"), new QuantityDTO(1.0, "KILOGRAM", "WeightUnit")));
    }

    @Test
    public void testService_Add_Success() throws QuantityMeasurementException {
        QuantityDTO result = service.add(new QuantityDTO(2.0, "INCHES", "LengthUnit"), new QuantityDTO(2.0, "INCHES", "LengthUnit"), "CENTIMETERS");
        assertEquals(10.16, result.value);
    }

    @Test
    public void testService_Subtract_Success() throws QuantityMeasurementException {
        QuantityDTO result = service.subtract(new QuantityDTO(10.0, "FEET", "LengthUnit"), new QuantityDTO(2.0, "FEET", "LengthUnit"), "FEET");
        assertEquals(8.0, result.value);
    }

    @Test
    public void testService_Divide_Success() throws QuantityMeasurementException {
        QuantityDTO result = service.divide(new QuantityDTO(10.0, "GALLON", "VolumeUnit"), new QuantityDTO(5.0, "GALLON", "VolumeUnit"));
        assertEquals(2.0, result.value); // Pure ratio
    }

    @Test
    public void testService_DivideByZero_ThrowsException() {
        assertThrows(QuantityMeasurementException.class, () -> 
            service.divide(new QuantityDTO(10.0, "FEET", "LengthUnit"), new QuantityDTO(0.0, "FEET", "LengthUnit")));
    }

    @Test
    public void testService_TemperatureArithmetic_ThrowsException() {
        QuantityDTO t1 = new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit");
        assertThrows(QuantityMeasurementException.class, () -> service.add(t1, t1, "FAHRENHEIT"));
    }

    @Test
    public void testService_ExceptionsAreLoggedToRepository() {
        try { service.divide(new QuantityDTO(10.0, "FEET", "LengthUnit"), new QuantityDTO(0.0, "FEET", "LengthUnit")); } catch (Exception e) {}
        assertEquals(1, mockRepository.db.size());
        assertTrue(mockRepository.db.get(0).isError);
    }

    // --- 3. CONTROLLER OUTPUT TESTS ---

    @Test
    public void testController_ExecutesWithoutCrashing() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        assertDoesNotThrow(() -> controller.demonstrateApp());
    }

    @Test
    public void testController_OutputContainsSuccess() {
        new QuantityMeasurementController(service).demonstrateApp();
        assertTrue(outContent.toString().contains("Result: 12.0 INCHES"));
    }

    @Test
    public void testController_OutputContainsExpectedErrors() {
        new QuantityMeasurementController(service).demonstrateApp();
        assertTrue(outContent.toString().contains("EXPECTED ERROR CAUGHT"));
    }

    // --- 4. ARCHITECTURE & DECOUPLING TESTS ---

    @Test
    public void testArchitecture_ServiceUsesMockRepository() throws QuantityMeasurementException {
        service.convert(new QuantityDTO(1.0, "FEET", "LengthUnit"), "INCHES");
        assertEquals(1, mockRepository.db.size()); // Proves Service isn't hardcoded to the File DB
    }

    @Test
    public void testDataFlow_ServiceAlwaysReturnsDTO() throws QuantityMeasurementException {
        Object result = service.add(new QuantityDTO(1.0, "FEET", "LengthUnit"), new QuantityDTO(1.0, "FEET", "LengthUnit"), "INCHES");
        assertTrue(result instanceof QuantityDTO); // Enforces API contract
    }

    @Test
    public void testBackwardCompatibility_LegacyAppWrappers() {
        // Ensures your older tests won't break
        QuantityModel<IMeasurable> q1 = new QuantityModel<>(1.0, LengthUnit.FEET);
        QuantityModel<IMeasurable> q2 = new QuantityModel<>(12.0, LengthUnit.INCHES);
        assertEquals(q1.unit.convertToBaseUnit(q1.value), q2.unit.convertToBaseUnit(q2.value));
    }

    // --- 5. END-TO-END INTEGRATION TESTS ---

    @Test
    public void testE2E_SuccessfulOperationSavesReceipt() throws QuantityMeasurementException {
        service.subtract(new QuantityDTO(5.0, "FEET", "LengthUnit"), new QuantityDTO(2.0, "FEET", "LengthUnit"), "FEET");
        QuantityMeasurementEntity receipt = mockRepository.db.get(0);
        
        assertEquals("SUBTRACTION", receipt.operation);
        assertEquals(3.0, receipt.resultValue);
        assertFalse(receipt.isError);
    }

    @Test
    public void testE2E_FailedOperationSavesErrorReceipt() {
        QuantityDTO invalidDto = new QuantityDTO(1.0, "FAKE_UNIT", "LengthUnit");
        assertThrows(QuantityMeasurementException.class, () -> service.convert(invalidDto, "FEET"));
        
        QuantityMeasurementEntity receipt = mockRepository.db.get(0);
        assertEquals("CONVERSION", receipt.operation);
        assertTrue(receipt.isError);
        assertTrue(receipt.errorMessage.contains("Failed to resolve unit"));
    }

    @Test
    public void testE2E_GetHistoryReturnsAllReceipts() throws QuantityMeasurementException {
        service.convert(new QuantityDTO(1.0, "FEET", "LengthUnit"), "INCHES");
        service.compare(new QuantityDTO(1.0, "FEET", "LengthUnit"), new QuantityDTO(1.0, "FEET", "LengthUnit"));
        
        List<QuantityMeasurementEntity> history = service.getHistory();
        assertEquals(2, history.size());
        assertEquals("CONVERSION", history.get(0).operation);
        assertEquals("COMPARISON", history.get(1).operation);
    }
}