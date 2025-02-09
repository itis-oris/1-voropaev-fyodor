package ru.itis.merch.store.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import ru.itis.merch.store.dao.RoleDAO;
import ru.itis.merch.store.dao.UserDAO;
import ru.itis.merch.store.entity.Role;
import ru.itis.merch.store.entity.User;
import ru.itis.merch.store.exception.IncorrectCredentialsException;
import ru.itis.merch.store.exception.UserWithSpecifiedEmailAlreadyExistException;
import ru.itis.merch.store.exception.UserWithSpecifiedEmailNotFoundException;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int HASH_POW = 10;
    private static final String DEFAULT_USER_STATUS_AFTER_CREATION = "Не подтверждён";
    private static final String DEFAULT_USER_ROLE_NAME = "CUSTOMER";

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void registerUser(String firstName, String lastName, String email, String password) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            userDAO.findByEmail(email)
                    .ifPresent((user) -> {
                        throw new UserWithSpecifiedEmailAlreadyExistException("User with email: " + email + " already exist");
                    });
            Role role = roleDAO
                    .findByName(DEFAULT_USER_ROLE_NAME)
                    .orElseThrow(() -> new IllegalStateException("Role named: " + DEFAULT_USER_ROLE_NAME + " was not found, although it should be"));
            userDAO.save(new User(null, firstName, lastName,
                    email, BCrypt.hashpw(password, BCrypt.gensalt(HASH_POW)),
                    DEFAULT_USER_STATUS_AFTER_CREATION, null, null, role.getId()));
            transactionManager.commit(transaction);
        } catch (Throwable throwable) {
            transactionManager.rollback(transaction);
            throw throwable;
        }
    }

    @Override
    public User loginUser(String email, String password, String sessionId) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            User user = userDAO
                    .findByEmail(email)
                    .orElseThrow(() -> new UserWithSpecifiedEmailNotFoundException("User with email: " + email + " not found"));
            boolean isAuthenticated = BCrypt.checkpw(password, user.getPassword());
            if (!isAuthenticated) {
                throw new IncorrectCredentialsException("Incorrect credentials");
            }
            if (sessionId != null) {
                user.setSessionId(sessionId);
                userDAO.updateById(user.getId(), user);
            }
            transactionManager.commit(transaction);
            return user;
        } catch (Throwable throwable) {
            transactionManager.rollback(transaction);
            throw throwable;
        }
    }

    @Override
    public void logoutUser(Long userId) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            userDAO.findById(userId).ifPresent((user) -> {
                user.setSessionId(null);
                userDAO.updateById(user.getId(), user);
            });
            transactionManager.commit(transaction);
        } catch (Throwable throwable) {
            transactionManager.rollback(transaction);
            throw throwable;
        }
    }
}
