package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.service.CartService;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/carts/change-cart-item")
public class ChangeCartItemQuantityServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        cartService = (CartService) config.getServletContext().getAttribute("cartService");
        super.init(config);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        User user = (User) req.getSession().getAttribute("user");
        String json = sb.toString();
        cartService.changeQuantityOfCartItem(json, user);
    }
}
