package com.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class ApplicationV8 {

    public static void main(String[] args) throws SQLException {

        DataSource ds = createDataSource();

        try (Connection connection = ds.getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement("insert into users (first_name, last_name, registration_date) values (?,?,?)"
                                                    , Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, "[Some FirstName]");
                stmt.setString(2, "[Some LastName]");
                stmt.setObject(3, LocalDateTime.now());

                int insertedRows = stmt.executeUpdate();
                final ResultSet keysResultSet = stmt.getGeneratedKeys();
                keysResultSet.next();
                final long autogenerateId = keysResultSet.getLong(1);

                System.out.println("Inserted [" + insertedRows + "] row into table users, with autogenerated id [" +  autogenerateId + "]");

            }
        }
    }

    private static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:~/mydatabase;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        ds.setUsername("sa");
        ds.setPassword("s3cr3tPassword");
        return ds;
    }
}