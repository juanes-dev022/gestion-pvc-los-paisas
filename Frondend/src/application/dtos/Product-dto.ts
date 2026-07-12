import type { Product } from '$domain/Products/Product'

export type ProductDto = Product

export interface CreateProductDto {
  name: string
  purchaseValue: number
  stock: number
  active: boolean
}

export interface UpdateProductDto extends Partial<CreateProductDto> {
  id: number
}
