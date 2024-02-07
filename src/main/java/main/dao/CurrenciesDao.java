package main.dao;

import main.entity.Currencies;
import main.config.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao implements Dao<Currencies> {
    private final String path = Config.getDataBasePath();
    @Override
    public List<Currencies> getAll() throws SQLException {
        String sqlQuery = "SELECT * FROM Currencies";
        List<Currencies> listCurrencies = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Currencies currencies = new Currencies();
            long currenciesId = resultSet.getLong("ID");
            currencies.setId(currenciesId);
            String currenciesCode = resultSet.getString("Code");
            currencies.setCode(currenciesCode);
            String currenciesFullName = resultSet.getString("FullName");
            currencies.setFullName(currenciesFullName);
            String currenciesSign = resultSet.getString("Sign");
            currencies.setSign(currenciesSign);
            listCurrencies.add(currencies);
        }
        connection.close();
        return listCurrencies;
    }

    @Override
    public void save(Currencies currencies) throws SQLException {
        String sqlQuery = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, currencies.getCode());
        statement.setString(2, currencies.getFullName());
        statement.setString(3, currencies.getSign());
        statement.executeUpdate();
        connection.close();
    }

    @Override
    public void update(Currencies currencies) throws SQLException {
        String sqlQuery = "UPDATE Currencies SET Code = ?, FullName = ?, Sign = ? WHERE ID = ?";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, currencies.getCode());
        statement.setString(2, currencies.getFullName());
        statement.setString(3, currencies.getSign());
        statement.setLong(4,currencies.getId());
        statement.executeUpdate();
        connection.close();
    }

    @Override
    public Optional<Currencies> getById(long id) throws SQLException {
        String sqlQuery = "SELECT * FROM Currencies WHERE ID=?";
        Currencies currencies = new Currencies();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            long currenciesId = resultSet.getLong("ID");
            currencies.setId(currenciesId);
            String currenciesCode = resultSet.getString("Code");
            currencies.setCode(currenciesCode);
            String currenciesFullName = resultSet.getString("FullName");
            currencies.setFullName(currenciesFullName);
            String currenciesSign = resultSet.getString("Sign");
            currencies.setSign(currenciesSign);
        }
        connection.close();
        if (currencies.getCode() == null || currencies.getFullName() == null || currencies.getSign() == null) {
            currencies = null;
        }
        return Optional.ofNullable(currencies);
    }

    public Optional<Currencies> getByCode(String code) throws SQLException {
        String sqlQuery = "SELECT * FROM Currencies WHERE Code=?";
        Currencies currencies = new Currencies();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, code);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            long currenciesId = resultSet.getLong("ID");
            currencies.setId(currenciesId);
            String currenciesCode = resultSet.getString("Code");
            currencies.setCode(currenciesCode);
            String currenciesFullName = resultSet.getString("FullName");
            currencies.setFullName(currenciesFullName);
            String currenciesSign = resultSet.getString("Sign");
            currencies.setSign(currenciesSign);
        }
        connection.close();
        if (currencies.getCode() == null || currencies.getFullName() == null || currencies.getSign() == null) {
            currencies = null;
        }
        return Optional.ofNullable(currencies);
    }
}
