package com.gestionpvc.applications.dtos.sale;

import com.gestionpvc.applications.dtos.product.ProductDto;

public class SaleDetailsDto {
    public String productId;
    public ProductDto product;
    public String name;
    public int quantity;
    public double salePrice;
    public double profit; // (salePrice - purchaseValue) * quantity

    public SaleDetailsDto() {}

    public SaleDetailsDto(String productId, ProductDto product, String name, 
                         int quantity, double salePrice, double profit) {
        this.productId = productId;
        this.product = product;
        this.name = name;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.profit = profit;
    }
}
