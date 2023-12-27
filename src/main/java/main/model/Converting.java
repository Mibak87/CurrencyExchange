package main.model;

import main.dao.CurrenciesDao;
import main.dao.ExchangeRatesDao;
import main.entity.ExchangeRates;

import java.sql.SQLException;

public class Converting {
    private final String baseCode;
    private final String targetCode;
    private final String amountString;

    public Converting(String baseCode, String targetCode, String amountString) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.amountString = amountString;
    }

    public ConvertedAmount Convert() throws SQLException {
        double amount = Double.parseDouble(amountString);
        double rate = getRate();
        if (rate < 0) {
            return null;
        } else {
            double convertedValue = amount * rate;
            ConvertedAmount convertedAmount = new ConvertedAmount();
            convertedAmount.setBaseCurrency(new CurrenciesDao().getByCode(baseCode));
            convertedAmount.setTargetCurrency(new CurrenciesDao().getByCode(targetCode));
            convertedAmount.setRate(rate);
            convertedAmount.setAmount(amount);
            convertedAmount.setConvertedAmount(convertedValue);
            return convertedAmount;
        }
    }

    private double getRate() throws SQLException {
        double rate = getDirectOrReverseRate(baseCode,targetCode);
        if (rate < 0) {
            return getCrossConvertationRate();
        } else {
            return rate;
        }
    }

    private double getDirectOrReverseRate(String baseCode, String targetCode) throws SQLException {
        ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(baseCode + targetCode);
        if (exchangeRates == null) {
            ExchangeRates exchangeReverseRates = new ExchangeRatesDao().getByCode(targetCode + baseCode);
            if (exchangeReverseRates == null) {
               return -1;
            } else {
                return (1 / exchangeReverseRates.getRate());
            }
        } else {
            return exchangeRates.getRate();
        }
    }

    private double getCrossConvertationRate() throws SQLException {
        double firstRate = getDirectOrReverseRate("USD",baseCode);
        if (firstRate < 0) {
            return -1;
        } else {
            double secondRate = getDirectOrReverseRate("USD",targetCode);
            return firstRate/secondRate;
        }
    }
}
