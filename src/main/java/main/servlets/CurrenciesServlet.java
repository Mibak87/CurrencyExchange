package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.CurrenciesDao;
import main.entity.Currencies;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Currencies", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PrintWriter print = response.getWriter();
            List<Currencies> currencies = new CurrenciesDao().getAll();
            objectMapper.writeValue(print, currencies);
            System.out.println(print);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        if (name != null && code != null && sign != null) {
            try {
                if (new CurrenciesDao().getByCode(code) == null) {
                    Currencies currencies = new Currencies();
                    currencies.setFullName(name);
                    currencies.setCode(code);
                    currencies.setSign(sign);
                    new CurrenciesDao().save(currencies);

                    ObjectMapper objectMapper = new ObjectMapper();
                    PrintWriter print = response.getWriter();
                    Currencies responseCurrencies = new CurrenciesDao().getByCode(code);
                    objectMapper.writeValue(print, responseCurrencies);
                    System.out.println(print);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.sendError(HttpServletResponse.SC_CONFLICT,"A currency with this code already exists.");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The required form field is missing.");
        }
    }
}
