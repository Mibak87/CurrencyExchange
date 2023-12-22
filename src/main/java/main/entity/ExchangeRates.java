package main.entity;

import java.math.BigDecimal;

public class ExchangeRates {
    private int id;
    private Currencies baseCurrencyId;
    private Currencies targetCurrencyId;
    private double rate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currencies getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Currencies baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Currencies getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Currencies targetCurrencyId) {
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
