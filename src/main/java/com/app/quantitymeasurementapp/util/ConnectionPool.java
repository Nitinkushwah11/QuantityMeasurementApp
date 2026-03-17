package com.app.quantitymeasurementapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Manages a reusable fleet of database connections.
 * Prevents the application from wasting time opening and closing 
 * a new connection for every single database query.
 */
public class ConnectionPool {
    
    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());
    private static ConnectionPool instance;

    // The two parking lots for our connections
    private final List<Connection> availableConnections = new ArrayList<>();
    private final List<Connection> usedConnections = new ArrayList<>();
    
    private final int poolSize;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String driverClass;
    private final String testQuery;

    private ConnectionPool() throws SQLException {
        ApplicationConfig config = ApplicationConfig.getInstance();
        
        // Fetch settings from our simplified config manager
        this.poolSize = config.getIntProperty(ApplicationConfig.ConfigKey.DB_POOL_SIZE.getKey(), 10);
        this.dbUrl = config.getProperty(ApplicationConfig.ConfigKey.DB_URL.getKey());
        this.dbUsername = config.getProperty(ApplicationConfig.ConfigKey.DB_USERNAME.getKey());
        this.dbPassword = config.getProperty(ApplicationConfig.ConfigKey.DB_PASSWORD.getKey());
        this.driverClass = config.getProperty(ApplicationConfig.ConfigKey.DB_DRIVER_CLASS.getKey());
        this.testQuery = config.getProperty(ApplicationConfig.ConfigKey.HIKARI_CONNECTION_TEST_QUERY.getKey(), "SELECT 1");

        // Pre-warm the pool by creating the initial fleet of connections
        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(createConnection());
        }
        logger.info("Connection pool initialized with " + poolSize + " connections.");
    }

    public static synchronized ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * The physical act of connecting to the database.
     */
    private Connection createConnection() throws SQLException {
        try {
            Class.forName(driverClass);
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + driverClass, e);
        }
    }

    /**
     * Borrows a connection from the pool.
     */
    public synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            if (usedConnections.size() < poolSize) {
                availableConnections.add(createConnection());
            } else {
                throw new SQLException("Maximum pool size reached! No available connections.");
            }
        }
        
        // Take the last connection from the available list
        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        
        // Ensure the connection didn't die while sitting in the pool
        if (!validateConnection(connection)) {
            connection = createConnection();
        }
        
        // Move it to the used list and hand it to the requester
        usedConnections.add(connection);
        return connection;
    }

    /**
     * Returns a connection to the pool for the next person to use.
     */
    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            availableConnections.add(connection);
        }
    }

    /**
     * Pings the database to ensure the connection is still alive.
     */
    public boolean validateConnection(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(this.testQuery);
            return true;
        } catch (SQLException e) {
            logger.warning("Dead connection detected: " + e.getMessage());
            return false;
        }
    }

    /**
     * Destroys all connections (called when the app is shutting down).
     */
    public synchronized void closeAll() {
        for (Connection c : availableConnections) {
            try { c.close(); } catch (SQLException ignored) {}
        }
        for (Connection c : usedConnections) {
            try { c.close(); } catch (SQLException ignored) {}
        }
        availableConnections.clear();
        usedConnections.clear();
        logger.info("All database connections successfully closed.");
    }

    public int getAvailableConnectionCount() { return availableConnections.size(); }
    public int getUsedConnectionCount() { return usedConnections.size(); }

    @Override
    public String toString() {
        return "ConnectionPool [Available: " + getAvailableConnectionCount() + ", Used: " + getUsedConnectionCount() + "]";
    }
}
