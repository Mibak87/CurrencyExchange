package main.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import main.entity.Currencies;

import java.math.BigDecimal;

@JsonPropertyOrder({"baseCurrency", "targetCurrency", "rate", "amount", "convertedAmount"})
public class ConvertedAmount {
    private Currencies baseCurrency;
    private Currencies targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
