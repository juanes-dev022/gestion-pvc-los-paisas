<script lang="ts">
  import ProductSearchInput from '$presentation/components/search/sales-search/product-search/product-search-input.svelte'
  import { GetAllProductsUseCase } from '$application/usecases/productusecase/get-all-products.usecase'
  import SaleDetailsTable  from '$presentation/components/ui/tables/sales/sale-items-table.svelte'
  import SavedSalesTable from '$presentation/components/ui/tables/sales/saved-sales-table.svelte'
  // ── Botón nuevo ────────────────────────────────────────────
  import SaveSaleButton  from '$presentation/components/ui/buttons/save-sale-button.svelte'
  import { HttpProductGateway } from '$infrastructure/api/product/http-product-gateway'
  import AppHeader       from '$presentation/components/ui/header/AppHeader.svelte'
  import { sales, saving, saveError, saveSale, initSales } from '$presentation/stores/sale.store'
  import type { Product }  from '$domain/Products/Product'
  import type { SaleItem } from '$domain/Sales/SaleItem'
  import type { CreateSaleItemDto } from '$application/dtos/Sale-dto'
  import { onMount }       from 'svelte'
  import './SalesListPage.css'

  const gateway = new HttpProductGateway()
  const getAllProductsUseCase = new GetAllProductsUseCase(gateway)

  // ── Estado ─────────────────────────────────────────────────
  let loading = true
  let error: string | null = null
  let allProducts: Product[] = []
  let SaleDetails: SaleItem[] = []
  let successMsg: string | null = null
  let numberBill: string = ''

  // ── Carga inicial ──────────────────────────────────────────
  onMount(async () => {
    try {
      console.log('📥 Cargando productos...')
      allProducts = await getAllProductsUseCase.execute()
      console.log('✅ Productos cargados:', allProducts)
      console.log('📊 Primer producto:', allProducts[0])
      if (allProducts[0]) {
        console.log('🔍 ID del primer producto:', allProducts[0].id, 'Tipo:', typeof allProducts[0].id)
      }
      await initSales()
    } catch (err) {
      error = err instanceof Error ? err.message : 'Error desconocido'
      console.error('❌ Error al cargar:', err)
    } finally {
      loading = false
    }
  })

  // ── Handlers ───────────────────────────────────────────────
  function handleProductSelect(product: Product) {
    if (SaleDetails.some((i) => i.product.id === product.id)) return
    SaleDetails = [
      ...SaleDetails,
      {
        productId: String(product.id),
        product,
        name: product.name,
        quantity: 1,
        salePrice: product.purchaseValue ?? 0
      }
    ]
  }

  function handleUpdateItem(detail: {
    productId: string
    field: 'quantity' | 'salePrice'
    value: number
  }) {
    SaleDetails = SaleDetails.map((item) =>
      item.product.id === detail.productId ? { ...item, [detail.field]: detail.value } : item
    )
  }

  function handleRemoveItem(productId: string) {
    SaleDetails = SaleDetails.filter((item) => item.product.id !== productId)
  }

  async function handleSaveSale() {
    console.log('🔵 Click en botón de guardar - handleSaveSale ejecutada')
    console.log('📦 SaleDetails:', SaleDetails)
    console.log('📝 numberBill:', numberBill)
    console.log('✅ Validación: SaleDetails.length =', SaleDetails.length, 'numberBill.trim() =', numberBill.trim())

    if (!numberBill.trim()) {
      error = 'El número de factura es requerido'
      console.log('❌ Error: número de factura vacío')
      return
    }

    if (SaleDetails.length === 0) {
      error = 'Debe agregar al menos un producto a la venta'
      console.log('❌ Error: SaleDetails vacío')
      return
    }

    try {
      console.log('🟢 Pasó validaciones, mapeando items...')
      const saleItemsForBackend: CreateSaleItemDto[] = SaleDetails.map((item) => {
        console.log('Item:', item)
        return {
          productId: item.productId,
          quantity: item.quantity,
          salePrice: item.salePrice
        }
      })
      console.log('📤 Items a enviar:', saleItemsForBackend)
      console.log('📤 NumberBill a enviar:', numberBill)

      await saveSale(saleItemsForBackend, numberBill)
      SaleDetails = []
      numberBill = ''
      successMsg = '✅ Venta guardada exitosamente'
      setTimeout(() => (successMsg = null), 3000)
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Error desconocido'
      error = `Error al guardar venta: ${errorMsg}`
      console.error('❌ Error capturado:', err)
      console.error('Mensaje de error:', errorMsg)
    }
  }
</script>

<div class="layout">
  <section>
    <AppHeader title="Ventas" subtitle="Gestión de ventas" />

    {#if loading}
      <p>Cargando productos...</p>
    {:else if error}
      <p class="error">{error}</p>
    {:else}
      <div class="sales-content">
        <h3>Agregar productos a la venta</h3>

        <ProductSearchInput products={allProducts} onselect={handleProductSelect} />

        <SaleDetailsTable
          items={SaleDetails}
          onUpdateItem={handleUpdateItem}
          onRemoveItem={handleRemoveItem}
        />

        <div style="margin: 1.5rem 0; padding: 1rem; background: #f5f5f5; border-radius: 8px;">
          <label for="numberBill" style="display: block; margin-bottom: 0.5rem; font-weight: 500;">
            Número de Factura
          </label>
          <input
            id="numberBill"
            type="text"
            bind:value={numberBill}
            placeholder="Ej: FAC-001"
            style="width: 100%; padding: 0.5rem; border: 1px solid #ddd; border-radius: 4px; font-size: 1rem;"
          />
        </div>

        {#if $saveError}
          <p class="error">{$saveError}</p>
        {/if}
        {#if successMsg}
          <p class="success">{successMsg}</p>
        {/if}

        <!-- ── Botón reutilizable ──────────────────────────── -->
        <SaveSaleButton
          saving={$saving}
          disabled={SaleDetails.length === 0 || !numberBill.trim()}
          onclick={handleSaveSale}
        />

        <h3>Ventas guardadas</h3>
        <SavedSalesTable sales={$sales} />
      </div>
    {/if}
  </section>
</div>

