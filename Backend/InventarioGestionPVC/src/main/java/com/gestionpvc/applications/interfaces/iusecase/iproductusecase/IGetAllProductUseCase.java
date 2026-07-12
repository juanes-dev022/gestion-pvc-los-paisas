package com.gestionpvc.applications.interfaces.iusecase.iproductusecase;

import com.gestionpvc.applications.interfaces.iusecase.IbaseUsecase;
import com.gestionpvc.applications.dtos.product.ProductDto;
import java.util.List;

public interface IGetAllProductUseCase extends IbaseUsecase<Void, List<ProductDto>> {}
