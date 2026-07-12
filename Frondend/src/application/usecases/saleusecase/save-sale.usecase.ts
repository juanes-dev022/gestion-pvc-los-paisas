import type { ISaleGateway }  from '$application/interfaces/isale-gateway'
import type { CreateSaleDto } from '$application/dtos/Sale-dto'
import type { Sale }  from '$domain/Sales/Sale'

export class SaveSaleUseCase {
  constructor(private readonly gateway: ISaleGateway) {}

  async execute(dto: CreateSaleDto): Promise<Sale> {
    if (!dto.items.length) throw new Error('La venta debe tener al menos un producto')

    return this.gateway.save(dto)
  }
}