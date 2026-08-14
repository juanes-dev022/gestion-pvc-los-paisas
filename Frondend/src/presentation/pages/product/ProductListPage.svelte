<script lang="ts">
  import CreateProductModal from '$presentation/components/ui/modals/product-modals/CreateProductModal.svelte'
  import UpdateProductModal from '$presentation/components/ui/modals/product-modals/UpdateProductModal.svelte'
  import { GetAllProductsUseCase } from '$application/usecases/product/get-all-products.usecase'
  import { DeleteProductUseCase } from '$application/usecases/product/delete-product.usecase'
  import DataTable, { type Column } from '$presentation/components/ui/tables/DataTable.svelte'
  import { HttpProductGateway } from '$infrastructure/api/product/http-product-gateway'
  import CreateButton from '$presentation/components/ui/buttons/CreateButton.svelte'
  import DeleteButton from '$presentation/components/ui/buttons/DeleteButton.svelte'
  import ConfirmModal from '$presentation/components/ui/modals/ConfirmModal.svelte'
  import EditButton   from '$presentation/components/ui/buttons/EditButton.svelte'
  import AppHeader    from '$presentation/components/ui/header/AppHeader.svelte'
  import type { Product } from '$domain/product/Product'
  import { onMount } from 'svelte'
  import './product-list-page.css'

  const gateway = new HttpProductGateway()
  const getAllProductsUseCase = new GetAllProductsUseCase(gateway)
  const deleteProductUseCase  = new DeleteProductUseCase(gateway)

  // ── Estado ─────────────────────────────────────────────────
  let products: Product[]                  = $state([])
  let tableRows: Record<string, unknown>[] = $state([])
  let loading                              = $state(true)
  let error: string | null                 = $state(null)
  let showCreateModal                      = $state(false)
  let showUpdateModal                      = $state(false)
  let showDeleteModal                      = $state(false)
  let selectedProduct: Product | null      = $state(null)

  // ── Crear ──────────────────────────────────────────────────
  const openCreateModal  = () => (showCreateModal = true)
  const closeCreateModal = () => (showCreateModal = false)

  function handleProductCreated(created: Product) {
    products  = [...products, created]
    tableRows = mapToRows(products)
  }

  // ── Editar ─────────────────────────────────────────────────
  function openUpdateModal(row: Record<string, unknown>) {
    selectedProduct = products.find((p) => p.id === String(row.id)) ?? null
    if (selectedProduct) showUpdateModal = true
  }

  const closeUpdateModal = () => {
    showUpdateModal = false
    selectedProduct = null
  }

  function handleProductUpdated(updated: Product) {
    products  = products.map((p) => (p.id === updated.id ? updated : p))
    tableRows = mapToRows(products)
  }

  // ── Eliminar ───────────────────────────────────────────────
  function openDeleteModal(row: Record<string, unknown>) {
    selectedProduct = products.find((p) => p.id === String(row.id)) ?? null
    if (selectedProduct) showDeleteModal = true
  }

  const closeDeleteModal = () => {
    showDeleteModal = false
    selectedProduct = null
  }

  async function handleDeleteConfirmed() {
    if (!selectedProduct) return
    try {
      await deleteProductUseCase.execute(selectedProduct.id)
      products  = products.filter((p) => p.id !== selectedProduct?.id)
      tableRows = mapToRows(products)
    } catch (err) {
      error = err instanceof Error ? err.message : 'Error al eliminar'
    } finally {
      closeDeleteModal()
    }
  }

  // ── Columnas de la tabla ───────────────────────────────────
  const productColumns: Column[] = [
    { key: 'id',            header: 'ID'          },
    { key: 'name',          header: 'Nombre'       },
    { key: 'purchaseValue', header: 'Valor compra' },
    { key: 'stock',         header: 'Stock'        },
    { key: 'active',        header: 'Activo', format: (v) => (v ? 'Sí' : 'No') },
    { key: 'action',        header: 'Acciones'     }
  ]

  const mapToRows = (items: Product[]): Record<string, unknown>[] =>
    items.map((item) => ({
      id:            item.id,
      name:          item.name,
      purchaseValue: item.purchaseValue,
      stock:         item.stock,
      active:        item.active
    }))

  // ── Carga inicial ──────────────────────────────────────────
  onMount(async () => {
    try {
      products  = await getAllProductsUseCase.execute()
      tableRows = mapToRows(products)
    } catch (err) {
      error = err instanceof Error ? err.message : 'Error desconocido'
    } finally {
      loading = false
    }
  })
</script>

<div class="layout">
  <section>
    <AppHeader title="Productos" subtitle="Gestión de productos" />

    {#if loading}
      <p>Cargando productos...</p>
    {:else if error}
      <p class="error">{error}</p>
    {:else}
      <DataTable title="Listado de productos" columns={productColumns} rows={tableRows}>

        {#snippet headerActions()}
          <CreateButton onclick={openCreateModal}>Crear producto</CreateButton>
        {/snippet}

        {#snippet rowActions(row)}
          <div class="action-buttons">
            <EditButton   onclick={() => openUpdateModal(row)}>✏️</EditButton>
            <DeleteButton onclick={() => openDeleteModal(row)}>🗑️</DeleteButton>
          </div>
        {/snippet}

      </DataTable>
    {/if}
  </section>
</div>

<!-- ── Modales ──────────────────────────────────────────────── -->
<CreateProductModal
  open={showCreateModal}
  onclose={closeCreateModal}
  oncreated={handleProductCreated}
/>

<UpdateProductModal
  open={showUpdateModal}
  product={selectedProduct}
  onclose={closeUpdateModal}
  onupdated={handleProductUpdated}
/>

<ConfirmModal
  open={showDeleteModal}
  title="Eliminar producto"
  message={`¿Desea eliminar el producto "${selectedProduct?.name ?? ''}"?`}
  oncancel={closeDeleteModal}
  onconfirm={handleDeleteConfirmed}
/>
