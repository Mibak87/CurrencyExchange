package main.dao;

import main.entity.Currencies;
import main.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao implements Dao<Currencies> {
    private final String path = Utils.getDataBasePath();
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
            int currenciesId = resultSet.getInt("ID");
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
        statement.setInt(4,currencies.getId());
        statement.executeUpdate();
        connection.close();
    }

    @Override
    public Currencies getById(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM Currencies WHERE ID=?";
        Currencies currencies = new Currencies();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            int currenciesId = resultSet.getInt("ID");
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
            return null;
        }
        return currencies;
    }

    public Currencies getByCode(String code) throws SQLException {
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
            int currenciesId = resultSet.getInt("ID");
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
            return null;
        }
        return currencies;
    }
}
