package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.ExchangeRatesDao;
import main.entity.ExchangeRates;
import main.error.ErrorMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangerate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeCode = request.getPathInfo().substring(1);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (!exchangeCode.isEmpty()) {
            try {
                Optional<ExchangeRates> exchangeRatesOptional = new ExchangeRatesDao().getByCode(exchangeCode);
                if (exchangeRatesOptional.isPresent()) {
                    objectMapper.writeValue(print, exchangeRatesOptional.get());
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    objectMapper.writeValue(print, new ErrorMessage("Обменный курс для этой пары валют не найден."));
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(print, new ErrorMessage("Коды валют отсутствуют в адресе."));
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if (method.equalsIgnoreCase("PATCH")) {
            this.doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeCode = request.getPathInfo().substring(1);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (!exchangeCode.isEmpty()) {
            try {
                Optional<ExchangeRates> exchangeRatesOptional = new ExchangeRatesDao().getByCode(exchangeCode);
                if (exchangeRatesOptional.isPresent()) {
                    String rateString = request.getParameter("rate");
                    if (rateString != null) {
                        BigDecimal rate = new BigDecimal(rateString);
                        ExchangeRates exchangeRates = exchangeRatesOptional.get();
                        exchangeRates.setRate(rate);
                        new ExchangeRatesDao().update(exchangeRates);
                        Optional<ExchangeRates> exchangeOptionalResponse = new ExchangeRatesDao().getByCode(exchangeCode);
                        if (exchangeOptionalResponse.isPresent()) {
                            objectMapper.writeValue(print, exchangeOptionalResponse.get());
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        objectMapper.writeValue(print, new ErrorMessage("Отсутствует нужное поле формы."));
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    objectMapper.writeValue(print, new ErrorMessage("Валютная пара отсутствует в базе данных."));
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}