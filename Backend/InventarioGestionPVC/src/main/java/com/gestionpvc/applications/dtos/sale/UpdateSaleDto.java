package com.gestionpvc.applications.dtos.sale;

import java.util.List;

public class UpdateSaleDto extends CreateSaleDto {
    public String id;

    public UpdateSaleDto() {}

    public UpdateSaleDto(String id, List<CreateSaleItemDto> items, String numberBill) {
        super(items, numberBill);
        this.id = id;
    }
}
