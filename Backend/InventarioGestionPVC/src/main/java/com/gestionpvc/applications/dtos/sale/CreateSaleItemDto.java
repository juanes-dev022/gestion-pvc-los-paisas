package com.gestionpvc.applications.dtos.sale;

public class CreateSaleItemDto {
    public String productId;
    public int quantity;
    public double salePrice;

    public CreateSaleItemDto() {}

    public CreateSaleItemDto(String productId, int quantity, double salePrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }
}
