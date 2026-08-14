import type { CreateProductDto, UpdateProductDto } from '$application/dtos/product.dto'
import type { IProductGateway } from '$application/interfaces/product-gateway.interface'
import type { Product } from '$domain/product/Product'
import { httpClient } from '$infrastructure/api/http-client'

export class HttpProductGateway implements IProductGateway {
  async getAll(): Promise<Product[]> {
    return httpClient.get<Product[]>('/products')
  }

  async create(data: CreateProductDto): Promise<Product> {
    return httpClient.post<CreateProductDto, Product>('/products', data)
  }

  async update(data: UpdateProductDto): Promise<Product> {
    return httpClient.put<UpdateProductDto, Product>(`/products/${data.id}`, data)
  }

  async delete(id: string): Promise<void> {
    return httpClient.delete<void>(`/products/${id}`)
  }

  async getById(id: string): Promise<Product> {
    return httpClient.get<Product>(`/product/${id}`)
  }
}
