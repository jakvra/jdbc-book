package com.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

@Slf4j
public class Application {

    public static void main(String[] args) {

        DataSource dataSource = createDataSource();

        try (Connection connection = dataSource.getConnection()) {

            // update
            try (PreparedStatement stmt = connection.prepareStatement("UPDATE USERS SET first_name = CONCAT(FIRST_NAME,?) WHERE id > ?")) {

                stmt.setString(1, "-yay");
                stmt.setInt(2, 5);

                int rowsUpdated = stmt.executeUpdate();

                System.out.println("I just updated " + rowsUpdated + " rows");

            }

            // select
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM USERS")) { // return the auto-generated keys

                stmt.setFetchSize(50);

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
        HikariDataSource hikariDs = new HikariDataSource();
        hikariDs.setJdbcUrl("jdbc:h2:~/mydatabase;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        hikariDs.setUsername("sa");
        hikariDs.setPassword("somePassword");

        DataSource proxyDataSource = ProxyDataSourceBuilder.create(hikariDs)
//                .name("MyDS")
//                .asJson()
                .countQuery()
//                .logQueryBySlf4j()
                .logQueryToSysOut()
                .multiline()
                .build();

        return proxyDataSource;
    }
}
