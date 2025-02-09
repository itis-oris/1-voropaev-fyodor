<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name()}</title>
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

        .product-container {
            max-width: 900px;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .product-image-container {
            display: flex;
            overflow-x: auto; /* Horizontal scroll for images */
            margin-bottom: 15px;
        }

        .product-image {
            flex: 0 0 auto; /* Don't allow to shrink */
            width: 200px; /* Set image size */
            height: auto; /* Maintain image aspect ratio */
            margin-right: 10px;
            border-radius: 8px; /* Smooth rounded images */
        }


        .product-spec-container {
            padding: 10px; /* Add some padding */
            margin-bottom: 10px; /* Add some space between containers */
            border: 1px solid #dee2e6;  /* Add a subtle border */
            border-radius: 8px;
        }
        .product-spec-container strong {
            font-weight: 500;
        }
        .add-to-cart-container {
            display: none;
            margin-top: 10px;

        }
        .add-to-cart-container input {
            max-width: 60px;
            margin-right: 10px;
        }
        .return-text {
            display: none;
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
<div class="container product-container">
    <h2 class="text-center mb-4">${product.name()}</h2>
    <div class="mb-3">
        <h4 class="text-center">Фотографии</h4>
        <div class="product-image-container">
            <#list product.photoUrls() as photoUrl>
                <img src="/resources/images/${photoUrl}" alt="Product Photo" class="product-image img-fluid">
            </#list>
        </div>
    </div>
    <div class="mb-3">
        <h4 class="text-center">Описание</h4>
        <p class="text-center">${product.description()}</p>
    </div>
    <div class="mb-3">
        <h4 class="text-center">Цена</h4>
        <p class="text-center"><strong>${product.price()}</strong></p>
    </div>

    <div class="mb-3">
        <h4 class="text-center">Размеры</h4>
        <#list product.productSpecifications() as spec>
            <div class="product-spec-container">
                <div>
                    <p><strong>Название:</strong> ${spec.getName()}</p>
                    <p><strong>Описание:</strong> ${spec.getDescription()}</p>
                    <p><strong>В наличии:</strong> ${spec.getQuantityInStock()}</p>
                </div>
                <button class="btn btn-sm btn-primary add-to-cart-btn"
                        data-spec-id="${spec.getId()}">Добавить в корзину</button>
                <div class="add-to-cart-container" id="add-to-cart-${spec.getId()}">
                    <label >
                        <span>Сколько добавить товаров в корзину?</span>
                        <input type="number" class="form-control quantity-input"  min="1" value="1">
                        <button class="btn btn-sm btn-success confirm-add-to-cart"
                                data-spec-id="${spec.getId()}">Подтвердить</button>
                        <span class="return-text" id="return-text-${spec.getId()}"><br></span>
                    </label>
                </div>
            </div>
        </#list>
    </div>

</div>
<script src="/resources/static/js/bootstrap.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const addToCartButtons = document.querySelectorAll('.add-to-cart-btn');

        addToCartButtons.forEach(button => {
            button.addEventListener('click', function() {
                const specId = this.dataset.specId;
                const container = document.getElementById('add-to-cart-' + specId);
                if (container.style.display === 'none' || container.style.display === '')
                    container.style.display = 'flex';
                else
                    container.style.display = 'none';

            });
        });

        const inputs = document.querySelectorAll(".quantity-input");
        inputs.forEach(input => {
            input.addEventListener('keypress', function(event) {
                const charCode = (event.which) ? event.which : event.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    event.preventDefault();
                }
            });
        })

        const confirmButtons = document.querySelectorAll('.confirm-add-to-cart');
        confirmButtons.forEach(button => {
            button.addEventListener('click', function() {
                const url = 'http://localhost:8080/carts/add-cart-item'
                const specId = this.dataset.specId;
                const container = document.getElementById('add-to-cart-' + specId);
                const inputElement = container.querySelector('.quantity-input');
                const quantity = inputElement.value;
                fetch(url, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: "{ \"specificationId\" : " + specId + ", \"quantity\" : " + quantity + " }"
                }).then((response) => {
                    // проверяем, что ответ ok, иначе выбрасываем ошибку
                    if (!response.ok) {
                        throw new Error("Сетевой ответ не был ok");
                    }
                    return response.text();  // Преобразование ответа в JSON
                })
                    .then((data) => {
                        const returnText = document.getElementById('return-text-' + specId)
                        returnText.textContent = data;
                        if (returnText.style.display === 'none' || returnText.style.display === '')
                            returnText.style.display = 'flex';
                    })
                    .catch((error) => {
                        console.error("Ошибка:", error); // Обработка любых ошибок
                    });



            });
        });
    });
</script>

</body>
</html>