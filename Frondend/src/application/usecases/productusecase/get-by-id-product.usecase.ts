import type { IProductGateway } from '$application/interfaces/iproduct-gateway'
import type { Product } from '$domain/Products/Product'

export class GetByIdProductUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(id: number): Promise<Product> {
    return this.gateway.getById(id)
  }
}
