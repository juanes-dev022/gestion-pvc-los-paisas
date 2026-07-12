package com.gestionpvc.applications.usecase.productusecase;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IGetAllProductUseCase;
import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Product;
import java.util.stream.Collectors;
import java.util.List;

public class GetAllProductUseCase implements IGetAllProductUseCase {
    private final IProductRepository repository;

    public GetAllProductUseCase(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductDto> execute(Void ignored) {
        List<Product> items = repository.getAll();
        return items.stream()
                .map(
                    product -> new ProductDto(product.id(),
                    product.name(),
                    product.purchaseValue(),
                    product.stock(),
                    product.active()))
                .collect(Collectors.toList());
    }
}
