package com.jdbc;

import org.h2.jdbcx.JdbcDataSource;

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
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:~/mydatabase");
        dataSource.setUser("sa");
        dataSource.setPassword("somePassword");
        return dataSource;
    }

}
