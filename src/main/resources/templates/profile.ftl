<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
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
        .profile-container {
            max-width: 800px;
            margin: 80px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .profile-label {
            font-weight: bold;
            margin-bottom: 0.5rem;
        }
        .profile-value {
            margin-bottom: 1rem;
        }
        .profile-value strong {
            font-weight: 500;
        }
        .order-table th, .order-table td {
            vertical-align: middle;
        }
        .product-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
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

<div class="container profile-container">
    <h2 class="text-center mb-4">Профиль пользователя</h2>
    <div class="row">
        <div class="col-md-6">
            <p class="profile-label">Email:</p>
            <p class="profile-value"><strong>${user.email}</strong></p>
        </div>
        <div class="col-md-6">
            <p class="profile-label">Имя:</p>
            <p class="profile-value"><strong>${user.getFirstName()}</strong></p>
        </div>

        <div class="col-md-6">
            <p class="profile-label">Фамилия:</p>
            <p class="profile-value"><strong>${user.getLastName()}</strong></p>
        </div>

        <div class="col-md-6">
            <p class="profile-label">Статус:</p>
            <p class="profile-value"><strong>${user.getStatus()}</strong></p>
        </div>

        <#if user.getActivityPoints()??>
            <div class="col-md-6">
                <p class="profile-label">Активность:</p>
                <p class="profile-value"><strong>${user.getActivityPoints()}</strong></p>
            </div>
        </#if>

        <#if user.getStatus() == "Подтверждён">
            <div class="col-md-6">
                <p class="profile-label">Повторно отправить заявку на подтверждение:</p>
                <button id="change-user-status" type="submit" class="btn btn-primary">Отправить</button>
            </div>
        </#if>
    </div>

    <!-- Orders Section -->
    <h3 class="mt-5">Ваши Заказы</h3>
    <#if orders??>
        <#if orders?has_content>
        <table class="table table-bordered order-table mt-3">
            <thead>
            <tr>
                <th>#</th>
                <th>Статус</th>
                <th>Описание</th>
                <th>Дата создания</th>
                <th>Дата обновления</th>
                <th>Продукты</th>
            </tr>
            </thead>
            <tbody>
            <#list orders as order>
                <tr>
                    <td>${order.id()}</td>
                    <td>${order.status()}</td>
                    <td>${order.description()}</td>
                    <td>${order.creationTime()?string("dd/MM/yyyy HH:mm")}</td>
                    <td>${order.updateTime()?string("dd/MM/yyyy HH:mm")}</td>
                    <td>
                        <ul class="list-unstyled">
                            <#list order.products() as product>
                                <li>
                                    <div class="d-flex align-items-center">
                                        <img src="/resources/images/${product.mainPhotoUrl()}" alt="${product.productName()}" class="product-image me-2">
                                        <div>
                                            <strong>${product.productName()}</strong><br>
                                            <small>${product.productSpecificationName()}</small><br>
                                            <span>Количество: ${product.quantity()}</span>
                                        </div>
                                    </div>
                                </li>
                            </#list>
                        </ul>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
        <#else>
            <p>У вас нет заказов.</p>
        </#if>
    </#if>
</div>

<form action="/logout">
    <div class="text-center mt-4">
        <button type="submit" class="btn btn-primary">Выйти</button>
    </div>
</form>
<br>

<script src="/resources/static/js/bootstrap.js"></script>
</body>
<#if user.getActivityPoints()??>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const button = document.getElementById('change-user-status');
            button.addEventListener('click', function () {
                const activityPoints = ${user.getActivityPoints()};
                const userId = ${user.getId()};
                const status = "Не подтверждён";
                const url = "http://localhost:8080/admin-panel/change-user-status"
                fetch(url, {
                    method: "PUT",
                    headers: {"Content-Type": "application/json"},
                    body: "{\"userId\" : " + userId + ", \"status\" : \"" + status + "\", \"points\" : " + activityPoints + "}"
                })
                window.location.replace("http://localhost:8080/");
            });
        })
    </script>
</#if>
</html>
