package ru.itis.merch.store.dao;

import ru.itis.merch.store.entity.ProductPhoto;

import java.util.List;

public interface ProductPhotoDAO extends CrudDAO <ProductPhoto, Long> {
    List<ProductPhoto> findByProductId(Long id);
}
