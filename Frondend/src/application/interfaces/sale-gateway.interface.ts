

import type { CreateSaleDto, UpdateSaleDto } from '$application/dtos/sale.dto'
import type { Sale } from '$domain/sale/Sale'

export interface ISaleGateway {
  save(data: CreateSaleDto): Promise<Sale>
  getAll(): Promise<Sale[]>
  getById(id: string): Promise<Sale>
  update(data: UpdateSaleDto): Promise<Sale>
  delete(id: string): Promise<void>
}