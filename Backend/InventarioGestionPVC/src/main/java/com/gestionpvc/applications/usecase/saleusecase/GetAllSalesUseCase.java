package com.gestionpvc.applications.usecase.saleusecase;

import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IGetAllSalesUseCase;
import com.gestionpvc.applications.interfaces.irepositories.ISaleRepository;
import com.gestionpvc.applications.dtos.sale.SaleDto;
import com.gestionpvc.applications.dtos.sale.SaleDetailsDto;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Sale;
import java.util.stream.Collectors;
import java.util.List;

public class GetAllSalesUseCase implements IGetAllSalesUseCase {
    private final ISaleRepository repository;

    public GetAllSalesUseCase(ISaleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SaleDto> execute(Void ignored) {
        List<Sale> sales = repository.getAll();
        return sales.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
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
