package com.gestionpvc.domains;

public record SaleDetails(
    String productId,
    Product product,
    String name,
    int quantity,
    double salePrice,
    double profit       // calculado: (salePrice - purchaseValue) * quantity
) {}
