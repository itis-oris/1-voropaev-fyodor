package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.model.OrderForProfilePage;
import ru.itis.merch.store.service.OrderService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profiles/*")
public class ProfileServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        orderService = (OrderService) config.getServletContext().getAttribute("orderService");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.isBlank()) {
            req.getRequestDispatcher("/error404.ftl").forward(req, resp);
            return;
        }
        path = path.substring(1);
        try {
            long userId = Long.parseLong(path);
            User user = (User) req.getSession().getAttribute("user");
            if (user.getId() != userId) {
                req.getRequestDispatcher("/error403.ftl").forward(req, resp);
            }
            List<OrderForProfilePage> orders = orderService.findOrdersByUserId(userId);
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/profile.ftl").forward(req, resp);
        } catch (NumberFormatException exception) {
            req.getRequestDispatcher("/error404.ftl").forward(req, resp);
        }

    }
}
