import type { IProductGateway } from '$application/interfaces/product-gateway.interface'
import type { Product } from '$domain/product/Product'

export class GetByIdProductUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(id: number): Promise<Product> {
    return this.gateway.getById(id)
  }
}
