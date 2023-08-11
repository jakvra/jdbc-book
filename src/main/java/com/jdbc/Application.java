package com.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Application {

    public static void main(String[] args) {

        DataSource dataSource = createDataSource();

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO USERS (FIRST_NAME, LAST_NAME, REGISTRATION_DATE) VALUES (?, ?, ?)"
                    , Statement.RETURN_GENERATED_KEYS)) { // return the auto-generated keys

                stmt.setString(1, "John");
                stmt.setString(2, "Doe");
                stmt.setObject(3, LocalDateTime.now());

                stmt.addBatch();

//                stmt.clearParameters();

                stmt.setString(1, "Another John");
                stmt.setString(2, "Another Doe");
                stmt.setObject(3, LocalDateTime.now());

                stmt.addBatch();

                int[] numberOfInsertedRows = stmt.executeBatch();
                System.out.println("Arrays.toString(numberOfInsertedRows) = " + Arrays.toString(numberOfInsertedRows));
            }

            System.out.println("connection.isValid(0) = "
                    + connection.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static DataSource createDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:~/mydatabase;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        dataSource.setUsername("sa");
        dataSource.setPassword("somePassword");
        return dataSource;
    }
}
