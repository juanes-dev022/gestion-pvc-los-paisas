package com.gestionpvc.applications.usecase.saleusecase;

import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IGetSaleUseCase;
import com.gestionpvc.applications.interfaces.irepositories.ISaleRepository;
import com.gestionpvc.applications.Exceptions.CustomNotFoundException;
import com.gestionpvc.applications.dtos.sale.SaleDto;
import com.gestionpvc.applications.dtos.sale.SaleDetailsDto;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Sale;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

public class GetSaleUseCase implements IGetSaleUseCase {
    private final ISaleRepository repository;

    public GetSaleUseCase(ISaleRepository repository) {
        this.repository = repository;
    }

    @Override
    public SaleDto execute(String id) {
        Optional<Sale> opt = repository.getById(id);
        if (opt.isEmpty()) {
            throw new CustomNotFoundException("Venta no encontrada");
        }
        return mapToDto(opt.get());
    }

    private SaleDto mapToDto(Sale sale) {
        List<SaleDetailsDto> itemsDto = sale.items().stream()
                .map(item -> new SaleDetailsDto(
                        item.productId(),
                        new ProductDto(
                                item.product().id(),
                                item.product().name(),
                                item.product().purchaseValue(),
                                item.product().stock(),
                                item.product().active()
                        ),
                        item.name(),
                        item.quantity(),
                        item.salePrice(),
                        item.profit()
                ))
                .collect(Collectors.toList());

        return new SaleDto(sale.id(), sale.createdAt(), itemsDto, sale.total(), null);
    }
}
