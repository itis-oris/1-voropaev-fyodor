package ru.itis.merch.store.service;

import lombok.RequiredArgsConstructor;
import ru.itis.merch.store.dao.ProductDAO;
import ru.itis.merch.store.dao.ProductPhotoDAO;
import ru.itis.merch.store.dao.ProductSpecificationDAO;
import ru.itis.merch.store.entity.Product;
import ru.itis.merch.store.entity.ProductPhoto;
import ru.itis.merch.store.entity.ProductSpecification;
import ru.itis.merch.store.exception.EntityNotFoundException;
import ru.itis.merch.store.model.ProductForIndexPage;
import ru.itis.merch.store.model.ProductForSpecificProductPage;

import java.util.List;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final ProductPhotoDAO productPhotoDAO;
    private final ProductSpecificationDAO productSpecificationDAO;

    @Override
    public List<ProductForIndexPage> findAllProducts(int page, int pageSize) {
        List<Product> products = productDAO.findAllWithPagination(page, pageSize);
        return products.stream().map((product) -> {
            List<ProductPhoto> productPhotos = productPhotoDAO.findByProductId(product.getId());
            String mainPhotoUrl = !productPhotos.isEmpty() ? productPhotos.get(0).getUrl() : "no-photo.jpg";
            return new ProductForIndexPage(product.getId(), product.getName(), product.getPrice(), product.getDescription(), mainPhotoUrl);
        }).toList();
    }

    @Override
    public ProductForSpecificProductPage findProductById(Long productId) {
        Product product = productDAO
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID: " + productId + " not found"));
        List<String> photoUrls = productPhotoDAO
                .findByProductId(productId)
                .stream()
                .map(ProductPhoto::getUrl)
                .toList();
        List<ProductSpecification> productSpecifications = productSpecificationDAO.findByProductId(productId);
        return new ProductForSpecificProductPage(productId, product.getName(), product.getPrice(), product.getDescription(), photoUrls, productSpecifications);
    }

    @Override
    public Product createProduct(String name, Long price, String description) {
        return new Product(productDAO.saveAndReturnId(new Product(null, name, price, description)), name, price, description);
    }
}
