package com.gestionpvc.applications.dtos.product;

import com.gestionpvc.applications.dtos.BaseDto;

public class ProductDto extends BaseDto {
    public String name;
    public double purchaseValue;
    public int stock;
    public boolean active;

    public ProductDto() {}

    public ProductDto(String id, String name, double purchaseValue, int stock, boolean active) {
        super(id);
        this.name = name;
        this.purchaseValue = purchaseValue;
        this.stock = stock;
        this.active = active;
    }
}
