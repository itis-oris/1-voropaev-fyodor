package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.exception.UserWithSpecifiedEmailAlreadyExistException;
import ru.itis.merch.store.service.AuthService;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init(ServletConfig config) {
        authService = (AuthService) config.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exception = req.getParameter("exception");
        if (exception != null) req.setAttribute("exception", exception);
        req.getRequestDispatcher("/registration.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            authService.registerUser(firstName, lastName, email, password);
            resp.sendRedirect("/login");
        } catch (UserWithSpecifiedEmailAlreadyExistException exception) {
            resp.sendRedirect("/registration?exception=" + exception.getMessage());
        }
    }
}
