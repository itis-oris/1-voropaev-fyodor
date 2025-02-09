package ru.itis.merch.store.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import ru.itis.merch.store.dao.*;
import ru.itis.merch.store.dao.impl.*;
import ru.itis.merch.store.entity.*;
import ru.itis.merch.store.service.*;
import ru.itis.merch.store.util.RoleToPathReader;
import ru.itis.merch.store.util.RoleToPathReaderImpl;

import java.io.IOException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties properties = configureProperties();
        
        HikariDataSource dataSource = configureHikariDataSource(properties);
        flywayMigrate(dataSource);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);


        // DAOs
        UserDAO userDAO = new UserDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(User.class));
        RoleDAO roleDAO = new RoleDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(Role.class));
        ProductDAO productDAO = new ProductDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(Product.class));
        ProductPhotoDAO productPhotoDAO = new ProductPhotoDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(ProductPhoto.class));
        ProductSpecificationDAO productSpecificationDAO = new ProductSpecificationDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(ProductSpecification.class));
        CartItemDAO cartItemDAO = new CartItemDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(CartItem.class));
        OrderItemDAO orderItemDAO = new OrderItemDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(OrderItem.class));
        OrderDAO orderDAO = new OrderDAOImpl(jdbcTemplate, new BeanPropertyRowMapper<>(Order.class));

        // Services
        ProductService productService = new ProductServiceImpl(productDAO, productPhotoDAO, productSpecificationDAO);
        AuthService authService = new AuthServiceImpl(userDAO, roleDAO, transactionManager);
        CartService cartService = new CartServiceImpl(productDAO, cartItemDAO, productSpecificationDAO, productPhotoDAO, new ObjectMapper(), transactionManager);
        OrderService orderService = new OrderServiceImpl(orderDAO, cartItemDAO, orderItemDAO, productDAO, productPhotoDAO, userDAO, productSpecificationDAO, transactionManager);
        UserService userService = new UserServiceImpl(userDAO, new ObjectMapper());

        // Insert default admin
        Role role = roleDAO.findByName("ADMIN").orElseThrow(() -> new IllegalStateException("ADMIN role not found"));
        if (userDAO.findByEmail(properties.getProperty("admin.email")).isEmpty()) {
            userDAO.save(new User(null, properties.getProperty("admin.first-name"), properties.getProperty("admin.last-name"), properties.getProperty("admin.email"), BCrypt.hashpw(properties.getProperty("admin.password"), BCrypt.gensalt(10)), "ADMIN", null, null, role.getId()));
        }

        sce.getServletContext().setAttribute("userDAO", userDAO);
        sce.getServletContext().setAttribute("roleDAO", roleDAO);
        sce.getServletContext().setAttribute("productDAO", productDAO);
        sce.getServletContext().setAttribute("productPhotoDAO", productPhotoDAO);
        sce.getServletContext().setAttribute("productSpecificationDAO", productSpecificationDAO);
        sce.getServletContext().setAttribute("cartItemDAO", cartItemDAO);
        sce.getServletContext().setAttribute("orderItemDAO", orderItemDAO);
        sce.getServletContext().setAttribute("orderDAO", orderDAO);

        sce.getServletContext().setAttribute("authService", authService);
        sce.getServletContext().setAttribute("productService", productService);
        sce.getServletContext().setAttribute("cartService", cartService);
        sce.getServletContext().setAttribute("orderService", orderService);
        sce.getServletContext().setAttribute("userService", userService);
        RoleToPathReader pathReader = new RoleToPathReaderImpl();
        sce.getServletContext().setAttribute("roleToPathReader", pathReader);
        sce.getServletContext().setAttribute("pathToFile", properties.getProperty("path-to-images"));


    }

    private void flywayMigrate(HikariDataSource dataSource) {
        try {
            Flyway flyway = Flyway.configure().dataSource(dataSource).load();
            flyway.migrate();
        } catch (FlywayException e) {
            throw new IllegalStateException("Exception while flyway migrate: " + e.getMessage(), e);
        }
    }

    private HikariDataSource configureHikariDataSource(Properties properties) {
        try {
            Class.forName(properties.getProperty("dataSource.driver-class"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Exception while load db driver", e);
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("dataSource.url"));
        hikariConfig.setUsername(properties.getProperty("dataSource.user"));
        hikariConfig.setPassword(properties.getProperty("dataSource.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("dataSource.maximumPoolSize")));
        return new HikariDataSource(hikariConfig);
    }

    private Properties configureProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Exception while parse properties file", e);
        }
        return properties;
    }
}
