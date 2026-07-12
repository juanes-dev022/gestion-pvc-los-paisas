import type { SaleItem } from "./SaleItem"

export interface Sale {
    id: string
    createdAt: Date
    items: SaleItem[]
    total: number
    numberBill: string
}