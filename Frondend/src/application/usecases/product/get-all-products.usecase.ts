import type { IProductGateway } from '$application/interfaces/product-gateway.interface'
import type { Product } from '$domain/product/Product'

export class GetAllProductsUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(): Promise<Product[]> {
    return this.gateway.getAll()
  }
}
