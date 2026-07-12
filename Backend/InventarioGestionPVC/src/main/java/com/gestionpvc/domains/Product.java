package com.gestionpvc.domains;

public record Product(
    String id,
    String name,
    double purchaseValue,
    int stock,
    boolean active
) {}


