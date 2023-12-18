package main.dao;

import main.entity.ExchangeRates;

import java.util.List;

public class ExchangeRatesDao implements Dao<ExchangeRates> {
    private List<ExchangeRates> exchangeRates;

    @Override
    public List<ExchangeRates> getAll() {
        return null;
    }

    @Override
    public void save(ExchangeRates exchangeRates) {

    }

    @Override
    public void update(ExchangeRates exchangeRates, String[] params) {

    }

    @Override
    public ExchangeRates getById(int id) {
        return null;
    }
}
