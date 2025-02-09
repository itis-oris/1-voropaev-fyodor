package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.merch.store.model.ProductForIndexPage;
import ru.itis.merch.store.service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductsServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init(ServletConfig config) {
        productService = (ProductService) config.getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page;
        int pageSize;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            pageSize = Integer.parseInt(req.getParameter("page-size"));
        } catch (NumberFormatException exception) {
            page = 1;
            pageSize = 20;
        }
        List<ProductForIndexPage> products = productService.findAllProducts(page, pageSize);
        req.setAttribute("products", products);
        req.getRequestDispatcher("/products.ftl").forward(req, resp);
    }
}
