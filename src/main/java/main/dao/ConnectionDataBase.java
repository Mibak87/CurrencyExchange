package main.dao;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDataBase {
    public static Connection connectionDB() {
        Connection connection = null;
        URL resource = CurrenciesDao.class.getClassLoader().getResource("CurrencyExchange.sqlite");
        String path = null;
        try {
            path = new File(resource.toURI()).getAbsolutePath();
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных.");
            e.printStackTrace();
        }
        return connection;
    }
}
