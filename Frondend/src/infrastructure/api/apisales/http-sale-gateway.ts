import type { CreateSaleDto, UpdateSaleDto } from '$application/dtos/Sale-dto'
import type { ISaleGateway } from '$application/interfaces/isale-gateway'
import type { Sale } from '$domain/Sales/Sale'
import { httpClient } from '$infrastructure/api/http-client'

export class HttpSaleGateway implements ISaleGateway {
  async save(data: CreateSaleDto): Promise<Sale> {
    return httpClient.post<CreateSaleDto, Sale>('/sales', data)
  }

  async getAll(): Promise<Sale[]> {
    return httpClient.get<Sale[]>('/sales')
  }

  async getById(id: string): Promise<Sale> {
    return httpClient.get<Sale>(`/sales/${id}`)
  }

  async update(data: UpdateSaleDto): Promise<Sale> {
    return httpClient.put<UpdateSaleDto, Sale>(`/sales/${data.id}`, data)
  }

  async delete(id: string): Promise<void> {
    return httpClient.delete<void>(`/sales/${id}`)
  }
}
