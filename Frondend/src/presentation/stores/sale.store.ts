import { HttpSaleGateway } from '$infrastructure/api/sale/http-sale-gateway'
import { GetAllSalesUseCase }  from '$application/usecases/sale/get-all-sales.usecase'
import { SaveSaleUseCase }     from '$application/usecases/sale/save-sale.usecase'
import type { Sale }           from '$domain/sale/Sale'
import type { CreateSaleItemDto, CreateSaleDto, UpdateSaleDto } from '$application/dtos/sale.dto'
import { writable }            from 'svelte/store'

const gateway          = new HttpSaleGateway()
const saveSaleUseCase  = new SaveSaleUseCase(gateway)
const getAllSalesUseCase = new GetAllSalesUseCase(gateway)

export const sales   = writable<Sale[]>([])
export const saving  = writable(false)
export const saveError = writable<string | null>(null)

export async function initSales(): Promise<void> {
  sales.set(await getAllSalesUseCase.execute())
}

export async function saveSale(items: CreateSaleItemDto[], numberBill: string): Promise<Sale> {
  saving.set(true)
  saveError.set(null)
  try {
    const dto: CreateSaleDto = { items, numberBill }
    const saved = await saveSaleUseCase.execute(dto)
    sales.update((prev) => [...prev, saved])
    return saved
  } catch (err) {
    const msg = err instanceof Error ? err.message : 'Error al guardar'
    saveError.set(msg)
    throw err
  } finally {
    saving.set(false)
  }
}

export async function getSale(id: string): Promise<Sale> {
  try {
    return await gateway.getById(id)
  } catch (err) {
    const msg = err instanceof Error ? err.message : 'Error al obtener venta'
    saveError.set(msg)
    throw err
  }
}

export async function updateSale(data: UpdateSaleDto): Promise<Sale> {
  saving.set(true)
  saveError.set(null)
  try {
    const updated = await gateway.update(data)
    sales.update((prev) => prev.map((s) => (s.id === updated.id ? updated : s)))
    return updated
  } catch (err) {
    const msg = err instanceof Error ? err.message : 'Error al actualizar'
    saveError.set(msg)
    throw err
  } finally {
    saving.set(false)
  }
}

export async function deleteSale(id: string): Promise<void> {
  saving.set(true)
  saveError.set(null)
  try {
    await gateway.delete(id)
    sales.update((prev) => prev.filter((s) => s.id !== id))
  } catch (err) {
    const msg = err instanceof Error ? err.message : 'Error al eliminar'
    saveError.set(msg)
    throw err
  } finally {
    saving.set(false)
  }
}