package main.entity;

import java.math.BigDecimal;

public class ExchangeRates {
    private int id;
    private String baseCurrencyId;
    private String targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRates(int id, String baseCurrencyId, String targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(String baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public String getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(String targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
