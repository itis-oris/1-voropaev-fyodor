<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ITIS Merch Store</title>
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
        .product-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
        }
        .product-card h4,
        .product-card p {
            text-align: center;
            margin-bottom: 0.5rem;
        }
        .product-card img {
            max-width: 100%;
            height: auto; /* Maintain aspect ratio */
            margin-bottom: 10px;
            display: block; /* Makes it a block-level element */
            margin-left: auto; /* Centers the image horizontally */
            margin-right: auto; /* Centers the image horizontally */

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

<div class="container mt-4">
    <h1 class="text-center mb-4">Welcome to ITIS Merch Store!</h1>
    <div class="row">
        <#list products as product>
            <div class="col-md-4 text-center">
                <div class="product-card">
                    <h4>${product.name()}</h4>
                    <img src="/resources/images/${product.mainPhotoUrl()}" alt="${product.name()}" class="img-fluid">
                    <p>Цена: ${product.price()}</p>
                    <p>Описание: ${product.description()}</p>
                    <a href="/products/${product.id()}" class="btn btn-primary">Подробнее</a>
                </div>
            </div>
        </#list>
    </div>
</div>

<script src="/resources/static/js/bootstrap.js"></script>
</body>
</html>