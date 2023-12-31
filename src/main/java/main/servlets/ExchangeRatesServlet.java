package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.CurrenciesDao;
import main.dao.ExchangeRatesDao;
import main.entity.ExchangeRates;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangerates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter print = response.getWriter();
            List<ExchangeRates> exchangeRates = new ExchangeRatesDao().getAll();
            objectMapper.writeValue(print, exchangeRates);
            System.out.println(print);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String code = baseCurrencyCode + targetCurrencyCode;
        String rateString = request.getParameter("rate");
        if (baseCurrencyCode != null && targetCurrencyCode != null && rateString != null) {
            double rate = Double.parseDouble(rateString);
            try {
                if (new ExchangeRatesDao().getByCode(code) == null) {
                    if (new CurrenciesDao().getByCode(baseCurrencyCode) != null &&
                            new CurrenciesDao().getByCode(targetCurrencyCode) != null) {
                        ExchangeRates exchangeRates = new ExchangeRates();
                        exchangeRates.setBaseCurrencyId(new CurrenciesDao().getByCode(baseCurrencyCode));
                        exchangeRates.setTargetCurrencyId(new CurrenciesDao().getByCode(targetCurrencyCode));
                        exchangeRates.setRate(rate);
                        new ExchangeRatesDao().save(exchangeRates);

                        ObjectMapper objectMapper = new ObjectMapper();
                        PrintWriter print = response.getWriter();
                        ExchangeRates responceExchangeRates = new ExchangeRatesDao().getByCode(code);
                        objectMapper.writeValue(print, responceExchangeRates);
                        System.out.println(print);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.sendError(HttpServletResponse.SC_NOT_FOUND,
                                "One (or both) currency from the currency pair does not exist in the database.");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.sendError(HttpServletResponse.SC_CONFLICT,"A currency pair with this code already exists.");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The database is unavailable.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The required form field is missing.");
        }
    }
}
