<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Корзина</title>
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
        .cart-container {
            max-width: 1000px;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .cart-item {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            display: flex;
            align-items: center; /* Vertically align items */
        }

        .cart-item img {
            max-width: 120px;
            height: auto;
            margin-right: 15px;
        }
        .cart-item-info{
            flex: 1; /* Take up available space, pushing buttons right*/
        }

        .cart-item-actions {
            text-align: right;
            white-space: nowrap; /* Prevent buttons from wrapping */
        }
        .quantity-input {
            max-width: 70px;
            margin-left: 10px;
        }
        .cart-total {
            text-align: left;
            font-size: 1.2em;
            margin-top: 20px;
        }
        .link {
            text-decoration: none;
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
<div class="container cart-container">
    <h2 class="text-center mb-4">Ваша корзина</h2>
    <#if cartItems?has_content>
        <#list cartItems as item>
            <div class="cart-item" data-product-price="${item.productPrice()}" data-quantity="${item.quantity()}" data-id="${item.cartItemId()}">
                <img src="/resources/images/${item.mainPhotoUrl()}" alt="${item.productName()}" class="img-fluid">
                <div class="cart-item-info">
                    <p><strong><a class="link" href="/products/${item.productId()}">${item.productName()}</a></strong></p>
                    <p>Размер: ${item.specificationName()}</p>
                    <p>Цена: ${item.productPrice()}</p>
                    <p>Оставшееся кол-во на складе: ${item.quantityInStock()}</p>
                    <p>
                        Количество:
                        <input id="quantity-input-${item.cartItemId()}" type="number" class="form-control quantity-input" value="${item.quantity()}" min="1" pattern="[0-9]*">
                    </p>
                    <p>Общая цена: <span class="item-total"></span></p>
                </div>

                <div class="cart-item-actions">
                    <button id="delete-button-${item.cartItemId()}" class="btn btn-sm btn-danger">Удалить</button>
                </div>
            </div>
        </#list>
        <p class="cart-total"><strong>Общая сумма: <span id="cart-total"></span></strong></p>
        <div class="text-right mt-4">
            <button id="create-order-button" class="confirm-order btn btn-primary">Оформить заказ</button>
        </div>
        <div class="mt-2">
            <span id="error-text"></span>
        </div>
    <#else>
        <p class="text-center">Ваша корзина пуста.</p>
    </#if>
</div>



<script src="/resources/static/js/bootstrap.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const cartItems = document.querySelectorAll('.cart-item');
        function restrictToDigitsKeypress(inputElement) {
            inputElement.addEventListener('keypress', function(event) {
                const charCode = (event.which) ? event.which : event.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    event.preventDefault();
                }
            });
        }
        function confirmOrder(inputElement) {
            inputElement.addEventListener('click', function() {
                const url = "http://localhost:8080/create-order"
                fetch(url, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: "{}"
                }).then((response) => {
                    if (!response.ok) {
                        const elem = document.getElementById("error-text");
                        if (response.status === 400) {
                            elem.textContent = "Невозможно создать заказ, недостаточно товаров на складе, проверьте корзину."
                        } else if (response.status === 401) {
                            elem.textContent = "Невозможно создать заказ, недостаточно баллов активности."
                        } else {
                            elem.textContent = "Невозможно создать заказ, пользователь не подтверждён."
                        }
                    } else {
                        window.location.replace("http://localhost:8080/profiles/" + ${user.getId()});
                    }
                });
            })
        }
        const confirmButton = document.getElementById("create-order-button");
        confirmOrder(confirmButton);
        function deleteButtonUpdate(inputElement) {
            inputElement.addEventListener('click', function(event) {
                const id = event.target.id;
                const int_id = id.substring(id.lastIndexOf("-") + 1);
                const url = "http://localhost:8080/carts/change-cart-item"
                fetch(url, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: "{ \"id\" : " + int_id + ", \"quantity\" : " + 0 + " }"
                })
                inputElement.parentElement.parentElement.remove()
                updateCartSummary()
            });
        }
        function updateCartSummary() {
            const cartItems = document.querySelectorAll('.cart-item');
            let cartTotal = 0;
            cartItems.forEach(function(item) {
                const productPrice = parseInt(item.dataset.productPrice);
                const quantity = parseInt(item.dataset.quantity);
                const id = parseInt(item.dataset.id);
                const input_var = document.getElementById("quantity-input-" + id)
                const delete_button = document.getElementById("delete-button-" + id)
                deleteButtonUpdate(delete_button)
                restrictToDigitsKeypress(input_var);
                const itemTotal = productPrice * quantity;
                const itemTotalElement = item.querySelector('.item-total');
                itemTotalElement.textContent = itemTotal.toFixed(0);
                cartTotal += itemTotal;
            });

            document.getElementById('cart-total').textContent = cartTotal.toFixed(0);
        }

        function updateCartSummaryAfter() {
            const cartItems = document.querySelectorAll('.cart-item');
            let cartTotal = 0;
            cartItems.forEach(function(item) {
                const productPrice = parseInt(item.dataset.productPrice);
                const id = parseInt(item.dataset.id);
                const inputvar = document.getElementById("quantity-input-" + id)
                const quantity = parseInt(inputvar.value);
                const itemTotal = productPrice * quantity;
                const itemTotalElement = item.querySelector('.item-total');
                itemTotalElement.textContent = itemTotal.toFixed(0);
                cartTotal += itemTotal;
            });

            document.getElementById('cart-total').textContent = cartTotal.toFixed(0);
        }

        cartItems.forEach(function(item) {
            const quantityInput = item.querySelector('.quantity-input');
            quantityInput.addEventListener('change', function(event) {
                updateCartSummaryAfter();
                const id = event.target.id;
                const int_id = id.substring(id.lastIndexOf("-") + 1);
                const url = "http://localhost:8080/carts/change-cart-item"
                fetch(url, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: "{ \"id\" : " + int_id + ", \"quantity\" : " + event.target.value + " }"
                });
                console.log(event.target.value)
                if (event.target.value === '0') {
                    event.target.parentElement.parentElement.parentElement.remove()
                }
            });
        });
        updateCartSummary();
    });
</script>

</body>
</html>