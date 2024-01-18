package main.utils;

import main.dao.CurrenciesDao;

import java.io.File;
import java.net.URL;

public class Utils {
    public static String getDataBasePath() {
        URL resource = CurrenciesDao.class.getClassLoader().getResource("CurrencyExchange.sqlite");
        String path = null;
        try {
            path = new File(resource.toURI()).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
