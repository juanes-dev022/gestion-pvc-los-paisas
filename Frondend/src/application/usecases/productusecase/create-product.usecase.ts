import type { CreateProductDto } from '$application/dtos/Product-dto'
import type { IProductGateway } from '$application/interfaces/iproduct-gateway'
import type { Product } from '$domain/Products/Product'

export class CreateProductUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(input: CreateProductDto): Promise<Product> {
    const name = input.name?.trim()
    if (!name) throw new Error('El nombre es obligatorio')
    if (input.purchaseValue < 0) {
      throw new Error('Los valores deben ser positivos')
    }
    if (input.stock < 0) throw new Error('El stock debe ser positivo')

    return this.gateway.create({
      ...input,
      name,
      active: input.active ?? true
    })
  }
}
