package main.service;

import main.dao.CurrenciesDao;
import main.dao.ExchangeRatesDao;
import main.dto.ConvertedAmount;
import main.entity.ExchangeRates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;

public class Converting {
    private static final String CROSS_CURRENCY = "USD";

    private final String baseCode;
    private final String targetCode;
    private final String amountString;


    public Converting(String baseCode, String targetCode, String amountString) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.amountString = amountString;
    }

    public Optional<ConvertedAmount> Convert() throws SQLException {
        BigDecimal amount = new BigDecimal(amountString);
        BigDecimal rate = getRate();
        if (rate.signum() >= 0) {
            BigDecimal convertedValue = amount.multiply(rate);
            ConvertedAmount convertedAmount = new ConvertedAmount();
            convertedAmount.setBaseCurrency(new CurrenciesDao().getByCode(baseCode).get());
            convertedAmount.setTargetCurrency(new CurrenciesDao().getByCode(targetCode).get());
            convertedAmount.setRate(rate);
            convertedAmount.setAmount(amount);
            convertedAmount.setConvertedAmount(convertedValue);
            return Optional.of(convertedAmount);
        }
        return Optional.empty();
    }

    private BigDecimal getRate() throws SQLException {
        BigDecimal rate = getDirectOrReverseRate(baseCode,targetCode);
        if (rate.signum() < 0) {
            return getCrossConvertingRate();
        }
        return rate;
    }

    private BigDecimal getDirectOrReverseRate(String baseCode, String targetCode) throws SQLException {
        Optional<ExchangeRates> exchangeRatesOptional = new ExchangeRatesDao().getByCode(baseCode + targetCode);
        if (!exchangeRatesOptional.isPresent()) {
            Optional<ExchangeRates> reverseRatesOptional = new ExchangeRatesDao().getByCode(targetCode + baseCode);
            if (reverseRatesOptional.isPresent()) {
                return new BigDecimal(1).divide(reverseRatesOptional.get().getRate(),2,RoundingMode.DOWN);
            } else {
                return new BigDecimal(-1);
            }
        }
        return exchangeRatesOptional.get().getRate();
    }

    private BigDecimal getCrossConvertingRate() throws SQLException {
        BigDecimal firstRate = getDirectOrReverseRate(CROSS_CURRENCY,baseCode);
        if (firstRate.signum() >= 0) {
            BigDecimal secondRate = getDirectOrReverseRate(CROSS_CURRENCY,targetCode);
            return firstRate.divide(secondRate,2, RoundingMode.DOWN);
        }
        return new BigDecimal(-1);
    }
}
