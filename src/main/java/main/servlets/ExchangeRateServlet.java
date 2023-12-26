package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.ExchangeRatesDao;
import main.entity.ExchangeRates;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangerate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeCode = request.getPathInfo().substring(1);
        if (!exchangeCode.isEmpty()) {
            try {
                ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(exchangeCode);
                if (exchangeRates != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    PrintWriter print = response.getWriter();
                    objectMapper.writeValue(print, exchangeRates);
                    System.out.println(print);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,"The exchange rate for the currencies was not found.");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The currency codes is missing from the address.");
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
        if (!exchangeCode.isEmpty()) {
            try {
                if (new ExchangeRatesDao().getByCode(exchangeCode) != null) {
                    String rateString = request.getParameter("rate");
                    if (rateString != null) {
                        ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(exchangeCode);
                        double rate = Double.parseDouble(rateString);
                        exchangeRates.setRate(rate);
                        new ExchangeRatesDao().update(exchangeRates);

                        ObjectMapper objectMapper = new ObjectMapper();
                        PrintWriter print = response.getWriter();
                        ExchangeRates responseExchange = new ExchangeRatesDao().getByCode(exchangeCode);
                        objectMapper.writeValue(print, responseExchange);
                        System.out.println(print);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The required form field is missing.");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,"The currency pair is missing from the database.");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}