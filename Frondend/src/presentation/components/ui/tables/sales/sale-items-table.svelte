<script lang="ts">
  import type { SaleItem } from '$domain/Sales/SaleItem'
  import '../data-table.css'

  let {
    items = [],
    onUpdateItem,
    onRemoveItem
  }: {
    items: SaleItem[]
    onUpdateItem?: (detail: { productId: string; field: 'quantity' | 'salePrice'; value: number }) => void
    onRemoveItem?: (productId: string) => void
  } = $props()

  const total = $derived(items.reduce((sum, item) => sum + item.quantity * item.salePrice, 0))

  function handleFieldChange(productId: string, field: 'quantity' | 'salePrice', value: string) {
    const parsed = parseFloat(value)
    if (!isNaN(parsed) && parsed >= 0) {
      onUpdateItem?.({ productId, field, value: parsed })
    }
  }

  function handleRemove(productId: string) {
    onRemoveItem?.(productId)
  }
</script>

{#if items.length > 0}
  <div class="table-wrapper">
    <table class="sale-table">
      <thead>
        <tr>
          <th>#ID</th>
          <th>Producto</th>
          <th>Cantidad</th>
          <th>Precio venta</th>
          <th>Subtotal</th>
          <th>Acción</th>
        </tr>
      </thead>
      <tbody>
        {#each items as item (item.product.id)}
          <tr>
            <td>#{item.product.id}</td>
            <td>{item.product.name}</td>
            <td>
              <input
                type="number"
                min="1"
                value={item.quantity}
                onchange={(e) => handleFieldChange(item.product.id, 'quantity', e.currentTarget.value)}
                class="number-input"
              />
            </td>
            <td>
              <input
                type="number"
                min="0"
                step="0.01"
                value={item.salePrice}
                onchange={(e) => handleFieldChange(item.product.id, 'salePrice', e.currentTarget.value)}
                class="number-input"
              />
            </td>
            <td>${(item.quantity * item.salePrice).toFixed(2)}</td>
            <td>
              <button class="btn-remove" onclick={() => handleRemove(item.product.id)}>
                ✕
              </button>
            </td>
          </tr>
        {/each}
      </tbody>
      <tfoot>
        <tr>
          <td colspan="4"><strong>Total</strong></td>
          <td colspan="2"><strong>${total.toFixed(2)}</strong></td>
        </tr>
      </tfoot>
    </table>
  </div>
{:else}
  <p class="empty-msg">No hay productos en la venta. Busca y agrega productos.</p>
{/if}
