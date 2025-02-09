<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Verification</title>
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
        .verification-container {
            max-width: 1000px;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .user-item {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .user-info {
            flex: 1;
        }
        .user-actions {
            white-space: nowrap;
        }
        .user-actions input{
            max-width: 60px;
            margin-right: 10px;
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
<div class="container verification-container">
    <h2 class="text-center mb-4">Подтверждение пользователей</h2>
    <#if users?has_content>
        <#list users as user>
            <div class="user-item" data-user-id="${user.getId()}">
                <div class="user-info">
                    <p><strong>Имя:</strong> ${user.getFirstName()}</p>
                    <p><strong>Фамилия:</strong> ${user.getLastName()}</p>
                    <p><strong>Email:</strong> ${user.getEmail()}</p>
                    <p><strong>Статус:</strong> ${user.getStatus()}</p>
                    <#if user.getActivityPoints()??>
                        <p><strong>Текущее кол-во баллов активности:</strong> ${user.getActivityPoints()}</p>
                    <#else>
                        <p><strong>Текущее кол-во баллов активности:</strong> 0}</p>
                    </#if>
                </div>
                <div class="user-actions">
                    <p><strong>Количество баллов активности:</strong></p>
                    <input type="number" class="form-control activity-points-input" value="0" min="0">
                    <button class="btn btn-sm btn-success approve-user-btn">Подтвердить</button>
                    <button class="btn btn-sm btn-danger reject-user-btn">Отклонить</button>
                </div>
            </div>
        </#list>
    <#else>
        <p class="text-center">Нет пользователей для подтверждения.</p>
    </#if>
</div>
<script src="/resources/static/js/bootstrap.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const userItems = document.querySelectorAll('.user-item');
        userItems.forEach(item => {
            const approveBtn = item.querySelector('.approve-user-btn');
            const rejectBtn = item.querySelector('.reject-user-btn');
            const activityInput = item.querySelector('.activity-points-input');
            const userId = item.dataset.userId;
            activityInput.addEventListener('keypress', function(event) {
                const charCode = (event.which) ? event.which : event.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    event.preventDefault();
                }
            });

            approveBtn.addEventListener('click', function() {
                const activityPoints = parseInt(activityInput.value);
                const status = "Подтверждён";
                const url = "http://localhost:8080/admin-panel/change-user-status"
                fetch(url, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: "{\"userId\" : " + userId + ", \"status\" : \"" + status + "\", \"points\" : " + activityPoints + "}"
                })
                item.remove()
            });
            rejectBtn.addEventListener('click', function() {
                const status = "Отклонён";
                const url = "http://localhost:8080/admin-panel/change-user-status"
                fetch(url, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: "{\"userId\" : " + userId + ", \"status\" : \"" + status + "\"}"
                })
                item.remove()
            });
        });
    });
</script>
</body>
</html>