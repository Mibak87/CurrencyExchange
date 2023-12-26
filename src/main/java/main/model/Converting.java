package main.model;

import main.dao.CurrenciesDao;
import main.dao.ExchangeRatesDao;
import main.entity.Currencies;
import main.entity.ExchangeRates;

import java.sql.SQLException;

public class Converting {
    private String baseCode;
    private String targetCode;
    private String amountString;
    private double amount;

    public Converting(String baseCode, String targetCode, String amountString) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.amountString = amountString;
    }

    public ConvertedAmount Convert() throws SQLException {
        amount = Double.parseDouble(amountString);
        ConvertedAmount convertedAmount = new ConvertedAmount();
        ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(baseCode + targetCode);
        double rate = exchangeRates.getRate();
        double convertedValue = amount * rate;
        convertedAmount.setBaseCurrency(exchangeRates.getBaseCurrencyId());
        convertedAmount.setTargetCurrency(exchangeRates.getTargetCurrencyId());
        convertedAmount.setRate(rate);
        convertedAmount.setAmount(amount);
        convertedAmount.setConvertedAmount(convertedValue);
        return convertedAmount;
    }
}
