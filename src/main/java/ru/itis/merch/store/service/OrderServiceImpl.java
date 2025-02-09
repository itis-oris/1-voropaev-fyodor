package ru.itis.merch.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import ru.itis.merch.store.dao.*;
import ru.itis.merch.store.entity.*;
import ru.itis.merch.store.exception.EntityNotFoundException;
import ru.itis.merch.store.exception.NotEnoughActivityPoints;
import ru.itis.merch.store.exception.NotEnoughProductsInStock;
import ru.itis.merch.store.model.OrderForProfilePage;
import ru.itis.merch.store.model.ProductForProfilePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String DEFAULT_STATUS = "Создан";

    private final OrderDAO orderDAO;
    private final CartItemDAO cartItemDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;
    private final ProductPhotoDAO productPhotoDAO;
    private final UserDAO userDAO;
    private final ProductSpecificationDAO productSpecificationDAO;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void createOrder(Long userId) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            List<CartItem> cartItems = cartItemDAO.findByUserId(userId);
            User user = userDAO.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (cartItems.isEmpty()) return;
            List<ProductSpecification> productSpecifications = productSpecificationDAO.findAllByIds(cartItems.stream().map(CartItem::getProductSpecificationId).toList());
            List<Product> products = productDAO.findAllByIds(productSpecifications.stream().map(ProductSpecification::getProductId).toList());
            Long sum = cartItems.stream().mapToLong((cartItem) -> {
                ProductSpecification productSpecification = productSpecifications.stream().filter((spec) -> cartItem.getProductSpecificationId().equals(spec.getId())).findFirst().orElseThrow(() -> new EntityNotFoundException("Product specification not found"));
                Product product = products.stream().filter((prod) -> productSpecification.getProductId().equals(prod.getId())).findFirst().orElseThrow(() -> new EntityNotFoundException("Product not found"));
                return cartItem.getQuantity() * product.getPrice();
            }).sum();
            if (sum > user.getActivityPoints()) {
                throw new NotEnoughActivityPoints("Недостаточно баллов активности, для оплаты заказа");
            }
            user.setActivityPoints(user.getActivityPoints() - sum);
            userDAO.updateById(userId, user);
            Long orderId = orderDAO.saveAndReturnId(new Order(null, userId, DEFAULT_STATUS, "", null, null));
            for (var cartItem : cartItems) {
                OrderItem orderItem = new OrderItem(null, orderId, cartItem.getProductSpecificationId(), cartItem.getQuantity());
                ProductSpecification specif = productSpecifications.stream().filter(spec -> spec.getId().equals(cartItem.getProductSpecificationId())).findFirst().orElseThrow(() -> new EntityNotFoundException("Spec not found"));
                if (specif.getQuantityInStock() < cartItem.getQuantity()) {
                    throw new NotEnoughProductsInStock("Недостаточно товаров на складе");
                }
                orderItemDAO.save(orderItem);
                cartItemDAO.deleteById(cartItem.getId());
                specif.setQuantityInStock(specif.getQuantityInStock() - cartItem.getQuantity());
                productSpecificationDAO.updateById(specif.getId(), specif);
            }
            transactionManager.commit(transaction);
        } catch (Throwable throwable) {
            transactionManager.rollback(transaction);
            throw throwable;
        }
    }

    @Override
    public List<OrderForProfilePage> findOrdersByUserId(Long userId) {
        List<Order> orders = orderDAO.findByUserId(userId);
        List<OrderForProfilePage> models = new ArrayList<>();
        for (var order : orders) {
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(order.getId());
            List<Long> productSpecificationIds = orderItems.stream().map(OrderItem::getProductSpecificationId).toList();
            List<ProductSpecification> productSpecifications = productSpecificationDAO.findAllByIds(productSpecificationIds);
            List<Product> products = productDAO.findAllByIds(productSpecifications.stream().map(ProductSpecification::getProductId).toList());
            HashMap<Long, String> photoMap = new HashMap<>();
            products.forEach(product -> {
                List<ProductPhoto> photos = productPhotoDAO.findByProductId(product.getId());
                if (photos.isEmpty()) {
                    photoMap.put(product.getId(), "no-photo.jpg");
                } else {
                    photoMap.put(product.getId(), photos.get(0).getUrl());
                }
            });
            List<ProductForProfilePage> productsForPage = orderItems.stream().map((item) -> {
                ProductSpecification productSpecification = productSpecifications
                        .stream()
                        .filter((spec) -> item.getProductSpecificationId().equals(spec.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Product specification not found"));
                Product product = products
                        .stream().filter(product1 -> productSpecification.getProductId().equals(product1.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                String photoUrl = photoMap.get(product.getId());
                return new ProductForProfilePage(product.getId(), product.getName(), photoUrl, productSpecification.getName(), item.getQuantity());
            }).toList();
            models.add(new OrderForProfilePage(order.getId(), order.getStatus(), order.getDescription(), order.getCreationTime(), order.getUpdateTime(), productsForPage));
        }
        return models;
    }

}
