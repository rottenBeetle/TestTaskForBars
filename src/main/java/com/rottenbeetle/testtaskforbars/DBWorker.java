package com.rottenbeetle.testtaskforbars;

import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DBWorker {

    private Connection connection;

    public DBWorker() throws SQLException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("src/database.properties"))){
            props.load(in);
            String url = props.getProperty("HOST");
            String username = props.getProperty("USERNAME");
            String password = props.getProperty("PASSWORD");

            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
