package ru.itis.merch.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import ru.itis.merch.store.dao.CartItemDAO;
import ru.itis.merch.store.dao.ProductDAO;
import ru.itis.merch.store.dao.ProductPhotoDAO;
import ru.itis.merch.store.dao.ProductSpecificationDAO;
import ru.itis.merch.store.entity.*;
import ru.itis.merch.store.model.ProductForCartPage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductDAO productDAO;
    private final CartItemDAO cartItemDAO;
    private final ProductSpecificationDAO productSpecificationDAO;
    private final ProductPhotoDAO productPhotoDAO;
    private final ObjectMapper objectMapper;
    private final PlatformTransactionManager transactionManager;

    @Override
    public List<ProductForCartPage> findCartItemsByUserId(Long userId) {
        List<CartItem> cartItems = cartItemDAO.findByUserId(userId);
        List<Long> productSpecificationsIds = cartItems
                .stream()
                .map(CartItem::getProductSpecificationId)
                .toList();
        List<ProductSpecification> specifications = productSpecificationDAO.findAllByIds(productSpecificationsIds);
        if (cartItems.size() != specifications.size()) throw new IllegalStateException("Some specification not found");
        List<Long> productIds = specifications
                .stream()
                .map(ProductSpecification::getProductId)
                .toList();
        List<Product> products = productDAO.findAllByIds(productIds);
        HashMap<Long, String> photoMap = new HashMap<>();
        products.forEach(product -> {
            List<ProductPhoto> photos = productPhotoDAO.findByProductId(product.getId());
            if (photos.isEmpty()) {
                photoMap.put(product.getId(), "no-photo.jpg");
            } else {
                photoMap.put(product.getId(), photos.get(0).getUrl());
            }
        });
        return cartItems
                .stream()
                .map(cartItem -> {
            ProductSpecification productSpecification = specifications
                    .stream()
                    .filter((spec) -> cartItem
                            .getProductSpecificationId()
                            .equals(spec.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Product specification not found"));
            Product product = products
                    .stream()
                    .filter((pr) -> pr.getId().equals(productSpecification.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Product not found"));
            String photoUrl = photoMap.get(product.getId());
            return new ProductForCartPage(cartItem.getId(), product.getId(), product.getName(), product.getPrice(), photoUrl, productSpecification.getId(), productSpecification.getName(), productSpecification.getQuantityInStock(), cartItem.getQuantity());
        }).sorted(Comparator.comparingLong(ProductForCartPage::cartItemId)).toList();
    }

    @Override
    public String addProductSpecificationToUserCart(String data, Long userId) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            Long productSpecificationId = objectMapper.readTree(data).findValue("specificationId").asLong();
            Long quantity = objectMapper.readTree(data).findValue("quantity").asLong();
            if (quantity <= 0) {
                transactionManager.commit(transaction);
                return "Количество товара не может быть отрицательным или равным нулю.";
            }
            ProductSpecification productSpecification = productSpecificationDAO
                    .findById(productSpecificationId)
                    .orElseThrow(() -> new IllegalStateException("Specification not found"));
            Optional<CartItem> optionalCartItem = cartItemDAO.findByUserIdAndProductSpecificationId(userId, productSpecification.getId());
            if (optionalCartItem.isPresent()) {
                CartItem cartItem = optionalCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemDAO.updateById(cartItem.getId(), cartItem);
                transactionManager.commit(transaction);
                return "Количество товара в корзине успешно обновлено и теперь равно: " + cartItem.getQuantity() + " шт.";
            } else {
                cartItemDAO.save(new CartItem(null, userId, productSpecification.getId(), quantity));
                transactionManager.commit(transaction);
                return "Товар успешно добавлен в корзину в количестве: " + quantity + " шт.";
            }
        } catch (Throwable e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeQuantityOfCartItem(String data, User user) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            long cartItemId = objectMapper.readTree(data).findValue("id").asLong();
            long quantity = objectMapper.readTree(data).findValue("quantity").asLong();
            CartItem cartItem = cartItemDAO
                    .findById(cartItemId)
                    .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
            if (quantity <= 0) {
                cartItemDAO.deleteById(cartItemId);
            }
            cartItem.setQuantity(quantity);
            cartItemDAO.updateById(cartItemId, cartItem);
            transactionManager.commit(transaction);
        } catch (Throwable e) {
            transactionManager.rollback(transaction);
            throw new RuntimeException(e);
        }
    }
}
