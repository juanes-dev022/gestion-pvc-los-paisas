<script lang="ts">
  import type { UpdateProductDto } from '$application/dtos/Product-dto'
  import { UpdateProductUseCase } from '$application/usecases/productusecase/update-product.usecase'
  import type { Product } from '$domain/Products/Product'
  import { HttpProductGateway } from '$infrastructure/api/product/http-product-gateway'
  import Button from '$presentation/components/ui/buttons/Button.svelte'
  import EditButton from '$presentation/components/ui/buttons/edit-button.svelte'
  import Modal from '$presentation/components/ui/modals/Modal.svelte'
  import './create-product-modal.css'

  let {
    open = false,
    product = null,
    onclose,
    onupdated
  }: {
    open?:      boolean
    product?:   Product | null
    onclose?:   () => void
    onupdated?: (product: Product) => void
  } = $props()

  const gateway     = new HttpProductGateway()
  const updateUseCase = new UpdateProductUseCase(gateway)

  type FormState = {
    id:            string | null
    name:          string
    purchaseValue: number
    stock:         number
    active:        'true' | 'false'
  }

  let submitting = $state(false)
  let error: string | null = $state(null)
  let form: FormState = $state({
    id: null, name: '', purchaseValue: 0, stock: 0, active: 'true'
  })

  const formId = 'update-product-form'

  const reset = () => {
    form  = { id: null, name: '', purchaseValue: 0, stock: 0, active: 'true' }
    error = null
  }

  const handleClose = () => {
    reset()
    onclose?.()
  }

  $effect(() => {
    if (open && product) {
      form = {
        id:            product.id,
        name:          product.name,
        purchaseValue: product.purchaseValue,
        stock:         product.stock,
        active:        product.active ? 'true' : 'false'
      }
      error = null
    }
  })

  const handleSubmit = async () => {
    if (!form.id) return
    submitting = true
    error      = null
    try {
      const payload: UpdateProductDto = {
        id:            form.id,
        name:          form.name.trim(),
        purchaseValue: form.purchaseValue,
        stock:         form.stock,
        active:        form.active === 'true'
      }
      const updated = await updateUseCase.execute(payload)
      onupdated?.(updated)
      handleClose()
    } catch (err) {
      error = err instanceof Error ? err.message : 'Ocurrió un error'
    } finally {
      submitting = false
    }
  }
</script>

<Modal {open} title="Editar producto" onclose={handleClose}>

  {#snippet children()}
    <form id={formId} class="create-product-form" onsubmit={(e) => { e.preventDefault(); handleSubmit() }}>
      <div class="form-row">
        <label>
          Nombre
          <input name="name" bind:value={form.name} placeholder="nombre" autocomplete="off" required />
        </label>
      </div>

      <div class="form-row form-row--two">
        <label>
          Valor compra
          <input name="purchaseValue" type="number" min="0" step="0.01" bind:value={form.purchaseValue} required />
        </label>
      </div>

      <div class="form-row form-row--two">
        <label>
          Stock
          <input name="stock" type="number" min="0" bind:value={form.stock} required />
        </label>
        <label>
          Estado
          <select name="active" bind:value={form.active}>
            <option value="true">Activo</option>
            <option value="false">Inactivo</option>
          </select>
        </label>
      </div>

      {#if error}
        <p class="error-text">{error}</p>
      {/if}
    </form>
  {/snippet}

  {#snippet actions()}
    <Button variant="ghost" type="button" onclick={handleClose}>Cancelar</Button>
    <EditButton type="button" disabled={submitting} onclick={handleSubmit}>
      {submitting ? 'Guardando...' : 'Actualizar'}
    </EditButton>
  {/snippet}

</Modal>
