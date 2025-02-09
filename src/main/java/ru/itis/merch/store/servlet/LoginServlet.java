package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.exception.IncorrectCredentialsException;
import ru.itis.merch.store.exception.UserWithSpecifiedEmailNotFoundException;
import ru.itis.merch.store.service.AuthService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init(ServletConfig config) {
        authService = (AuthService) config.getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exception = req.getParameter("exception");
        if (exception != null) req.setAttribute("exception", exception);
        req.getRequestDispatcher("/login.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        boolean rememberMe = req.getParameter("rememberMe") != null;
        try {
            String sessionId = null;
            if (rememberMe) {
                sessionId = UUID.randomUUID().toString();
            }
            User user = authService.loginUser(email, password, sessionId);
            req.getSession().setAttribute("user", user);
            if (rememberMe) {
                Cookie cookie = new Cookie("session-id", sessionId);
                cookie.setPath("/");
                cookie.setMaxAge(86400);
                resp.addCookie(cookie);
            }
            if (user.getStatus().equals("ADMIN")) {
                resp.sendRedirect("/admin-panel");
            } else {
                resp.sendRedirect("/");
            }
        } catch (IncorrectCredentialsException | UserWithSpecifiedEmailNotFoundException exception) {
            resp.sendRedirect("/login?exception=" + exception.getMessage());
        }
    }
}
