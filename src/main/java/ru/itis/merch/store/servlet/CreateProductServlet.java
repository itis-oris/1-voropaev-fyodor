package ru.itis.merch.store.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ru.itis.merch.store.model.ProductSpecificationDTO;
import ru.itis.merch.store.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin-panel/create-product")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class CreateProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productService = (ProductService) config.getServletContext().getAttribute("productService");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/create-product.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String productName = req.getParameter("productName");
        Long productPrice = Long.parseLong(req.getParameter("productPrice"));
        String productDescription = req.getParameter("productDescription");

        productService.createProduct(productName, productPrice, productDescription);
        List<String> productPhotosUrls = new ArrayList<>();
        for (Part part : req.getParts()) {
            if (part.getName().equals("photos")) {
                
                String filename = part.getSubmittedFileName();
                productPhotosUrls.add(filename);
            }
        }

        String specificationsJson = req.getParameter("specifications");
        List<ProductSpecificationDTO> specifications =  mapper.readValue(specificationsJson, mapper.getTypeFactory().constructCollectionType(List.class, ProductSpecificationDTO.class));

        System.out.println("Received product:");
        System.out.println("Product Name: " + productName);
        System.out.println("Product Price: " + productPrice);
        System.out.println("Product Description: " + productDescription);
        System.out.println("Photos: " + productPhotosUrls);
        System.out.println("Specifications: " + specifications);
        resp.setStatus(HttpServletResponse.SC_OK);

    }
}
