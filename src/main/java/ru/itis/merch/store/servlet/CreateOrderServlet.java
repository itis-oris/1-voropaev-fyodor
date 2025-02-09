package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.exception.NotEnoughActivityPoints;
import ru.itis.merch.store.exception.NotEnoughProductsInStock;
import ru.itis.merch.store.service.OrderService;

import java.io.IOException;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {

    private OrderService orderService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        orderService = (OrderService) config.getServletContext().getAttribute("orderService");
        super.init(config);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (!"Подтверждён".equals(user.getStatus())) {
            resp.setStatus(402);
            resp.getWriter().println("Для создания заказов, пользователь должен быть подтверждён");
            return;
        }
        try {
            orderService.createOrder(user.getId());
        } catch (NotEnoughProductsInStock exception) {
            resp.setStatus(400);
            resp.getWriter().println(exception.getMessage());
        } catch (NotEnoughActivityPoints exception) {
            resp.setStatus(401);
            resp.getWriter().println(exception.getMessage());
        }
    }
}
