package main.dao;

import main.entity.Currencies;
import main.entity.ExchangeRates;
import main.utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements Dao<ExchangeRates> {
    private final String path = Utils.getDataBasePath();

    @Override
    public List<ExchangeRates> getAll() throws SQLException {
        String sqlQuery = "SELECT * FROM ExchangeRates";
        List<ExchangeRates> listExchangeRates = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ExchangeRates exchangeRates = new ExchangeRates();
            int exchangeId = resultSet.getInt("ID");
            exchangeRates.setId(exchangeId);
            Optional<Currencies> baseOpt = new CurrenciesDao().getById(resultSet.getInt("BaseCurrencyId"));
            if (baseOpt.isPresent()) {
                Currencies exchangeBaseId = baseOpt.get();
                exchangeRates.setBaseCurrency(exchangeBaseId);
            }
            Optional<Currencies> targetOpt = new CurrenciesDao().getById(resultSet.getInt("TargetCurrencyId"));
            if (targetOpt.isPresent()) {
                Currencies exchangeTargetId = targetOpt.get();
                exchangeRates.setTargetCurrency(exchangeTargetId);
            }
            double exchangeRate = resultSet.getDouble("Rate");
            exchangeRates.setRate(exchangeRate);
            listExchangeRates.add(exchangeRates);
        }
        connection.close();
        return listExchangeRates;
    }

    @Override
    public void save(ExchangeRates exchangeRates) throws SQLException {
        String sqlQuery = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, exchangeRates.getBaseCurrency().getId());
        statement.setInt(2, exchangeRates.getTargetCurrency().getId());
        statement.setDouble(3, exchangeRates.getRate());
        statement.executeUpdate();
        connection.close();
    }

    @Override
    public void update(ExchangeRates exchangeRates) throws SQLException {
        String sqlQuery = "UPDATE ExchangeRates SET BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ? WHERE ID = ?";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, exchangeRates.getBaseCurrency().getId());
        statement.setInt(2, exchangeRates.getTargetCurrency().getId());
        statement.setDouble(3, exchangeRates.getRate());
        statement.setInt(4,exchangeRates.getId());
        statement.executeUpdate();
        connection.close();
    }

    @Override
    public Optional<ExchangeRates> getById(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM ExchangeRates WHERE ID=?";
        ExchangeRates exchangeRates = new ExchangeRates();
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
            int exchangeId = resultSet.getInt("ID");
            exchangeRates.setId(exchangeId);
            Optional<Currencies> baseOpt = new CurrenciesDao().getById(resultSet.getInt("BaseCurrencyId"));
            if (baseOpt.isPresent()) {
                Currencies exchangeBaseId = baseOpt.get();
                exchangeRates.setBaseCurrency(exchangeBaseId);
            }
            Optional<Currencies> targetOpt = new CurrenciesDao().getById(resultSet.getInt("TargetCurrencyId"));
            if (targetOpt.isPresent()) {
                Currencies exchangeTargetId = targetOpt.get();
                exchangeRates.setTargetCurrency(exchangeTargetId);
            }
            double exchangeRate = resultSet.getDouble("Rate");
            exchangeRates.setRate(exchangeRate);
        }
        connection.close();
        return Optional.ofNullable(exchangeRates);
    }

    public Optional<ExchangeRates> getByCode(String code) throws SQLException {
        String baseCode = code.substring(0,3);
        String targetCode = code.substring(3);
        Optional<Currencies> checkingBaseOpt = new CurrenciesDao().getByCode(baseCode);
        Optional<Currencies> checkingTargetOpt = new CurrenciesDao().getByCode(targetCode);
        if (checkingBaseOpt.isPresent() && checkingTargetOpt.isPresent()) {
            int baseId = checkingBaseOpt.get().getId();
            int targetId =checkingTargetOpt.get().getId();
            String sqlQuery = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? AND TargetCurrencyId=?";
            ExchangeRates exchangeRates = new ExchangeRates();
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException();
            }
            Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, baseId);
            statement.setInt(2, targetId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int exchangeId = resultSet.getInt("ID");
                exchangeRates.setId(exchangeId);
                Optional<Currencies> baseOpt = new CurrenciesDao().getById(resultSet.getInt("BaseCurrencyId"));
                if (baseOpt.isPresent()) {
                    Currencies exchangeBaseId = baseOpt.get();
                    exchangeRates.setBaseCurrency(exchangeBaseId);
                }
                Optional<Currencies> targetOpt = new CurrenciesDao().getById(resultSet.getInt("TargetCurrencyId"));
                if (targetOpt.isPresent()) {
                    Currencies exchangeTargetId = targetOpt.get();
                    exchangeRates.setTargetCurrency(exchangeTargetId);
                }
                double exchangeRate = resultSet.getDouble("Rate");
                exchangeRates.setRate(exchangeRate);
            }
            connection.close();
            if (exchangeRates.getBaseCurrency() == null || exchangeRates.getTargetCurrency() == null) {
                exchangeRates = null;
            }
            return Optional.ofNullable(exchangeRates);
        } else {
            return Optional.empty();
        }
    }
}
