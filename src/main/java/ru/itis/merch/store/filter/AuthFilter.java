package ru.itis.merch.store.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.dao.RoleDAO;
import ru.itis.merch.store.dao.UserDAO;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.util.RoleToPathReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebFilter("*")
public class AuthFilter extends HttpFilter {

    private static final String ROLE_TO_PATHS_FILENAME = "roles-to-paths.txt";
    private static final String NOT_AUTHENTICATED_ROLE_NAME = "NOTAUTHENTICATED";

    private Map<String, List<String>> paths;
    private UserDAO userDAO;
    private RoleDAO roleDAO;

    @Override
    public void init(FilterConfig config) {
        RoleToPathReader pathsReader = (RoleToPathReader) config.getServletContext().getAttribute("roleToPathReader");
        paths = pathsReader.readToMap(ROLE_TO_PATHS_FILENAME);
        userDAO = (UserDAO) config.getServletContext().getAttribute("userDAO");
        roleDAO = (RoleDAO) config.getServletContext().getAttribute("roleDAO");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getSession().getAttribute("role") == null && req.getCookies() != null) {
            Cookie[] cookies = req.getCookies();
            Cookie cookieSessionId = Arrays.stream(cookies).filter(cookie -> "session-id".equals(cookie.getName())).findFirst().orElse(null);
            if (cookieSessionId != null) {
                String sessionId = cookieSessionId.getValue();
                userDAO.findBySessionId(sessionId).ifPresent((user) -> {
                    roleDAO.findById(user.getRoleId()).ifPresent((role) -> req.getSession().setAttribute("role", role.getName()));
                    req.getSession().setAttribute("user", user);
                });
            }
        }
        if (req.getSession().getAttribute("user") != null && req.getSession().getAttribute("role") == null) {
            User user = (User) req.getSession().getAttribute("user");
            roleDAO.findById(user.getRoleId()).ifPresent((role) -> req.getSession().setAttribute("role", role.getName()));
        }
        if (req.getSession().getAttribute("role") == null) {
            boolean isAuthorized = isAuthorized(NOT_AUTHENTICATED_ROLE_NAME, req.getRequestURI());
            if (!isAuthorized) {
                res.sendRedirect("/login");
                return;
            }
            chain.doFilter(req, res);
            return;
        }
        String role = (String) req.getSession().getAttribute("role");
        boolean isAuthorized = isAuthorized(role, req.getRequestURI());
        if (!isAuthorized) {
            req.getRequestDispatcher("/error403.ftl").forward(req, res);
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean isAuthorized(String roleName, String requestUri) {
        if (!paths.containsKey(roleName)) {
            throw new IllegalStateException("Not Supported Role: " + roleName);
        }
        List<String> urlPatterns = paths.get(roleName);
        return urlPatterns.stream().anyMatch(requestUri::matches);
    }
}
