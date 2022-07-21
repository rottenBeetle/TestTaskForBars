package com.rottenbeetle.testtaskforbars;

import java.sql.*;

public class DBWorker {
    private final String HOST = "jdbc:postgresql://localhost:5432/postgres";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "defender";

    private Connection connection;

    public DBWorker() {
        try {
            this.connection = DriverManager.getConnection(HOST,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
