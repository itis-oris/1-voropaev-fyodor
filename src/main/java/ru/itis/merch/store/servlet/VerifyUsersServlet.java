package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-panel/verify-users")
public class VerifyUsersServlet extends HttpServlet {

    private static final String DEFAULT_USER_UNCONFIRMED_STATUS = "Не подтверждён";

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = (UserService) config.getServletContext().getAttribute("userService");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userService.getUsersByStatus(DEFAULT_USER_UNCONFIRMED_STATUS);
        req.setAttribute("users", users);
        req.getRequestDispatcher("/user-verify.ftl").forward(req, resp);
    }
}
