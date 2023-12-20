package main.dao;

import main.entity.Currencies;
import main.entity.ExchangeRates;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao implements Dao<ExchangeRates> {
    private List<ExchangeRates> exchangeRates;

    @Override
    public List<ExchangeRates> getAll() {
        String sqlQuery = "SELECT * FROM ExchangeRates";
        List<ExchangeRates> listExchangeRates = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src\\main\\java\\main\\database\\CurrencyExchange.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ExchangeRates exchangeRates = new ExchangeRates();
                int exchangeId = resultSet.getInt("ID");
                exchangeRates.setId(exchangeId);
                int exchangeBaseId = resultSet.getInt("BaseCurrencyId");
                exchangeRates.setBaseCurrencyId(exchangeBaseId);
                int exchangeTaggetId = resultSet.getInt("TargetCurrencyId");
                exchangeRates.setTargetCurrencyId(exchangeTaggetId);
                double exchangeRate = resultSet.getDouble("Rate");
                exchangeRates.setRate(exchangeRate);
                listExchangeRates.add(exchangeRates);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listExchangeRates;
    }

    @Override
    public void save(ExchangeRates exchangeRates) {
        String sqlQuery = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src\\main\\java\\main\\database\\CurrencyExchange.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, exchangeRates.getBaseCurrencyId());
            statement.setInt(2, exchangeRates.getTargetCurrencyId());
            statement.setDouble(3, exchangeRates.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ExchangeRates exchangeRates) {
        String sqlQuery = "UPDATE ExchangeRates SET BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ? WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src\\main\\java\\main\\database\\CurrencyExchange.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, exchangeRates.getBaseCurrencyId());
            statement.setInt(2, exchangeRates.getTargetCurrencyId());
            statement.setDouble(3, exchangeRates.getRate());
            statement.setInt(4,exchangeRates.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExchangeRates getById(int id) {
        String sqlQuery = "SELECT * FROM ExchangeRates WHERE ID=?";
        ExchangeRates exchangeRates = new ExchangeRates();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src\\main\\java\\main\\database\\CurrencyExchange.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int exchangeId = resultSet.getInt("ID");
                exchangeRates.setId(exchangeId);
                int exchangeBaseId = resultSet.getInt("BaseCurrencyId");
                exchangeRates.setBaseCurrencyId(exchangeBaseId);
                int exchangeTaggetId = resultSet.getInt("TargetCurrencyId");
                exchangeRates.setTargetCurrencyId(exchangeTaggetId);
                double exchangeRate = resultSet.getDouble("Rate");
                exchangeRates.setRate(exchangeRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }
}
