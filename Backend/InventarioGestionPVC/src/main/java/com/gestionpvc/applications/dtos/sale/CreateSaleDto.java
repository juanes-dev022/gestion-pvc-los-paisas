package com.gestionpvc.applications.dtos.sale;

import java.util.List;

public class CreateSaleDto {
    public List<CreateSaleItemDto> items;
    public String numberBill;

    public CreateSaleDto() {}

    public CreateSaleDto(List<CreateSaleItemDto> items, String numberBill) {
        this.items = items;
        this.numberBill = numberBill;
    }
}
