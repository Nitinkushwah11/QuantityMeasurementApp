package com.app.quantitymeasurementapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.app.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.app.quantitymeasurementapp.entity.QuantityDTO;
import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.app.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurementapp.service.IQuantityMeasurementService;
import com.app.quantitymeasurementapp.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurementapp.unit.*;

/**
 * Unified Test Suite for Quantity Measurement App (UC1 - UC16).
 * This version uses pure JUnit 5 and manual Dependency Injection (No Mockito).
 * It uses the CacheRepository as a "Fake" for unit tests.
 */
public class QuantityMeasurementAppTest {

    // ============================================================
    // SECTION 1: CORE MATH LOGIC (UC1 - UC14)
    // ============================================================
    @Nested
    @DisplayName("UC1-UC14: Mathematical Logic & Conversion Tests")
    class MathLogicTests {

        @ParameterizedTest(name = "{0} {1} == {2} {3} -> {4}")
        @MethodSource("provideEqualityData")
        void testEqualityMatrix(double v1, IMeasurable u1, double v2, IMeasurable u2, boolean expected) {
            double b1 = Math.round(u1.convertToBaseUnit(v1) * 100.0) / 100.0;
            double b2 = Math.round(u2.convertToBaseUnit(v2) * 100.0) / 100.0;
            assertEquals(expected, Double.compare(b1, b2) == 0);
        }

        @ParameterizedTest(name = "Convert {0} {1} to {2} -> {3}")
        @MethodSource("provideConversionData")
        void testConversionMatrix(double val, IMeasurable from, IMeasurable to, double expected) {
            double baseValue = from.convertToBaseUnit(val);
            double result = to.convertFromBaseUnit(baseValue);
            assertEquals(expected, Math.round(result * 100.0) / 100.0, 0.01);
        }

        static Stream<Arguments> provideEqualityData() {
            return Stream.of(
                Arguments.of(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES, true),
                Arguments.of(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET, true),
                Arguments.of(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM, true),
                Arguments.of(1.0, VolumeUnit.GALLON, 3.785, VolumeUnit.LITER, true),
                Arguments.of(0.0, TemperatureUnit.CELSIUS, 32.0, TemperatureUnit.FAHRENHEIT, true)
            );
        }

        static Stream<Arguments> provideConversionData() {
            return Stream.of(
                Arguments.of(1.0, LengthUnit.FEET, LengthUnit.INCHES, 12.0),
                Arguments.of(1.0, VolumeUnit.LITER, VolumeUnit.MILLILITER, 1000.0),
                Arguments.of(100.0, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT, 212.0)
            );
        }
    }

    // ============================================================
    // SECTION 2: SERVICE LAYER TESTS (USING CACHE REPO AS FAKE)
    // ============================================================
    @Nested
    @DisplayName("UC16: Service Layer Unit Tests")
    class ServiceLayerTests {
        private IQuantityMeasurementService service;
        private IQuantityMeasurementRepository cacheRepo;

        @BeforeEach
        void setUp() {
            // Using the real CacheRepository instead of a Mockito mock
            cacheRepo = QuantityMeasurementCacheRepository.getInstance();
            cacheRepo.deleteAll(); 
            service = new QuantityMeasurementServiceImpl(cacheRepo);
        }

        @Test
        @DisplayName("Verify that compare operation persists a record")
        void testCompare_PersistsToRepo() {
            QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
            QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
            
            assertTrue(service.compare(q1, q2));
            
            // Instead of verify(), we check the actual state of the repository
            assertEquals(1, cacheRepo.getTotalCount(), "Repository should have 1 record saved");
        }

        @Test
        @DisplayName("Verify arithmetic restriction for Temperature")
        void testAdd_WithUnsupportedArithmetic_ThrowsException() {
            QuantityDTO t1 = new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit");
            assertThrows(QuantityMeasurementException.class, () -> 
                service.add(t1, t1, "FAHRENHEIT")
            );
        }
    }

    // ============================================================
    // SECTION 3: CONTROLLER LAYER TESTS
    // ============================================================
    @Nested
    @DisplayName("UC16: Controller Layer Unit Tests")
    class ControllerLayerTests {
        private QuantityMeasurementController controller;
        private IQuantityMeasurementRepository cacheRepo;

        @BeforeEach
        void setUp() {
            cacheRepo = QuantityMeasurementCacheRepository.getInstance();
            cacheRepo.deleteAll();
            IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(cacheRepo);
            controller = new QuantityMeasurementController(service);
        }

        @Test
        @DisplayName("Verify Controller routes data through service to repo")
        void testPerformComparison_IntegrationCheck() {
            QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
            QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");
            
            boolean result = controller.performComparison(q1, q2);
            
            assertTrue(result);
            assertEquals(1, cacheRepo.getTotalCount(), "Controller call should result in 1 repo record");
        }
    }

    // ============================================================
    // SECTION 4: INTEGRATION TESTS (REAL DATABASE)
    // ============================================================
    @Nested
    @DisplayName("UC16: End-to-End Integration Tests")
    class IntegrationTests {
        private QuantityMeasurementApp app;

        @BeforeEach
        void setUp() {
            // This initializes the app using your application.properties settings (Database)
            app = QuantityMeasurementApp.getInstance();
            app.deleteAllMeasurements();
        }

        @AfterEach
        void tearDown() {
            app.closeResources();
        }

        @Test
        void testFullFlow_DatabasePersistence() {
            QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LengthUnit");
            QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");

            boolean result = app.controller.performComparison(q1, q2);
            assertTrue(result);

            // Access the real Database Repository instance
            IQuantityMeasurementRepository dbRepo = QuantityMeasurementDatabaseRepository.getInstance();
            List<QuantityMeasurementEntity> history = dbRepo.getAllMeasurements();
            
            assertFalse(history.isEmpty());
            assertEquals("COMPARISON", history.get(0).operation);
        }
    }
}
