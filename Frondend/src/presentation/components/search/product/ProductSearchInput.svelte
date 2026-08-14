<script lang="ts">
  import type { Product } from '$domain/product/Product'
  import './product-search-input.css'

  let {
    products = [],
    placeholder = 'Buscar producto por nombre o ID...',
    onselect

  }:{
    products: Product[],
    placeholder?: string,
    onselect?: (product: Product) => void
  } = $props()

  let query = $state('')
  let filtered = $state<Product[]>([])
  let showDropdown = $state(false)

  function handleInput() {
    const q = query.trim().toLowerCase()
    if (!q) {
      filtered = []
      showDropdown = false
      return
    }
    filtered = products.filter((p) => p.name.toLowerCase().includes(q) || String(p.id).includes(q))
    showDropdown = filtered.length > 0
  }

  function handleSelect(product: Product) {
    onselect?.(product)
    query = ''
    filtered = []
    showDropdown = false
  }

  function handleBlur() {
    setTimeout(() => (showDropdown = false), 150)
  }
</script>

<div class="search-wrapper">
  <input
    type="text"
    bind:value={query}
    oninput={handleInput}
    onblur={handleBlur}
    onfocus={handleInput}
    {placeholder}
    class="search-input"
  />

  {#if showDropdown}
    <ul class="dropdown">
      {#each filtered as product (product.id)}
        <li class="dropdown-item" onmousedown={() => handleSelect(product)}>
          <span class="product-name">{product.name}</span>
          <span class="product-id">#{product.id}</span>
        </li>
      {/each}
    </ul>
  {/if}
</div>
