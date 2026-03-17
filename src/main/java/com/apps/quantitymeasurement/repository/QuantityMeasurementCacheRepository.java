package com.apps.quantitymeasurement.repository;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;

/**
 * File-based repository implementation for storing and retrieving 
 * QuantityMeasurementEntity objects using Java Serialization.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    // HERE IS THE FILE NAME YOUR PREVIOUS CLASS WAS LOOKING FOR!
    public static final String FILE_NAME = "measurement_history.dat";

    @Override
    public void save(QuantityMeasurementEntity entity) {
        File file = new File(FILE_NAME);
        boolean append = file.exists() && file.length() > 0;

        try (FileOutputStream fos = new FileOutputStream(file, append);
             // If appending, use our custom class. Otherwise, use the standard Java class.
             ObjectOutputStream oos = append ? new AppendableObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            
            oos.writeObject(entity);
            System.out.println("Saved to history: " + entity);
            
        } catch (IOException e) {
            System.err.println("Error saving measurement to cache: " + e.getMessage());
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        List<QuantityMeasurementEntity> history = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists() || file.length() == 0) {
            return history; // Return empty list if no file exists yet
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
             
            while (true) {
                try {
                    QuantityMeasurementEntity entity = (QuantityMeasurementEntity) ois.readObject();
                    history.add(entity);
                } catch (EOFException e) {
                    break; // Expected exception when we reach the end of the file
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading measurements from cache: " + e.getMessage());
        }

        return history;
    }
}