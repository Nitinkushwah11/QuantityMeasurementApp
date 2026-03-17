package com.app.quantitymeasurementapp.repository;



import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A simple in-memory repository for development and testing.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = Logger.getLogger(QuantityMeasurementCacheRepository.class.getName());
    private static QuantityMeasurementCacheRepository instance;
    
    // Our "In-Memory Database"
    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();

    private QuantityMeasurementCacheRepository() {
        logger.info("QuantityMeasurementCacheRepository initialized.");
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        logger.info("Saved to Cache: " + entity.operation);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return new ArrayList<>(cache);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return cache.stream()
                .filter(e -> e.operation.equalsIgnoreCase(operation))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String type) {
        return cache.stream()
                .filter(e -> e.thisMeasurementType.equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalCount() {
        return cache.size();
    }

    @Override
    public void deleteAll() {
        cache.clear();
        logger.info("Cache cleared.");
    }
}