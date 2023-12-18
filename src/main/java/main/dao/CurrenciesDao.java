package main.dao;

import main.entity.Currencies;

import java.util.List;

public class CurrenciesDao implements Dao<Currencies> {
    private List<Currencies> currency;

    @Override
    public List<Currencies> getAll() {
        return null;
    }

    @Override
    public void save(Currencies currencies) {

    }

    @Override
    public void update(Currencies currencies, String[] params) {

    }

    @Override
    public Currencies getById(int id) {
        return null;
    }
}
