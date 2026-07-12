package com.gestionpvc.controllers;

import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.ICreateSaleUseCase;
import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IDeleteSaleUseCase;
import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IGetAllSalesUseCase;
import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IGetSaleUseCase;
import com.gestionpvc.applications.interfaces.iusecase.isaleusecase.IUpdateSaleUseCase;
import com.gestionpvc.applications.Exceptions.CustomBadRequestException;
import com.gestionpvc.applications.Exceptions.CustomNotFoundException;
import com.gestionpvc.applications.dtos.sale.CreateSaleDto;
import com.gestionpvc.applications.dtos.sale.CreateSaleItemDto;
import com.gestionpvc.applications.dtos.sale.UpdateSaleDto;
import com.gestionpvc.applications.dtos.sale.SaleDto;

import io.javalin.http.Context;
import io.javalin.Javalin;
import java.util.List;

public final class SaleController {
    private final IGetAllSalesUseCase getAllUseCase;
    private final IGetSaleUseCase getUseCase;
    private final ICreateSaleUseCase createUseCase;
    private final IUpdateSaleUseCase updateUseCase;
    private final IDeleteSaleUseCase deleteUseCase;

    public SaleController(
            IGetAllSalesUseCase getAllUseCase,
            IGetSaleUseCase getUseCase,
            ICreateSaleUseCase createUseCase,
            IUpdateSaleUseCase updateUseCase,
            IDeleteSaleUseCase deleteUseCase) {
        this.getAllUseCase = getAllUseCase;
        this.getUseCase = getUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    // Inyección de dependencias, queda pendiente ponerlo en otro lado.
    public void register(Javalin app) {
        app.get("/sales", this::getAll);
        app.get("/sales/{id}", this::get);
        app.post("/sales", this::create);
        app.put("/sales/{id}", this::update);
        app.delete("/sales/{id}", this::delete);
    }

    private void getAll(Context ctx) {
        try {
            List<SaleDto> sales = getAllUseCase.execute(null);
            ctx.json(sales);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void get(Context ctx) {
        try {
            String id = parseId(ctx);
            SaleDto sale = getUseCase.execute(id);
            ctx.json(sale);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void create(Context ctx) {
        try {
            CreateSaleDto dto = ctx.bodyAsClass(CreateSaleDto.class);
            validateCreateSaleDto(dto);
            SaleDto created = createUseCase.execute(dto);
            ctx.status(201).json(created);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void update(Context ctx) {
        try {
            String id = parseId(ctx);
            UpdateSaleDto dto = ctx.bodyAsClass(UpdateSaleDto.class);
            dto.id = id;
            validateCreateSaleDto(dto);
            SaleDto updated = updateUseCase.execute(dto);
            ctx.json(updated);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void delete(Context ctx) {
        try {
            String id = parseId(ctx);
            Boolean ok = deleteUseCase.execute(id);
            if (Boolean.TRUE.equals(ok)) {
                ctx.status(204);
            } else {
                ctx.status(404).json("Venta no encontrada");
            }
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    // 🔧 Métodos auxiliares
    private String parseId(Context ctx) {
        try {
            return ctx.pathParam("id");
        } catch (NumberFormatException ex) {
            throw new CustomBadRequestException("Identificador de venta inválido");
        }
    }

    private void validateCreateSaleDto(CreateSaleDto dto) {
        // Validar items
        if (dto.items == null || dto.items.isEmpty()) {
            throw new CustomBadRequestException("La venta debe contener al menos un producto");
        }

        // Validar cada item
        for (CreateSaleItemDto item : dto.items) {
            validateSaleItem(item);
        }

        // Validar número de factura
        if (dto.numberBill == null || dto.numberBill.isBlank()) {
            throw new CustomBadRequestException("El número de factura no puede estar vacío");
        }
    }

    private void validateSaleItem(CreateSaleItemDto item) {
        if (item.productId == null || item.productId.isBlank()) {
            throw new CustomBadRequestException("El ID del producto no puede estar vacío");
        }

        if (item.quantity <= 0) {
            throw new CustomBadRequestException("La cantidad debe ser mayor a 0");
        }

        if (item.salePrice <= 0) {
            throw new CustomBadRequestException("El precio de venta debe ser mayor a 0");
        }
    }

    private void handleError(Context ctx, Exception ex) {
        if (ex instanceof CustomNotFoundException) {
            ctx.status(404).json(ex.getMessage());
        } else if (ex instanceof CustomBadRequestException) {
            ctx.status(400).json(ex.getMessage());
        } else {
            ctx.status(500).json("Error interno del servidor");
        }
    }
}
