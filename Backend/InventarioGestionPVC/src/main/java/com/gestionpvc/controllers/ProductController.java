package com.gestionpvc.controllers;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gestionpvc.App;
import com.gestionpvc.applications.Exceptions.CustomBadRequestException;
import com.gestionpvc.applications.Exceptions.CustomNotFoundException;
import com.gestionpvc.applications.dtos.product.CreateProductDto;
import com.gestionpvc.applications.dtos.product.ProductDto;
import com.gestionpvc.applications.dtos.product.UpdateProductDto;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.ICreateProductUseCase;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IDeleteProductUseCase;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IGetAllProductUseCase;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IGetProductUseCase;
import com.gestionpvc.applications.interfaces.iusecase.iproductusecase.IUpdateProductUseCase;

import io.javalin.Javalin;
import io.javalin.http.Context;

public final class ProductController {
    private final IGetAllProductUseCase getAllUseCase;
    private final IGetProductUseCase getUseCase;
    private final ICreateProductUseCase createUseCase;
    private final IUpdateProductUseCase updateUseCase;
    private final IDeleteProductUseCase deleteUseCase;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public ProductController(
            IGetAllProductUseCase getAllUseCase,
            IGetProductUseCase getUseCase,
            ICreateProductUseCase createUseCase,
            IUpdateProductUseCase updateUseCase,
            IDeleteProductUseCase deleteUseCase) {
        this.getAllUseCase = getAllUseCase;
        this.getUseCase = getUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
    }


    //inyeccion de depencias, queda pendiente ponerlo en otro lado.
    public void register(Javalin app) {
        app.get("/products", this::getAll);
        app.get("/products/{id}", this::get);
        app.post("/products", this::create);
        app.put("/products/{id}", this::update);
        app.delete("/products/{id}", this::delete);
    }

    private void getAll(Context ctx) {
        try {
            List<ProductDto> products = getAllUseCase.execute(null);
            ctx.json(products);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void get(Context ctx) {
        try {
            String id = parseId(ctx);
            ProductDto product = getUseCase.execute(id);
            ctx.json(product);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void create(Context ctx) {
        try {
            CreateProductDto dto = ctx.bodyAsClass(CreateProductDto.class);
            ProductDto created = createUseCase.execute(dto);
            ctx.status(201).json(created);
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private void update(Context ctx) {
        try {
            String id = parseId(ctx);
            UpdateProductDto dto = ctx.bodyAsClass(UpdateProductDto.class);
            dto.id = id;
            ProductDto updated = updateUseCase.execute(dto);
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
                ctx.status(404).json("No encontrado");
            }
        } catch (Exception ex) {
            handleError(ctx, ex);
        }
    }

    private String parseId(Context ctx) {
        try {
            return ctx.pathParam("id");
        } catch (NumberFormatException ex) {
            throw new CustomBadRequestException("Identificador inválido");
        }
    }

    private void handleError(Context ctx, Exception ex) {
        if (ex instanceof CustomNotFoundException) {
            ctx.status(404).json(ex.getMessage());
        } else if (ex instanceof CustomBadRequestException) {
            ctx.status(400).json(ex.getMessage());
        } else {
            logger.error("Error en {} {}", ctx.method(), ctx.path(), ex);
            ctx.status(500).json("Error internooo");
        }
    }
}


