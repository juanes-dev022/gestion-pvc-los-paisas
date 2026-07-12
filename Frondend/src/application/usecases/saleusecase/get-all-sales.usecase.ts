import type { ISaleGateway } from '$application/interfaces/isale-gateway'
import type { Sale } from '$domain/Sales/Sale'

export class GetAllSalesUseCase {
  constructor(private readonly gateway: ISaleGateway) {}

  async execute(): Promise<Sale[]> {
    return this.gateway.getAll()
  }
}