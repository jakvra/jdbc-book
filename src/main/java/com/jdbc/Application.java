package com.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class Application {

    public static void main(String[] args) {

        DataSource dataSource = createDataSource();

        try (Connection connection = dataSource.getConnection()) {

            System.out.println("connection.isValid(0) = " + connection.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static DataSource createDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost/MY_DATABASE?serverTimezone=UTC");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");
        return dataSource;
    }
}
