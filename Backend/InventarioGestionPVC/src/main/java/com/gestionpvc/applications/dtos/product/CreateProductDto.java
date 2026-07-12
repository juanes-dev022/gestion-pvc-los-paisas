package com.gestionpvc.applications.dtos.product;

public class CreateProductDto extends ProductDto {

    public CreateProductDto() {}

    public CreateProductDto(String id, String name, double purchaseValue, int stock, boolean active) {
        super(id, name, purchaseValue, stock, active);
    }    
}
