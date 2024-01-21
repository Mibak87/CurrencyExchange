package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.CurrenciesDao;
import main.dao.ExchangeRatesDao;
import main.entity.Currencies;
import main.entity.ExchangeRates;
import main.error.ErrorMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangerates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        try {
            List<ExchangeRates> exchangeRates = new ExchangeRatesDao().getAll();
            objectMapper.writeValue(print, exchangeRates);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String code = baseCurrencyCode + targetCurrencyCode;
        String rateString = request.getParameter("rate");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (baseCurrencyCode != null && targetCurrencyCode != null && rateString != null) {
            BigDecimal rate = new BigDecimal(rateString);
            try {
                if (new ExchangeRatesDao().getByCode(code).isEmpty()) {
                    Optional<Currencies> baseCurrencyOptional = new CurrenciesDao().getByCode(baseCurrencyCode);
                    Optional<Currencies> targetCurrencyOptional = new CurrenciesDao().getByCode(targetCurrencyCode);
                    if (baseCurrencyOptional.isPresent() && targetCurrencyOptional.isPresent()) {
                        ExchangeRates exchangeRates = new ExchangeRates();
                        exchangeRates.setBaseCurrency(baseCurrencyOptional.get());
                        exchangeRates.setTargetCurrency(targetCurrencyOptional.get());
                        exchangeRates.setRate(rate);
                        new ExchangeRatesDao().save(exchangeRates);
                        Optional<ExchangeRates> exchangeOptionalResponse = new ExchangeRatesDao().getByCode(code);
                        if (exchangeOptionalResponse.isPresent()) {
                            objectMapper.writeValue(print, exchangeOptionalResponse.get());
                            response.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            objectMapper.writeValue(print, new ErrorMessage("Вставка не удалась."));
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        objectMapper.writeValue(print, new ErrorMessage(
                                "Одна (или обе) валюта из валютной пары не существует в базе данных."));
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    objectMapper.writeValue(print, new ErrorMessage("Валютная пара с таким кодом уже существует."));
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(print, new ErrorMessage("Отсутствует нужное поле формы."));
        }
    }
}
