package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.error.ErrorMessage;
import main.service.ConvertedAmount;
import main.service.Converting;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "ExchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCode = request.getParameter("from");
        String targetCode = request.getParameter("to");
        String amountString = request.getParameter("amount");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        try {
            Converting converting = new Converting(baseCode,targetCode,amountString);
            ConvertedAmount convertedAmount = converting.Convert();
            if (convertedAmount != null) {
                objectMapper.writeValue(print, convertedAmount);
                System.out.println(print);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(print, new ErrorMessage("The exchange rate was not found."));
                System.out.println(print);
                //response.sendError(HttpServletResponse.SC_NOT_FOUND,"The exchange rate was not found.");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(print, new ErrorMessage("The database is unavailable."));
            System.out.println(print);
            //response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The database is unavailable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
