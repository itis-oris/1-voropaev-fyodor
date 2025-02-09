<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel</title>
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
        .admin-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .admin-link {
            display: block;
            margin-bottom: 15px;
            padding: 10px;
            background-color: #e9ecef;
            border-radius: 5px;
            text-align: center;
            text-decoration: none;
            color: #212529; /* Dark text color for the links */
            transition: background-color 0.3s ease; /* Smooth transition on hover */
        }
        .admin-link:hover {
            background-color: #dee2e6;
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

<div class="container admin-container">
    <h2 class="text-center mb-4">Админ Панель</h2>
    <a href="/admin-panel/verify-users" class="admin-link">Подтверждение пользователей</a>
    <a href="/admin-panel/create-product" class="admin-link">Создание товара</a>
    <a href="/admin-panel/edit-products" class="admin-link">Редактирование товара</a>
</div>
<form action="/logout">
    <div class="text-center mt-4">
        <button type="submit" class="btn btn-primary">Выйти</button>
    </div>
</form>
<script src="/resources/static/js/bootstrap.js"></script>
</body>
</html>