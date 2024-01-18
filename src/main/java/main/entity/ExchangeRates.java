package main.entity;

public class ExchangeRates {
    private int id;
    private Currencies baseCurrency;
    private Currencies targetCurrency;
    private double rate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currencies getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currencies baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currencies getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currencies targetCurrency) {
        this.targetCurrency = targetCurrency;
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
                ", baseCurrencyId='" + baseCurrency + '\'' +
                ", targetCurrencyId='" + targetCurrency + '\'' +
                ", rate=" + rate +
                '}';
    }
}
