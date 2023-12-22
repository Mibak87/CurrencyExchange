package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.CurrenciesDao;
import main.entity.Currencies;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currencyCode = request.getPathInfo().substring(1);
        if (!currencyCode.isEmpty()) {
            Currencies currencies = new CurrenciesDao().getByCode(currencyCode);
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter print = response.getWriter();
            objectMapper.writeValue(print, currencies);
            System.out.println(print);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
