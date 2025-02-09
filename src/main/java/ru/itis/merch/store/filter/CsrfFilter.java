package ru.itis.merch.store.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.exception.CsrfException;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

@WebFilter("*")
public class CsrfFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!req.getMethod().equals("POST")) {
            String csrfToken = UUID.randomUUID().toString();
            if (req.getSession().getAttribute("csrf-tokens") == null) {
                req.getSession().setAttribute("csrf-tokens", new HashSet<String>());
            }
            var tokens = (HashSet<String>) req.getSession().getAttribute("csrf-tokens");
            tokens.add(csrfToken);
            req.setAttribute("csrfToken", csrfToken);
        } else {
            String csrfToken = req.getParameter("csrf-token");
            var tokens = (HashSet<String>) req.getSession().getAttribute("csrf-tokens");
            if (!tokens.contains(csrfToken)) {
                throw new CsrfException("CSRF token not match");
            }
            tokens.remove(csrfToken);
        }
        chain.doFilter(req, res);
    }
}
