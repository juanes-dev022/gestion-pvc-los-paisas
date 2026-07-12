package com.gestionpvc.applications.dtos.product;

public class UpdateProductDto extends CreateProductDto {

    public UpdateProductDto() {
    }

    public UpdateProductDto(String id, String name, double purchaseValue, int stock, boolean active) {
        super(id, name, purchaseValue, stock, active);
    }
}
