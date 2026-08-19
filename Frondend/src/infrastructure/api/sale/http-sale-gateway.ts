

import type { ISaleGateway } from '$application/interfaces/sale-gateway.interface'
import type { CreateSaleDto, UpdateSaleDto } from '$application/dtos/sale.dto'
import { httpClient } from '$infrastructure/api/http-client'
import type { Sale } from '$domain/sale/Sale'

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
