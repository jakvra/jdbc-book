package com.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class ApplicationV17 {

    public static void main(String[] args) throws SQLException {
        // database transactions

        DataSource ds = createDataSource();

        Connection connection = ds.getConnection();

        Savepoint savepoint;

        try (connection) {
            connection.setAutoCommit(false);

            int senderId = createUser(connection);
            int receiverId = createUser(connection);

            savepoint = connection.setSavepoint();

            int transactionId = sendMoney(connection, senderId, receiverId, 50);
            if (transactionId < 0) connection.rollback(savepoint);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            // connection.rollback();
        }
    }

    private static int createUser(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("insert into users (first_name, last_name, registration_date) values (?,?,?)"
                , Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "[Some FirstName]");
            stmt.setString(2, "[Some LastName]");
            stmt.setObject(3, LocalDateTime.now());
            stmt.executeUpdate();

            final ResultSet keysResultSet = stmt.getGeneratedKeys();
            keysResultSet.next();
            return keysResultSet.getInt(1);
        }
    }

    private static int sendMoney(Connection connection, int senderId, int receiverId, int amount) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update users set balance = (balance - ?) where id = ?")) {
            stmt.setInt(1, amount);
            stmt.setInt(2, senderId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement("update users set balance = (balance + ?) where id = ?")) {
            stmt.setInt(1, amount);
            stmt.setInt(2, receiverId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement("insert into transactions (sender, receiver, amount) values (?,?,?)"
                , Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setInt(3, amount);
            stmt.executeUpdate();

            final ResultSet keysResultSet = stmt.getGeneratedKeys();
            keysResultSet.next();
            return keysResultSet.getInt(1);
        }
    }

    private static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:h2:~/mydatabase;INIT=RUNSCRIPT FROM 'classpath:schema.sql'");
        ds.setUsername("sa");
        ds.setPassword("s3cr3tPassword");

        DataSource dataSource =
                ProxyDataSourceBuilder.create(ds)  // pass original datasource
                        .logQueryToSysOut()
                        // .logQueryByJUL()
                        //.logQueryBySlf4j()
                        //.logQueryByCommons()
                        .build();

        return dataSource;
    }
}