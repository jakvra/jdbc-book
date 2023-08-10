package com.jdbc;

import org.vibur.dbcp.ViburDBCPDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException {

        // Creating the connection with HSQLDB
        String db = "jdbc:hsqldb:file:~/marco/hsqldb";
        String user = "root";
        String password = "password";

        DataSource dataSource = createDataSource(db, user, password);

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("connection.isValid(0) = " + connection.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static DataSource createDataSource(String db, String user, String password) {
        ViburDBCPDataSource ds = new ViburDBCPDataSource();
        ds.setJdbcUrl(db);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.start();
        return ds;
    }
}
