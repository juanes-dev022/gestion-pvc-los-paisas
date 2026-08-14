import type { SaleItem } from '$domain/sale/SaleItem'
import type { Sale } from '$domain/sale/Sale'

// DTO para items de venta sin datos del producto completo
export interface CreateSaleItemDto {
  productId: string
  quantity: number
  salePrice: number
}

export interface CreateSaleDto {
  items: CreateSaleItemDto[]
  numberBill: string
}

export interface UpdateSaleDto extends CreateSaleDto {
  id: string
}

export type SaleDto = Sale