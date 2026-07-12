package com.gestionpvc.applications.usecase.productusecase;

import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IDeleteProductUseCase;
import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;

public class DeleteProductUseCase implements IDeleteProductUseCase {
    private final IProductRepository repository;

    public DeleteProductUseCase(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean execute(String id) {

        if (repository.getById(id).isEmpty()) {
            return false;
        }
        repository.delete(id);
        return true;
    }
}
