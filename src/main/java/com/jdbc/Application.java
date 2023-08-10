package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class Application {

    public static void main(String[] args) {

        try (Connection connection = DriverManager
                .getConnection("jdbc:h2:~/mydatabase", "sa", "somePassword")) {

            System.out.println("connection.isValid(0) = " + connection.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
