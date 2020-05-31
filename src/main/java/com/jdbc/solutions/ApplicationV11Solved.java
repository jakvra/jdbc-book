package com.jdbc.solutions;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ApplicationV11Solved {

    public static void main(String[] args) throws SQLException {

        DataSource ds = createDataSource();

        try (Connection connection = ds.getConnection()) {

            try (PreparedStatement stmt =
                         connection.prepareStatement(
                                 "delete from users where id = (select max(id) " +
                                         "from users)"
                                         )) {

                int deletedRows = stmt.executeUpdate();
                System.out.println("I just deleted " + deletedRows + " rows");
            }

            try (PreparedStatement stmt = connection
                    .prepareStatement("select * " + "from users")) {

                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    LocalDateTime registrationDate = resultSet.getObject(
                            "registration_date", LocalDateTime.class);
                    System.out.println("Found user: " + id + " | " + firstName +
                            " | " + lastName + " | " + registrationDate);
                }
            }
        }
    }

    private static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:~/mydatabase;INIT=RUNSCRIPT FROM 'classpath:schema" +
                ".sql'");
        ds.setUsername("sa");
        ds.setPassword("s3cr3tPassword");
        return ds;
    }
}
