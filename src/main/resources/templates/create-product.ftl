<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Product</title>
    <link href="/resources/static/css/bootstrap.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar {
            background-color: #343a40;
        }
        .navbar-brand, .navbar-nav .nav-link {
            color: white !important;
        }

        .create-product-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .specification-container {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 10px;
            margin-bottom: 10px;
        }
        .specification-input {
            margin-bottom: 10px;
        }
        .specification-container .form-control{
            margin-bottom: 5px;
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
<div class="container create-product-container">
    <h2 class="text-center mb-4">Создание товара</h2>
    <form id="createProductForm" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="productName" class="form-label">Название товара</label>
            <input type="text" class="form-control" id="productName" required>
        </div>
        <div class="mb-3">
            <label for="productPrice" class="form-label">Цена</label>
            <input type="number" class="form-control" id="productPrice" required min="1">
        </div>
        <div class="mb-3">
            <label for="productDescription" class="form-label">Описание</label>
            <textarea class="form-control" id="productDescription" rows="3" required></textarea>
        </div>
        <div class="mb-3">
            <label  class="form-label">Фотографии</label>
            <input type="file" id="photoInput" class="form-control" multiple accept="image/*">
        </div>
        <div class="mb-3">
            <label class="form-label">Спецификации</label>
            <div id="specificationContainers">
                <div class="specification-container">
                    <input type="text" class="form-control specification-name" placeholder="Название спецификации" required>
                    <input type="text" class="form-control specification-description" placeholder="Описание спецификации">
                    <input type="number" class="form-control specification-quantity" placeholder="Количество в наличии" required min="1">
                    <button type="button" class="btn btn-sm btn-danger remove-specification-btn">Удалить</button>
                </div>
            </div>
            <button type="button" id="addSpecificationBtn" class="btn btn-sm btn-secondary">Добавить спецификацию</button>
        </div>
        <div class="text-center">
            <button type="submit" class="btn btn-primary">Создать товар</button>
        </div>
    </form>
</div>
<script src="/resources/static/js/bootstrap.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const addSpecificationBtn = document.getElementById('addSpecificationBtn');
        const specificationContainers = document.getElementById('specificationContainers');
        const createProductForm = document.getElementById('createProductForm');
        const photoInput = document.getElementById('photoInput');

        addSpecificationBtn.addEventListener('click', function() {
            const newSpecificationContainer = document.createElement('div');
            newSpecificationContainer.classList.add('specification-container');
            newSpecificationContainer.innerHTML = `
          <input type="text" class="form-control specification-name" placeholder="Название спецификации" required>
         <input type="text" class="form-control specification-description" placeholder="Описание спецификации">
          <input type="number" class="form-control specification-quantity" placeholder="Количество в наличии" required min="1">
        <button type="button" class="btn btn-sm btn-danger remove-specification-btn">Удалить</button>
       `;
            specificationContainers.appendChild(newSpecificationContainer);
            newSpecificationContainer.querySelector('.remove-specification-btn').addEventListener('click', function() {
                newSpecificationContainer.remove();
            });
        });

        createProductForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const productName = document.getElementById('productName').value;
            const productPrice = document.getElementById('productPrice').value;
            const productDescription = document.getElementById('productDescription').value;
            const photos = photoInput.files;

            const specifications = Array.from(document.querySelectorAll('.specification-container')).map(container => {
                return {
                    name: container.querySelector('.specification-name').value,
                    description: container.querySelector('.specification-description').value,
                    quantity: container.querySelector('.specification-quantity').value,
                };
            });
            const formData = new FormData();
            formData.append('productName', productName);
            formData.append('productPrice', productPrice);
            formData.append('productDescription', productDescription);
            formData.append('csrf-token', '${csrfToken}')
            for (let i = 0; i < photos.length; i++) {
                formData.append('photos', photos[i]);
            }
            formData.append('specifications', JSON.stringify(specifications));

            fetch('/admin-panel/create-product', {
                method: 'POST',
                body: formData,
            })
                .then(response => {
                    if(!response.ok) {
                        console.log("Failed to create product")
                        return;
                    }
                    console.log("Product created successfully");
                    // Handle success (e.g., redirect, show a message)
                })
                .catch(error => {
                    console.error('Error:', error);
                    // Handle errors (e.g., show error message)
                });

        });
    });
</script>
</body>
</html>