package com.gestionpvc.applications.dtos.sale;

import com.gestionpvc.applications.dtos.BaseDto;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDto extends BaseDto {
    public LocalDateTime createdAt;
    public List<SaleDetailsDto> items;
    public double total;
    public String numberBill;

    public SaleDto() {}

    public SaleDto(String id, LocalDateTime createdAt, List<SaleDetailsDto> items, 
                   double total, String numberBill) {
        super(id);
        this.createdAt = createdAt;
        this.items = items;
        this.total = total;
        this.numberBill = numberBill;
    }
}
