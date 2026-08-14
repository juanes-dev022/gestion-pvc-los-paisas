import type { ISaleGateway } from '$application/interfaces/sale-gateway.interface'
import type { Sale } from '$domain/sale/Sale'

export class GetAllSalesUseCase {
  constructor(private readonly gateway: ISaleGateway) {}

  async execute(): Promise<Sale[]> {
    return this.gateway.getAll()
  }
}