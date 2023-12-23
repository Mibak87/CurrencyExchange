package main.dao;

import main.entity.Currencies;
import main.entity.ExchangeRates;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao implements Dao<ExchangeRates> {

    @Override
    public List<ExchangeRates> getAll() {
        String sqlQuery = "SELECT * FROM ExchangeRates";
        List<ExchangeRates> listExchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ExchangeRates exchangeRates = new ExchangeRates();
                int exchangeId = resultSet.getInt("ID");
                exchangeRates.setId(exchangeId);
                Currencies exchangeBaseId = new CurrenciesDao().getById(resultSet.getInt("BaseCurrencyId"));
                exchangeRates.setBaseCurrencyId(exchangeBaseId);
                Currencies exchangeTaggetId = new CurrenciesDao().getById(resultSet.getInt("TargetCurrencyId"));
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
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, exchangeRates.getBaseCurrencyId().getId());
            statement.setInt(2, exchangeRates.getTargetCurrencyId().getId());
            statement.setDouble(3, exchangeRates.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ExchangeRates exchangeRates) {
        String sqlQuery = "UPDATE ExchangeRates SET BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ? WHERE ID = ?";
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, exchangeRates.getBaseCurrencyId().getId());
            statement.setInt(2, exchangeRates.getTargetCurrencyId().getId());
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
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int exchangeId = resultSet.getInt("ID");
                exchangeRates.setId(exchangeId);
                Currencies exchangeBaseId = new CurrenciesDao().getById(resultSet.getInt("BaseCurrencyId"));
                exchangeRates.setBaseCurrencyId(exchangeBaseId);
                Currencies exchangeTaggetId = new CurrenciesDao().getById(resultSet.getInt("TargetCurrencyId"));
                exchangeRates.setTargetCurrencyId(exchangeTaggetId);
                double exchangeRate = resultSet.getDouble("Rate");
                exchangeRates.setRate(exchangeRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }

    public ExchangeRates getByCode(String code) {
        String baseCode = code.substring(0,3);
        String targetCode = code.substring(3);
        int baseId = new CurrenciesDao().getByCode(baseCode).getId();
        int targetId = new CurrenciesDao().getByCode(targetCode).getId();
        String sqlQuery = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? AND TargetCurrencyId=?";
        ExchangeRates exchangeRates = new ExchangeRates();
        try (Connection connection = ConnectionDataBase.connectionDB()) {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, baseId);
            statement.setInt(2, targetId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int exchangeId = resultSet.getInt("ID");
                exchangeRates.setId(exchangeId);
                Currencies exchangeBaseId = new CurrenciesDao().getById(baseId);
                exchangeRates.setBaseCurrencyId(exchangeBaseId);
                Currencies exchangeTaggetId = new CurrenciesDao().getById(targetId);
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
