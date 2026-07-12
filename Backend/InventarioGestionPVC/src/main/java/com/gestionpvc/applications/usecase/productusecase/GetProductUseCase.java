package com.gestionpvc.applications.usecase.productusecase;


import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IGetProductUseCase;
import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;
import com.gestionpvc.applications.Exceptions.CustomNotFoundException;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Product;
import java.util.Optional;

public class GetProductUseCase implements IGetProductUseCase {
    private final IProductRepository repository;

    public GetProductUseCase(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDto execute(String id) {

        Optional<Product> opt = repository.getById(id);
        if (opt.isEmpty()) {
            throw new CustomNotFoundException("Producto no encontrado");
        }
        Product product = opt.get();
        return new ProductDto(product.id(), product.name(), product.purchaseValue(), product.stock(), product.active());
    }
}
