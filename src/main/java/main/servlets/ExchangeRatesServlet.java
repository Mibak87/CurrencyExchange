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
        String rateString = request.getParameter("rate");
        double rate = Double.parseDouble(rateString);
        try {
            ExchangeRates exchangeRates = new ExchangeRates();
            exchangeRates.setBaseCurrencyId(new CurrenciesDao().getByCode(baseCurrencyCode));
            exchangeRates.setTargetCurrencyId(new CurrenciesDao().getByCode(targetCurrencyCode));
            exchangeRates.setRate(rate);
            new ExchangeRatesDao().save(exchangeRates);

            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter print = response.getWriter();
            ExchangeRates responceExchangeRates = new ExchangeRatesDao().getByCode(baseCurrencyCode + targetCurrencyCode);
            objectMapper.writeValue(print, responceExchangeRates);
            System.out.println(print);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
        }
    }
}
