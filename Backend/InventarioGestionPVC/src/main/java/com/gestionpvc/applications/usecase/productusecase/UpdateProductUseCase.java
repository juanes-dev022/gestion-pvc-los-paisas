package com.gestionpvc.applications.usecase.productusecase;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IUpdateProductUseCase;
import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;
import com.gestionpvc.applications.Exceptions.CustomNotFoundException;
import com.gestionpvc.applications.dtos.product.UpdateProductDto;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Product;
import java.util.Optional;

public class UpdateProductUseCase implements IUpdateProductUseCase {
    private final IProductRepository repository;

    public UpdateProductUseCase(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDto execute(UpdateProductDto data) {

        Optional<Product> existingOpt = repository.getById(data.id);
        if (existingOpt.isEmpty()) {
            throw new CustomNotFoundException("Producto no encontrado");
        }

        Product current = existingOpt.get();

        String name = data.name != null ? data.name : current.name();
        double purchaseValue = data.purchaseValue != 0 ? data.purchaseValue : current.purchaseValue();
        int stock = data.stock != 0 ? data.stock : current.stock();
        boolean active = current.active();

        Product toUpdate = new Product(
            current.id(),
            name,
            purchaseValue,
            stock,
            active
        );

        Product saved = repository.update(toUpdate);

        return new ProductDto(saved.id(), saved.name(), saved.purchaseValue(), saved.stock(), saved.active());
    }
}
