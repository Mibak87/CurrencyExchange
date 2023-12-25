package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.CurrenciesDao;
import main.entity.Currencies;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Currencies", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        List<Currencies> currencies = new CurrenciesDao().getAll();
        objectMapper.writeValue(print,currencies);
        System.out.println(print);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        Currencies currencies = new Currencies();
        currencies.setFullName(name);
        currencies.setCode(code);
        currencies.setSign(sign);
        new CurrenciesDao().save(currencies);

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        Currencies responseCurrencies = new CurrenciesDao().getByCode(code);
        objectMapper.writeValue(print,currencies);
        System.out.println(print);
    }
}
