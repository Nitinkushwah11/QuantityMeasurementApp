package com.app.quantitymeasurement.model;

/**
 * OperationType enum defines a set of constants that represent the different 
 * types of operations that can be performed in the quantity measurement application. 
 * By using an enum, we ensure type safety and improve code readability throughout 
 * the application, ensuring that only valid operation types are processed.
 */
public enum OperationType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    COMPARE,
    CONVERT;

    /**
     * Optional: Returns a user-friendly lowercase version of the operation name.
     * Useful for display purposes in the UI or JSON responses.
     *
     * @return lowercase string of the enum constant name
     */
    public String getDisplayName() {
        return this.name().toLowerCase();
    }
}