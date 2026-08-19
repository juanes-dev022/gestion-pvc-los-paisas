

import type { SaleItem } from '$domain/sale/SaleItem'
import type { Sale } from '$domain/sale/Sale'

export class SaleMapper {
  static toDomain(params: {
    id: string
    createdAt: Date
    items: SaleItem[]
    numberBill: string
  }): Sale {
    return {
      id: params.id,
      createdAt: params.createdAt,
      items: params.items,
      total: SaleMapper.calculateTotal(params.items),
      numberBill: params.numberBill,
    }
  }

  static calculateTotal(items: SaleItem[]): number {
    return items.reduce((sum, item) => sum + item.salePrice * item.quantity, 0)
  }

  // Normaliza la respuesta cruda del API (createdAt llega como string)
  static fromApi(raw: any): Sale {
    return {
      id: raw.id,
      createdAt: new Date(raw.createdAt),
      items: raw.items,
      total: raw.total,
      numberBill: raw.numberBill,
    }
  }
}