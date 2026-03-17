package com.app.quantitymeasurementapp.repository;


import com.app.quantitymeasurementapp.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.exception.DatabaseException;
import com.app.quantitymeasurementapp.util.ConnectionPool;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * QuantityMeasurementDatabaseRepository.java
 *
 * This class implements the IQuantityMeasurementRepository interface and provides methods
 * to interact with a relational database for storing and retrieving quantity measurement
 * data. It uses JDBC for database operations and a connection pool for efficient resource
 * management. The repository handles CRUD operations for QuantityMeasurementEntity objects
 * and includes methods to query measurements by operation type and measurement type. It
 * also includes error handling using a custom DatabaseException to encapsulate any
 * database-related issues. The repository is designed to be used by the service layer of
 * the application to persist the results of quantity measurement operations and to
 * retrieve historical data for analysis and reporting. It ensures that database connections
 * are properly managed and that resources are released after use to prevent leaks and ensure
 * optimal performance.
 *
 * @author Developer
 * @version 16.0
 * @since 16.0
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    // Logger for logging database operations and errors
    private static final Logger logger = Logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName());

    // Singleton instance of the repository
    private static QuantityMeasurementDatabaseRepository instance;

    private static final String INSERT_QUERY =
            "INSERT INTO quantity_measurement_entity " +
            "(this_value, this_unit, this_measurement_type, that_value, that_unit, " +
            "that_measurement_type, operation, result_value, result_unit, " +
            "result_measurement_type, result_string, is_error, error_message, " +
            "created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SELECT_ALL_QUERY =
            "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";

    private static final String SELECT_BY_OPERATION =
            "SELECT * FROM quantity_measurement_entity WHERE operation = ? ORDER BY created_at DESC";

    private static final String SELECT_BY_MEASUREMENT_TYPE =
            "SELECT * FROM quantity_measurement_entity " +
            "WHERE this_measurement_type = ? ORDER BY created_at DESC";

    private static final String DELETE_ALL_QUERY =
            "DELETE FROM quantity_measurement_entity";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) FROM quantity_measurement_entity";

    private ConnectionPool connectionPool;

    private QuantityMeasurementDatabaseRepository() {
        try {
            connectionPool = ConnectionPool.getInstance();
            initializeDatabase();
        } catch (SQLException e) {
            logger.severe("Failed to initialize database connection pool: " + e.getMessage());
        }
    }

    /**
     * Initializes the database schema for testing purposes. This method creates the
     * quantity_measurement_entity table if it does not already exist. It is called during
     * the construction of the repository to ensure that the database is ready for use. The
     * method uses a connection from the connection pool to execute the SQL statement for
     * creating the table. Error handling is included to catch any SQL exceptions that may
     * occur during the execution of the schema initialization and logs any errors encountered.
     *
     * Note: In a production environment, database schema management is typically handled
     * separately using migration tools like Flyway or Liquibase. This method is included here
     * for simplicity and testing purposes to ensure that the necessary table exists before
     * running any database operations.
     */
    private void initializeDatabase() {
        String schema = "CREATE TABLE IF NOT EXISTS quantity_measurement_entity (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "this_value DOUBLE NOT NULL, " +
                "this_unit VARCHAR(50) NOT NULL, " +
                "this_measurement_type VARCHAR(50) NOT NULL, " +
                "that_value DOUBLE, " +
                "that_unit VARCHAR(50), " +
                "that_measurement_type VARCHAR(50), " +
                "operation VARCHAR(20) NOT NULL, " +
                "result_value DOUBLE, " +
                "result_unit VARCHAR(50), " +
                "result_measurement_type VARCHAR(50), " +
                "result_string VARCHAR(255), " +
                "is_error BOOLEAN DEFAULT FALSE, " +
                "error_message VARCHAR(500), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
        
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            stmt.execute(schema);
            logger.info("Database schema initialized successfully.");
        } catch (SQLException e) {
            logger.severe("Schema initialization failed: " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static synchronized QuantityMeasurementDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementDatabaseRepository();
        }
        return instance;
    }

    /**
     * Saves a QuantityMeasurementEntity to the database. This method uses a prepared statement
     * to insert the entity's data into the quantity_measurement_entity table. It handles SQL
     * exceptions by catching them and throwing a custom DatabaseException with a meaningful
     * message. The method also ensures that all database resources are properly closed after
     * use to prevent leaks. Logging is included to track successful saves and any errors that
     * occur during the operation.
     *
     * @param entity the QuantityMeasurementEntity to be saved
     */
    
    @Override
    public void save(QuantityMeasurementEntity entity) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connectionPool.getConnection();
            pstmt = conn.prepareStatement(INSERT_QUERY);
            
            pstmt.setDouble(1, entity.thisValue);
            pstmt.setString(2, entity.thisUnit);
            pstmt.setString(3, entity.thisMeasurementType);
            
            if (entity.thatUnit != null) {
                pstmt.setDouble(4, entity.thatValue);
                pstmt.setString(5, entity.thatUnit);
                pstmt.setString(6, entity.thatMeasurementType);
            } else {
                pstmt.setNull(4, Types.DOUBLE);
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
            }
            
            pstmt.setString(7, entity.operation);
            pstmt.setDouble(8, entity.resultValue);
            pstmt.setString(9, entity.resultUnit);
            pstmt.setString(10, entity.resultMeasurementType);
            
            // Map result to result_string for history view
            pstmt.setString(11, entity.toString());
            
            pstmt.setBoolean(12, entity.isError);
            pstmt.setString(13, entity.errorMessage);

            pstmt.executeUpdate();
            logger.info("Measurement saved successfully to database.");
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(INSERT_QUERY, e);
        } finally {
            closeResources(pstmt, conn);
        }
    }

    /**
     * Retrieves all QuantityMeasurementEntity instances from the database. This method executes a
     * SQL query to select all records from the quantity_measurement_entity table, ordered by
     * creation date in descending order. It uses a statement to execute the query and maps the
     * result set to a list of QuantityMeasurementEntity objects. The method includes error handling
     * to catch any SQL exceptions that may occur during the database interaction and rethrows them
     * as DatabaseException with a meaningful message. Finally, it ensures that all database
     * resources are properly closed to prevent leaks. Logging is included to track the number of
     * measurements retrieved and any errors that occur during the operation.
     *
     * @return a list of all QuantityMeasurementEntity instances retrieved from the database
     */
    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<QuantityMeasurementEntity> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_ALL_QUERY);
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(SELECT_ALL_QUERY, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return list;
    }

    /**
     * Get measurements by operation type. This method retrieves all quantity measurement
     * entities from the database that match the specified operation type (e.g., "Addition",
     * "Subtraction", "Multiplication", "Division"). It uses a prepared statement to execute
     * the query and maps the result set to a list of QuantityMeasurementEntity objects.
     * The method also includes error handling to catch any SQL exceptions that may occur
     * during the database interaction and rethrows them as DatabaseException with a
     * meaningful message. Finally, it ensures that all database resources are properly closed
     * to prevent leaks.
     *
     * @param operation the type of operation to filter measurements by
     * @return a list of QuantityMeasurementEntity objects that match the specified operation type
     */
    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<QuantityMeasurementEntity> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_OPERATION);
            pstmt.setString(1, operation);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(SELECT_BY_OPERATION, e);
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return list;
    }

    /**
     * Get measurements by measurement type (Length, Weight, Volume, Temperature).
     * This method retrieves all quantity measurement entities from the database that
     * match the specified measurement type (e.g., "Length", "Weight", "Volume",
     * "Temperature"). It uses a prepared statement to execute the query and maps the
     * result set to a list of QuantityMeasurementEntity objects. The method also
     * includes error handling to catch any SQL exceptions that may occur during
     * the database interaction and rethrows them as DatabaseException. Finally, it
     * ensures that all database resources are properly closed to prevent leaks.
     *
     * @param measurementType the type of measurement to filter by
     * @return a list of QuantityMeasurementEntity objects that match the specified
     * measurement type
     */
    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<QuantityMeasurementEntity> list = new ArrayList<>();
        try {
            conn = connectionPool.getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_MEASUREMENT_TYPE);
            pstmt.setString(1, measurementType);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(SELECT_BY_MEASUREMENT_TYPE, e);
        } finally {
            closeResources(rs, pstmt, conn);
        }
        return list;
    }

    /**
     * Get count of all measurements. This method executes a SQL query to count the
     * total number of quantity measurement entities in the database. It uses a statement
     * to execute the COUNT query and retrieves the result from the result set. The method
     * includes error handling to catch any SQL exceptions that may occur during the
     * database interaction and rethrows them as DatabaseException with a meaningful
     * message. Finally, it ensures that all database resources are properly closed to
     * prevent leaks.
     *
     * @return the total count of quantity measurement entities in the database
     */
    @Override
    public int getTotalCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(COUNT_QUERY);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(COUNT_QUERY, e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return 0;
    }

    /**
     * Delete all measurements (useful for testing). This method executes a SQL query
     * to delete all quantity measurement entities from the database. It uses a statement
     * to execute the DELETE query and includes error handling to catch any SQL exceptions
     * that may occur during the database interaction, rethrowing them as DatabaseException
     * with a meaningful message. Finally, it ensures that all database resources are
     * properly closed to prevent leaks. This method is particularly useful for testing
     * purposes to reset the state of the database before running test cases.
     *
     * Note: Use this method with caution in a production environment as it will permanently
     * delete all measurement data from the database. It is recommended to use this method
     * only in a testing context or with appropriate safeguards in place to prevent accidental data loss.
     */
    @Override
    public void deleteAll() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connectionPool.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(DELETE_ALL_QUERY);
            logger.info("All measurements deleted from database.");
        } catch (SQLException e) {
            throw DatabaseException.queryFailed(DELETE_ALL_QUERY, e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    /**
     * Get pool statistics. This method provides insights into the current state of
     * the connection pool, such as the number of available and used connections.
     *
     * This can be useful for monitoring and debugging database connection issues. The
     * method can be overridden by repository implementations that utilize connection
     * pooling to provide specific pool statistics, while other implementations can
     * simply return a default message indicating that pool statistics are not available.
     */
    @Override
    public String getPoolStatistics() {
        if (connectionPool != null) {
            return connectionPool.toString();
        }
        return "Pool statistics not available";
    }

    /**
     * Release resources held by the repository, such as closing database connections or
     * clearing caches. This method can be implemented by repository implementations that
     * manage resources to ensure proper cleanup when the repository is no longer needed.
     */
    @Override
    public void releaseResources() {
        if (connectionPool != null) {
            connectionPool.closeAll();
        }
    }

    /**
     * Map ResultSet row to QuantityMeasurementEntity
     */
    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        // Initialize an empty entity. Note: in a real app you might add a constructor for this.
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        
        entity.thisValue = rs.getDouble("this_value");
        entity.thisUnit = rs.getString("this_unit");
        entity.thisMeasurementType = rs.getString("this_measurement_type");
        
        entity.thatValue = rs.getDouble("that_value");
        entity.thatUnit = rs.getString("that_unit");
        entity.thatMeasurementType = rs.getString("that_measurement_type");
        
        entity.operation = rs.getString("operation");
        entity.resultValue = rs.getDouble("result_value");
        entity.resultUnit = rs.getString("result_unit");
        entity.resultMeasurementType = rs.getString("result_measurement_type");
        
        entity.isError = rs.getBoolean("is_error");
        entity.errorMessage = rs.getString("error_message");
        
        return entity;
    }

    /**
     * Release connection back to pool
     */
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.warning(e.getMessage()); } }
        closeResources(stmt, conn);
    }

    /**
     * Release connection and statement back to pool
     */
    private void closeResources(Statement stmt, Connection conn) {
        if (stmt != null) { try { stmt.close(); } catch (SQLException e) { logger.warning(e.getMessage()); } }
        if (conn != null && connectionPool != null) {
            connectionPool.releaseConnection(conn);
        }
    }

    // Main method for testing purposes
    public static void main(String[] args) {
        try {
            QuantityMeasurementDatabaseRepository repo = QuantityMeasurementDatabaseRepository.getInstance();
            logger.info("Total measurements: " + repo.getTotalCount());
            repo.releaseResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}