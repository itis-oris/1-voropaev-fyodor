<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration</title>
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
        .registration-container {
            max-width: 450px; /* Slightly wider than login container */
            margin: 80px auto;
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
<div class="container registration-container">
    <h2 class="text-center mb-4">Регистрация</h2>
    <form action="/registration" method="post" id="registrationForm">
        <div class="mb-3">
            <label for="firstName" class="form-label">Имя</label>
            <input type="text" class="form-control" id="firstName" name="firstName" required>
        </div>
        <div class="mb-3">
            <label for="lastName" class="form-label">Фамилия</label>
            <input type="text" class="form-control" id="lastName" name="lastName" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Пароль</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div>
            <input type="hidden" id="csrf-token" name="csrf-token" value="${csrfToken}">
        </div>
        <div class="mb-3">
            <label for="confirmPassword" class="form-label">Подтверждение пароля</label>
            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
            <div id="passwordError" class="error-message" style="display: none;">Пароли не совпадают</div>
        </div>
        <div class="mb-3 error-message">
            <#if exception??>
                <span>${exception}</span>
            </#if>
        </div>
        <div class="d-grid gap-2">
            <button type="submit" class="btn btn-primary" id="submitButton">Зарегистрироваться</button>
        </div>
        <p class="mt-3 text-center">Уже есть аккаунт? <a href="/login">Войти</a></p>
    </form>
</div>

<script src="/resources/static/js/bootstrap.js"></script>

<script>
    document.getElementById('registrationForm').addEventListener('submit', function(event) {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const passwordErrorDiv = document.getElementById('passwordError');
        const submitButton = document.getElementById('submitButton');

        if (password !== confirmPassword) {
            event.preventDefault(); //Prevent form submission
            passwordErrorDiv.style.display = 'block';
            submitButton.disabled = false;
            submitButton.blur();
        } else {
            passwordErrorDiv.style.display = 'none';
        }
    });
</script>
</body>
</html>