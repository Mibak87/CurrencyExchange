package main.dao;

import main.entity.Currencies;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao implements Dao<Currencies> {

    @Override
    public List<Currencies> getAll() {
        String sqlQuery = "SELECT * FROM Currencies";
        List<Currencies> listCurrencies = new ArrayList<>();
        try (Connection connection = ConnectionDataBase.connectionDB()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listCurrencies;
    }

    @Override
    public void save(Currencies currencies) {
        String sqlQuery = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, currencies.getCode());
            statement.setString(2, currencies.getFullName());
            statement.setString(3, currencies.getSign());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Currencies currencies) {
        String sqlQuery = "UPDATE Currencies SET Code = ?, FullName = ?, Sign = ? WHERE ID = ?";
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, currencies.getCode());
            statement.setString(2, currencies.getFullName());
            statement.setString(3, currencies.getSign());
            statement.setInt(4,currencies.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Currencies getById(int id) {
        String sqlQuery = "SELECT * FROM Currencies WHERE ID=?";
        Currencies currencies = new Currencies();
        try (Connection connection = ConnectionDataBase.connectionDB()) {
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
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных.");
            e.printStackTrace();
        }
        return currencies;
    }

    public Currencies getByCode(String code) {
        String sqlQuery = "SELECT * FROM Currencies WHERE Code=?";
        Currencies currencies = new Currencies();
        try (Connection connection = ConnectionDataBase.connectionDB()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }
}
