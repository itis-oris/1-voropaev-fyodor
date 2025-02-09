package ru.itis.merch.store.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.dao.UserDAO;
import ru.itis.merch.store.entity.User;

import java.io.IOException;

@WebFilter("*")
public class UserFilter extends HttpFilter {

    private UserDAO userDAO;

    @Override
    public void init(FilterConfig config) throws ServletException {
        userDAO = (UserDAO) config.getServletContext().getAttribute("userDAO");
        super.init(config);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getSession().getAttribute("user") != null) {
            User user = (User) req.getSession().getAttribute("user");
            User newUser = userDAO.findByEmail(user.getEmail()).orElse(user);
            req.getSession().setAttribute("user", newUser);
            req.setAttribute("user", newUser);
        }
        if (req.getSession().getAttribute("role") != null) {
            req.setAttribute("role", req.getSession().getAttribute("role"));
        }
        chain.doFilter(req, res);
    }
}
