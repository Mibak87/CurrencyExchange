package main.entity;

import java.math.BigDecimal;

public class ExchangeRates {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private double rate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "id=" + id +
                ", baseCurrencyId='" + baseCurrencyId + '\'' +
                ", targetCurrencyId='" + targetCurrencyId + '\'' +
                ", rate=" + rate +
                '}';
    }
}
