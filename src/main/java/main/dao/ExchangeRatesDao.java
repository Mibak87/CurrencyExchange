package main.dao;

import main.entity.Currencies;
import main.entity.ExchangeRates;
import main.utils.Utils;

import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao implements Dao<ExchangeRates> {
    private final String path = Utils.getDataBasePath();

    @Override
    public List<ExchangeRates> getAll() throws SQLException {
        String sqlQuery = """
                            SELECT
                                er.ID AS id,
                                base_cur.ID AS base_id,
                                base_cur.Code AS base_code,
                                base_cur.FullName AS base_name,
                                base_cur.Sign AS base_sign,
                                target_cur.ID AS target_id,
                                target_cur.Code AS target_code,
                                target_cur.FullName AS target_name,
                                target_cur.Sign AS target_sign,
                                er.rate AS rate
                            FROM ExchangeRates AS er
                            JOIN Currencies AS base_cur ON
                                er.BaseCurrencyId = base_cur.ID
                            JOIN Currencies AS target_cur ON
                                er.TargetCurrencyId = target_cur.ID
                            """;
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
            exchangeRates.setId(resultSet.getInt("id"));
            Currencies baseCurrency = new Currencies();
            baseCurrency.setId(resultSet.getInt("base_id"));
            baseCurrency.setCode(resultSet.getString("base_code"));
            baseCurrency.setFullName(resultSet.getString("base_name"));
            baseCurrency.setSign(resultSet.getString("base_sign"));
            exchangeRates.setBaseCurrency(baseCurrency);
            Currencies targetCurrency = new Currencies();
            targetCurrency.setId(resultSet.getInt("target_id"));
            targetCurrency.setCode(resultSet.getString("target_code"));
            targetCurrency.setFullName(resultSet.getString("target_name"));
            targetCurrency.setSign(resultSet.getString("target_sign"));
            exchangeRates.setTargetCurrency(targetCurrency);
            exchangeRates.setRate(resultSet.getBigDecimal("Rate"));
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
        statement.setBigDecimal(3, exchangeRates.getRate());
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
        statement.setBigDecimal(3, exchangeRates.getRate());
        statement.setInt(4,exchangeRates.getId());
        statement.executeUpdate();
        connection.close();
    }

    @Override
    public Optional<ExchangeRates> getById(int id) throws SQLException {
        String sqlQuery = """
                            SELECT
                                er.ID AS id,
                                base_cur.ID AS base_id,
                                base_cur.Code AS base_code,
                                base_cur.FullName AS base_name,
                                base_cur.Sign AS base_sign,
                                target_cur.ID AS target_id,
                                target_cur.Code AS target_code,
                                target_cur.FullName AS target_name,
                                target_cur.Sign AS target_sign,
                                er.rate AS rate
                            FROM ExchangeRates AS er
                            JOIN Currencies AS base_cur ON
                                er.BaseCurrencyId = base_cur.ID
                            JOIN Currencies AS target_cur ON
                                er.TargetCurrencyId = target_cur.ID
                            WHERE er.ID=?
                            """;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException();
        }
        Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        ExchangeRates exchangeRates = getExchangeRates(resultSet);
        connection.close();
        return Optional.of(exchangeRates);
    }

    public Optional<ExchangeRates> getByCode(String code) throws SQLException {
        String baseCode = code.substring(0,3);
        String targetCode = code.substring(3);
            String sqlQuery = """
                            SELECT
                                er.ID AS id,
                                base_cur.ID AS base_id,
                                base_cur.Code AS base_code,
                                base_cur.FullName AS base_name,
                                base_cur.Sign AS base_sign,
                                target_cur.ID AS target_id,
                                target_cur.Code AS target_code,
                                target_cur.FullName AS target_name,
                                target_cur.Sign AS target_sign,
                                er.rate AS rate
                            FROM ExchangeRates AS er
                            JOIN Currencies AS base_cur ON
                                er.BaseCurrencyId = base_cur.ID
                            JOIN Currencies AS target_cur ON
                                er.TargetCurrencyId = target_cur.ID
                            WHERE er.BaseCurrencyId=(SELECT ID FROM Currencies WHERE Code =?)
                            AND er.TargetCurrencyId=(SELECT ID FROM Currencies WHERE Code =?)
                            """;
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException();
            }
            Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            ResultSet resultSet = statement.executeQuery();
            ExchangeRates exchangeRates = getExchangeRates(resultSet);
            connection.close();
            if (exchangeRates.getBaseCurrency() == null || exchangeRates.getTargetCurrency() == null) {
                exchangeRates = null;
            }
            return Optional.ofNullable(exchangeRates);
    }

    private ExchangeRates getExchangeRates(ResultSet resultSet) throws SQLException {
        ExchangeRates exchangeRates = new ExchangeRates();
        while (resultSet.next()) {
            exchangeRates.setId(resultSet.getInt("id"));
            Currencies baseCurrency = new Currencies();
            baseCurrency.setId(resultSet.getInt("base_id"));
            baseCurrency.setCode(resultSet.getString("base_code"));
            baseCurrency.setFullName(resultSet.getString("base_name"));
            baseCurrency.setSign(resultSet.getString("base_sign"));
            exchangeRates.setBaseCurrency(baseCurrency);
            Currencies targetCurrency = new Currencies();
            targetCurrency.setId(resultSet.getInt("target_id"));
            targetCurrency.setCode(resultSet.getString("target_code"));
            targetCurrency.setFullName(resultSet.getString("target_name"));
            targetCurrency.setSign(resultSet.getString("target_sign"));
            exchangeRates.setTargetCurrency(targetCurrency);
            exchangeRates.setRate(resultSet.getBigDecimal("Rate"));
        }
        return exchangeRates;
    }
}
