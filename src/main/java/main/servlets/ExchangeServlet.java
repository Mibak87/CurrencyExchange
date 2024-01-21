package main.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.error.ErrorMessage;
import main.dto.ConvertedAmount;
import main.service.Converting;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

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
            Optional<ConvertedAmount> convertedAmountOptional = converting.Convert();
            if (convertedAmountOptional.isPresent()) {
                objectMapper.writeValue(print, convertedAmountOptional.get());
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(print, new ErrorMessage("Курс обмена не найден."));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
