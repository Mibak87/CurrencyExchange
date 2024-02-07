package main.config;

import main.dao.CurrenciesDao;

import java.io.File;
import java.net.URL;

public class Config {
    public static String getDataBasePath() {
        URL resource = CurrenciesDao.class.getClassLoader().getResource("CurrencyExchange.sqlite");
        try {
            return new File(resource.toURI()).getAbsolutePath();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
