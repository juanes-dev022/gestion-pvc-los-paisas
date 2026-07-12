package com.gestionpvc.applications.interfaces.iusecase.isaleusecase;

import com.gestionpvc.applications.interfaces.iusecase.IbaseUsecase;
import com.gestionpvc.applications.dtos.sale.SaleDto;
import java.util.List;

public interface IGetAllSalesUseCase extends IbaseUsecase<Void, List<SaleDto>> {}
