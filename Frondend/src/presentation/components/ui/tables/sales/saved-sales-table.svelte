<script lang="ts">
  import type { Sale } from '$domain/Sales/Sale'
  import '../data-table.css'

  let { sales }: { sales: Sale[] } = $props()
</script>

{#if sales.length > 0}
  <div class="table-wrapper">
    <table>
      <thead>
        <tr>
          <th>#</th>
          <th>Fecha</th>
          <th>Productos</th>
          <th>Total</th>
        </tr>
      </thead>
      <tbody>
        {#each sales as sale, i (sale.id)}
          <tr>
            <td>{i + 1}</td>
            <td>{sale.createdAt.toLocaleString('es-CO')}</td>
            <td>{sale.items.map((it) => it.product.name).join(', ')}</td>
            <td><strong>${sale.total.toFixed(2)}</strong></td>
          </tr>
        {/each}
      </tbody>
    </table>
  </div>
{:else}
  <p class="empty-msg">No hay ventas guardadas aún.</p>
{/if}