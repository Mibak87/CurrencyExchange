package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.dao.CurrenciesDao;
import main.entity.Currencies;
import main.error.ErrorMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currencyCode = request.getPathInfo().substring(1);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (!currencyCode.isEmpty()) {
            try {
                Currencies currencies = new CurrenciesDao().getByCode(currencyCode);
                if (currencies != null) {
                    objectMapper.writeValue(print, currencies);
                    System.out.println(print);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    objectMapper.writeValue(print, new ErrorMessage("The currency was not found."));
                    System.out.println(print);
                    //response.sendError(HttpServletResponse.SC_NOT_FOUND,"The currency was not found.");
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(print, new ErrorMessage("The database is unavailable."));
                System.out.println(print);
                //response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"The database is unavailable.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(print, new ErrorMessage("The currency code is missing from the address."));
            System.out.println(print);
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST,"The currency code is missing from the address.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
