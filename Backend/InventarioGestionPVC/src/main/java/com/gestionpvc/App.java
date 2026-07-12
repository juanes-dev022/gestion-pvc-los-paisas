package com.gestionpvc;


import javax.sql.DataSource;

import com.gestionpvc.applications.usecase.productusecase.CreateProductUseCase;
import com.gestionpvc.applications.usecase.productusecase.DeleteProductUseCase;
import com.gestionpvc.applications.usecase.productusecase.GetAllProductUseCase;
import com.gestionpvc.applications.usecase.productusecase.UpdateProductUseCase;
import com.gestionpvc.applications.usecase.productusecase.GetProductUseCase;
import com.gestionpvc.applications.usecase.saleusecase.CreateSaleUseCase;
import com.gestionpvc.applications.usecase.saleusecase.DeleteSaleUseCase;
import com.gestionpvc.applications.usecase.saleusecase.GetAllSalesUseCase;
import com.gestionpvc.applications.usecase.saleusecase.UpdateSaleUseCase;
import com.gestionpvc.applications.usecase.saleusecase.GetSaleUseCase;
import com.gestionpvc.infrastructures.repositories.ProductRepository;
import com.gestionpvc.infrastructures.repositories.SaleRepository;
import com.gestionpvc.infrastructures.config.DbConfigurator;
import com.gestionpvc.controllers.ProductController;
import com.gestionpvc.controllers.SaleController;
import io.javalin.Javalin;


public class App {
    public static void main(String[] args) {
        DataSource dataSource = DbConfigurator.createDataSource();
        
        // 🏭 Repositorios
        ProductRepository productRepo = new ProductRepository(dataSource);
        SaleRepository saleRepo = new SaleRepository(dataSource);

        // 📦 Use Cases - Productos
        GetAllProductUseCase getAllProducts = new GetAllProductUseCase(productRepo);        
        var getProduct = new GetProductUseCase(productRepo);           
        var createProduct = new CreateProductUseCase(productRepo);
        var deleteProduct = new DeleteProductUseCase(productRepo);        
        var updateProduct = new UpdateProductUseCase(productRepo);        

        // 📦 Use Cases - Ventas
        GetAllSalesUseCase getAllSales = new GetAllSalesUseCase(saleRepo);
        var getSale = new GetSaleUseCase(saleRepo);
        var createSale = new CreateSaleUseCase(saleRepo, productRepo);
        var deleteSale = new DeleteSaleUseCase(saleRepo);
        var updateSale = new UpdateSaleUseCase(saleRepo, productRepo);

        // 🎮 Controladores
        var productController = new ProductController(getAllProducts, getProduct, createProduct, updateProduct, deleteProduct);
        var saleController = new SaleController(getAllSales, getSale, createSale, updateSale, deleteSale);
        
        var app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> {
                it.anyHost();
                it.allowCredentials = true;
            }));
        });
        
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();                
            ctx.status(500).json("Error interno");
        });
        
        // 📡 Registrar rutas
        productController.register(app);
        saleController.register(app);
        
        app.start(7070);

        System.out.println("✅ Productos en base: " + productRepo.getAll().size());
        System.out.println("✅ Ventas en base: " + saleRepo.getAll().size());
    }
}
