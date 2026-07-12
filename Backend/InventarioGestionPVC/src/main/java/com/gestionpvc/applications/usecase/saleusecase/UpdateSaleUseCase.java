package com.gestionpvc.applications.usecase.saleusecase;

import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IUpdateSaleUseCase;
import com.gestionpvc.applications.interfaces.irepositories.ISaleRepository;
import com.gestionpvc.applications.interfaces.irepositories.IProductRepository;
import com.gestionpvc.applications.Exceptions.CustomNotFoundException;
import com.gestionpvc.applications.Exceptions.CustomBadRequestException;
import com.gestionpvc.applications.dtos.sale.UpdateSaleDto;
import com.gestionpvc.applications.dtos.sale.SaleDto;
import com.gestionpvc.applications.dtos.sale.SaleDetailsDto;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.domains.Sale;
import com.gestionpvc.domains.SaleDetails;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateSaleUseCase implements IUpdateSaleUseCase {
    private final ISaleRepository saleRepository;
    private final IProductRepository productRepository;

    public UpdateSaleUseCase(ISaleRepository saleRepository, IProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public SaleDto execute(UpdateSaleDto data) {
        var existingOpt = saleRepository.getById(data.id);
        if (existingOpt.isEmpty()) {
            throw new CustomNotFoundException("Venta no encontrada");
        }

        // Validar y obtener productos
        List<SaleDetails> saleDetailsList = data.items.stream()
                .map(item -> {
                    var product = productRepository.getById(item.productId)
                            .orElseThrow(() -> new CustomNotFoundException(
                                    "Producto con ID " + item.productId + " no encontrado"));
                    
                    // Validar stock disponible
                    if (product.stock() < item.quantity) {
                        throw new CustomBadRequestException(
                                "Stock insuficiente para " + product.name() + 
                                ". Disponible: " + product.stock());
                    }

                    double profit = (item.salePrice - product.purchaseValue()) * item.quantity;
                    
                    return new SaleDetails(
                            item.productId,
                            product,
                            product.name(),
                            item.quantity,
                            item.salePrice,
                            profit
                    );
                })
                .collect(Collectors.toList());

        // Calcular total
        double total = saleDetailsList.stream()
                .mapToDouble(item -> item.salePrice() * item.quantity())
                .sum();

        // Actualizar venta
        Sale current = existingOpt.get();
        Sale toUpdate = new Sale(
                data.id,
                current.createdAt(),
                saleDetailsList,
                total,
                data.numberBill
        );

        Sale updated = saleRepository.update(toUpdate);
        return mapToDto(updated);
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
