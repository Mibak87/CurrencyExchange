package main.service;

import main.dao.CurrenciesDao;
import main.dao.ExchangeRatesDao;
import main.dto.ConvertedAmount;
import main.entity.ExchangeRates;

import java.sql.SQLException;
import java.util.Optional;

public class Converting {
    private final String baseCode;
    private final String targetCode;
    private final String amountString;
    private final String CROSS_CURRENCY = "USD";

    public Converting(String baseCode, String targetCode, String amountString) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.amountString = amountString;
    }

    public Optional<ConvertedAmount> Convert() throws SQLException {
        double amount = Double.parseDouble(amountString);
        double rate = getRate();
        if (rate < 0) {
            return Optional.empty();
        } else {
            double convertedValue = amount * rate;
            ConvertedAmount convertedAmount = new ConvertedAmount();
            convertedAmount.setBaseCurrency(new CurrenciesDao().getByCode(baseCode).get());
            convertedAmount.setTargetCurrency(new CurrenciesDao().getByCode(targetCode).get());
            convertedAmount.setRate(rate);
            convertedAmount.setAmount(amount);
            convertedAmount.setConvertedAmount(convertedValue);
            return Optional.of(convertedAmount);
        }
    }

    private double getRate() throws SQLException {
        double rate = getDirectOrReverseRate(baseCode,targetCode);
        if (rate < 0) {
            return getCrossConvertingRate();
        } else {
            return rate;
        }
    }

    private double getDirectOrReverseRate(String baseCode, String targetCode) throws SQLException {
        Optional<ExchangeRates> exchangeRatesOptional = new ExchangeRatesDao().getByCode(baseCode + targetCode);
        if (exchangeRatesOptional.isPresent()) {
            return exchangeRatesOptional.get().getRate();
        } else {
            Optional<ExchangeRates> reverseRatesOptional = new ExchangeRatesDao().getByCode(targetCode + baseCode);
            if (reverseRatesOptional.isPresent()) {
                return (1 / reverseRatesOptional.get().getRate());
            } else {
                return -1;
            }
        }
    }

    private double getCrossConvertingRate() throws SQLException {
        double firstRate = getDirectOrReverseRate(CROSS_CURRENCY,baseCode);
        if (firstRate < 0) {
            return -1;
        } else {
            double secondRate = getDirectOrReverseRate(CROSS_CURRENCY,targetCode);
            return firstRate/secondRate;
        }
    }
}
