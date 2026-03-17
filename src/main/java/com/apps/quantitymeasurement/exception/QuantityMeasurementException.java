package com.apps.quantitymeasurement.exception;


/**
 * QuantityMeasurementException is a custom exception class used to handle
 * errors and exceptional conditions related to quantity measurement operations.
 * * This exception can be thrown when invalid measurements, unit conversions,
 * or other quantity-related operations fail during execution.
 */

public class QuantityMeasurementException extends Exception {
	 public QuantityMeasurementException(String message) {
	        super(message);
	    }

	    public QuantityMeasurementException(String message, Throwable cause) {
	        super(message, cause);
	    }

	    // Main method for testing purposes
	    public static void main(String[] args) {
	        try {
	            throw new QuantityMeasurementException(
	                "This is a test exception for quantity measurement."
	            );
	        } catch (QuantityMeasurementException ex) {
	            System.out.println("Caught QuantityMeasurementException: " + ex.getMessage());
	        }
	    }
}