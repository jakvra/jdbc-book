package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // Creating the connection with HSQLDB
        String db = "jdbc:hsqldb:file:~/marco/hsqldb";
        String user = "root";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(db, user, password)) {
            System.out.println("connection.isValid(0) = " + connection.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
