package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class Application {

    public static void main(String[] args) {

        // create and connect to the H2 in memory database
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:")) {

            System.out.println("connection.isValid(0) = " + connection.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
