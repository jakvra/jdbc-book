package com.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class Application {

    public static void main(String[] args) {

        DataSource dataSource = createDataSource();

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM USERS")) { // return the auto-generated keys

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    LocalDateTime registrationDate = rs.getObject("registration_date", LocalDateTime.class);

                    System.out.println("Found user: " + id + " | " + firstName + " | " + lastName + " | " + registrationDate);
                }

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
