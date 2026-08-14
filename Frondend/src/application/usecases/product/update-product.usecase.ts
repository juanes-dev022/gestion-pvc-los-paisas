import type { UpdateProductDto } from '$application/dtos/product.dto'
import type { IProductGateway } from '$application/interfaces/product-gateway.interface'
import type { Product } from '$domain/product/Product'

export class UpdateProductUseCase {
  constructor(private readonly gateway: IProductGateway) {}

  async execute(input: UpdateProductDto): Promise<Product> {
    const name = input.name?.trim()
    const { purchaseValue, stock } = input
    if (!name) throw new Error('El nombre es obligatorio')
    if (purchaseValue == null || stock == null) {
      throw new Error('Faltan valores numéricos')
    }
    if (purchaseValue < 0) throw new Error('Los valores deben ser positivos')
    if (stock < 0) throw new Error('El stock debe ser positivo')

    return this.gateway.update({ ...input, name })
  }
}
