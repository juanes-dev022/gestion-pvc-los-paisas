import type { Product } from '$domain/product/Product'

export interface SaleItem {
  productId: string
  product: Product
  name: string
  quantity: number
  salePrice: number
}
