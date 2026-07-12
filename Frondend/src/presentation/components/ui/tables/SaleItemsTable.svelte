<script lang="ts">
  import type { SaleItem } from '$domain/Sales/SaleItem'
  import './data-table.css'

  let {
    items = [],
    onUpdateItem,
    onRemoveItem  
  }: {
    items: SaleItem[]
    onUpdateItem: (productId: string, field: 'quantity' | 'salePrice', value: number) => void
    onRemoveItem: (productId: string) => void
  } = $props()

  function onFieldChange(productId: string, field: 'quantity' | 'salePrice', event: Event) {
    const value = parseFloat((event.target as HTMLInputElement).value)
    if (!isNaN(value) && value >= 0) {
      onUpdateItem(productId, field, value)
    }
  }
</script>

<section class="table-card">
  <div class="table-wrapper">
    {#if items.length === 0}
      <p class="empty-message">No hay items registrados en la tabla</p>
    {:else}
      <table>
        <thead>
          <tr>
            <th>Producto</th>
            <th>Cantidad</th>
            <th>Precio de venta</th>
            <th>Subtotal</th>
            <th class="actions"></th>
          </tr>
        </thead>
        <tbody>
          {#each items as item (item.productId)}
            <tr>
              <td>{item.name}</td>
              <td>
                <input
                  type="number"
                  min="1"
                  value={item.quantity}
                  onchange={(e) => onFieldChange(item.productId, 'quantity', e)}
                />
              </td>
              <td>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={item.salePrice}
                  onchange={(e) => onFieldChange(item.productId, 'salePrice', e)}
                />
              </td>
              <td>{(item.quantity * item.salePrice).toFixed(2)}</td>
              <td class="actions">
                <button onclick={() => onRemoveItem(item.productId)}> Eliminar </button>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>
    {/if}
  </div>
</section>

<style>
  .empty-message {
    text-align: center;
    color: #7b8190;
    font-style: italic;
    padding: 2rem 1rem;
    font-size: 0.95rem;
  }
</style>
