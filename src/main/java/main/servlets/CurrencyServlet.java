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
import java.util.Optional;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currencyCode = request.getPathInfo().substring(1);
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (!currencyCode.isEmpty()) {
            try {
                Optional<Currencies> currenciesOptional = new CurrenciesDao().getByCode(currencyCode);
                if (currenciesOptional.isPresent()) {
                    objectMapper.writeValue(print, currenciesOptional.get());
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    objectMapper.writeValue(print, new ErrorMessage("Валюта не найдена."));
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(print, new ErrorMessage("Код валюты отсутствует в адресе."));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
