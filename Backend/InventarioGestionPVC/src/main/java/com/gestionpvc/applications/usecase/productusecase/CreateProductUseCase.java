package com.gestionpvc.applications.usecase.productusecase;

import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.ICreateProductUseCase;
import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;
import com.gestionpvc.applications.dtos.product.CreateProductDto;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Product;

public class CreateProductUseCase implements ICreateProductUseCase {

    private final IProductRepository repository;

    public CreateProductUseCase(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDto execute(CreateProductDto data) {
    
    boolean isActive = data.active;
    Product toSave = new Product(
        null,
        data.name,
        data.purchaseValue,
        data.stock,
        isActive
    );

    Product saved = repository.add(toSave);

    return new ProductDto(
        saved.id(),
        saved.name(),
        saved.purchaseValue(),
        saved.stock(),
        saved.active()
    );
    }
}