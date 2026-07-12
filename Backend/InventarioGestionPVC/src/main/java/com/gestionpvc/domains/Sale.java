package com.gestionpvc.domains;

import java.time.LocalDateTime;
import java.util.List;

public record Sale(
    String id,
    LocalDateTime createdAt,
    List<SaleDetails> items,
    double total,
    String numberBill
) {}