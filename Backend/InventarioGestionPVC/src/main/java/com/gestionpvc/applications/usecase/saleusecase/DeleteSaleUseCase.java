package com.gestionpvc.applications.usecase.saleusecase;

import com.gestionpvc.applications.interfaces.irepositories.ISaleRepository;
import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IDeleteSaleUseCase;

public class DeleteSaleUseCase implements IDeleteSaleUseCase {
    private final ISaleRepository repository;

    public DeleteSaleUseCase(ISaleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean execute(String id) {
        if (repository.getById(id).isEmpty()) {
            return false;
        }
        repository.delete(id);
        System.out.println(id);
        return true;

    }
}
