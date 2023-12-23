package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.ExchangeRatesDao;
import main.entity.ExchangeRates;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangerate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exchangeCode = request.getPathInfo().substring(1);
        if (!exchangeCode.isEmpty()) {
            ExchangeRates exchangeRates = new ExchangeRatesDao().getByCode(exchangeCode);
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter print = response.getWriter();
            objectMapper.writeValue(print, exchangeRates);
            System.out.println(print);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}