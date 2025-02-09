package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.model.ProductForCartPage;
import ru.itis.merch.store.service.CartService;

import java.io.IOException;
import java.util.List;

@WebServlet("/carts/*")
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        cartService = (CartService) config.getServletContext().getAttribute("cartService");
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
            List<ProductForCartPage> cartItems = cartService.findCartItemsByUserId(userId);
            req.setAttribute("cartItems", cartItems);
            req.getRequestDispatcher("/cart.ftl").forward(req, resp);
        } catch (NumberFormatException exception) {
            req.getRequestDispatcher("/error404.ftl").forward(req, resp);
        }
    }
}
