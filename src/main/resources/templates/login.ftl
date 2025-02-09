<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="/resources/static/css/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa; /* Light gray background */
        }
        .navbar {
            background-color: #343a40; /* Dark background color for navbar */
        }
        .navbar-brand, .navbar-nav .nav-link {
            color: white !important;
        }
        .login-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .form-label {
            font-weight: bold;
        }
        .btn-primary {
            background-color: #007bff; /* Standard Bootstrap blue */
        }
        .error-message {
            color: red;
            font-size: 0.9em;
            margin-top: 0.25rem;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg">
    <div class="container">
        <a class="navbar-brand" href="/">ITIS Merch</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <#if user??>
                    <#if role == "ADMIN">
                        <li class="nav-item">
                            <a class="nav-link" href="/admin-panel">Админ Панель</a>
                        </li>
                    <#else>
                        <li class="nav-item">
                            <a class="nav-link" href="/carts/${user.getId()}">Корзина</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/profiles/${user.getId()}">Профиль</a>
                        </li>
                    </#if>
                <#else>
                    <li class="nav-item">
                        <a class="nav-link" href="/login">Войти</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/registration">Регистрация</a>
                    </li>
                </#if>
            </ul>
        </div>
    </div>
</nav>
<div class="container login-container">
    <h2 class="text-center mb-4">Вход</h2>
    <form action="/login" method="post">
        <div class="mb-3">
            <label for="email" class="form-label">Почта</label>
            <input type="text" class="form-control" id="email" name="email" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Пароль</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <#if exception??>
            <div class="mb-3 e error-message">
                <span>${exception}</span>
            </div>
        </#if>
        <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="rememberMe"  name="rememberMe">
            <label class="form-check-label" for="rememberMe">Запомнить меня</label>
        </div>
        <div class="mb-3 form-check">
            <span>Нет аккаунта? <a href="/registration">Зарегистрироваться</a> </span>
        </div>
        <div>
            <input type="hidden" id="csrf-token" name="csrf-token" value="${csrfToken}">
        </div>
        <div class="d-grid gap-2">
            <button type="submit" class="btn btn-primary">Войти</button>
        </div>
    </form>
</div>

<script src="/resources/static/js/bootstrap.js"></script>
</body>
</html>