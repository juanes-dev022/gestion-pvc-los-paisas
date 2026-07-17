import type { CreateProductDto, UpdateProductDto } from '$application/dtos/Product-dto'
import type { Product } from '$domain/Products/Product'

export interface IProductGateway {
  getAll(): Promise<Product[]>
  create(data: CreateProductDto): Promise<Product>
  update(data: UpdateProductDto): Promise<Product>
  delete(data: string): Promise<void>
  getById(data: string): Promise<Product>
}
