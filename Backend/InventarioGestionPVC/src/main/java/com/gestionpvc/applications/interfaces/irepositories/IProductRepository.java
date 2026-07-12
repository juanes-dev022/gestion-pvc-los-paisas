package com.gestionpvc.applications.interfaces.irepositories;

import com.gestionpvc.domains.Product;
import java.util.Optional;
import java.util.List;

public interface IProductRepository {
    Optional<Product> getById(String id);
    List<Product> getAll();
    Product add(Product product);
    Product update(Product product);
    void delete(String id);
}
