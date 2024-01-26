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
import java.util.List;
import java.util.Optional;

@WebServlet(name = "Currencies", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        try {
            List<Currencies> currencies = new CurrenciesDao().getAll();
            objectMapper.writeValue(print, currencies);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter print = response.getWriter();
        if (name != null && code != null && sign != null) {
            try {
                Currencies currencies = new Currencies();
                currencies.setFullName(name);
                currencies.setCode(code);
                currencies.setSign(sign);
                new CurrenciesDao().save(currencies);
                Optional<Currencies> currenciesOptionalResponse = new CurrenciesDao().getByCode(code);
                if (currenciesOptionalResponse.isPresent()) {
                    objectMapper.writeValue(print, currenciesOptionalResponse.get());
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    objectMapper.writeValue(print, new ErrorMessage("Вставка не удалась."));
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 19) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    objectMapper.writeValue(print, new ErrorMessage("Валютная пара с таким кодом уже существует."));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    objectMapper.writeValue(print, new ErrorMessage("Ошибка подключения к базе данных."));
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(print, new ErrorMessage("Отсутствует нужное поле формы."));
        }
    }
}
