<script lang="ts">
  import type { Product } from '$domain/product/Product'

  export let products: Product[] = []
</script>

<table>
  <thead>
    <tr>
      <th>ID</th>
      <th>Nombre</th>
      <th>Valor compra</th>
      <th>Stock</th>
      <th>Activo</th>
      <th>Acciones</th>
    </tr>
  </thead>
  <tbody>
    {#if products.length === 0}
      <tr>
        <td colspan="7">No hay productos disponibles.</td>
      </tr>
    {:else}
      {#each products as product (product.id)}
        <tr>
          <td>{product.id}</td>
          <td>{product.name}</td>
          <td>{product.purchaseValue}</td>
          <td>{product.stock}</td>
          <td>{product.active ? 'Sí' : 'No'}</td>
          <td>
            <slot name="actions" {product}></slot>
          </td>
        </tr>
      {/each}
    {/if}
  </tbody>
</table>
