import type { IProductGateway } from '$application/interfaces/iproduct-gateway'
import type { Product } from '$domain/Products/Product'

export class GetAllProductsUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(): Promise<Product[]> {
    return this.gateway.getAll()
  }
}
