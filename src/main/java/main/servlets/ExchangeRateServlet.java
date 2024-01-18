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
import java.sql.SQLException;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangerate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeCode = request.getPathInfo().substring(1);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (!exchangeCode.isEmpty()) {
            try {
                ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(exchangeCode);
                if (exchangeRates != null) {
                    objectMapper.writeValue(print, exchangeRates);
                    System.out.println(print);
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
                if (new ExchangeRatesDao().getByCode(exchangeCode) != null) {
                    String rateString = request.getParameter("rate");
                    if (rateString != null) {
                        ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(exchangeCode);
                        double rate = Double.parseDouble(rateString);
                        exchangeRates.setRate(rate);
                        new ExchangeRatesDao().update(exchangeRates);

                        ExchangeRates responseExchange = new ExchangeRatesDao().getByCode(exchangeCode);
                        objectMapper.writeValue(print, responseExchange);
                        response.setStatus(HttpServletResponse.SC_OK);
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