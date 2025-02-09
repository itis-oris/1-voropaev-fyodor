package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.model.ProductForSpecificProductPage;
import ru.itis.merch.store.service.ProductService;

import java.io.IOException;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productService = (ProductService) config.getServletContext().getAttribute("productService");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.isBlank()) {
            resp.sendRedirect("/products");
            return;
        }
        path = path.substring(1);
        try {
            long productId = Long.parseLong(path);
            ProductForSpecificProductPage product = productService.findProductById(productId);
            req.setAttribute("product", product);
            req.getRequestDispatcher("/product.ftl").forward(req, resp);
        } catch (NumberFormatException exception) {
            req.getRequestDispatcher("/error404.ftl").forward(req, resp);
        }
    }
}
