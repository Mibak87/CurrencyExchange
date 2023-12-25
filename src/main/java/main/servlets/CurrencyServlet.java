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

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currencyCode = request.getPathInfo().substring(1);
        if (!currencyCode.isEmpty()) {
            try {
                Currencies currencies = new CurrenciesDao().getByCode(currencyCode);
                if (currencies != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    PrintWriter print = response.getWriter();
                    objectMapper.writeValue(print, currencies);
                    System.out.println(print);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,"The currency was not found.");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The currency code is missing from the address.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
