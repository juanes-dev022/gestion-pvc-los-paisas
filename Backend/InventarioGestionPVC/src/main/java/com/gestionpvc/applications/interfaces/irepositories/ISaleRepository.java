package com.gestionpvc.applications.interfaces.irepositories;

import com.gestionpvc.domains.Sale;
import java.util.Optional;
import java.util.List;

public interface ISaleRepository {
    Optional<Sale> getById(String id);
    List<Sale> getAll();
    Sale add(Sale sale);
    Sale update(Sale sale);
    void delete(String id);
}
