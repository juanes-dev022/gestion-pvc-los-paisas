<script lang="ts">
  import type { CreateProductDto } from '$application/dtos/product.dto'
  import { CreateProductUseCase } from '$application/usecases/product/create-product.usecase'
  import type { Product } from '$domain/product/Product'
  import { HttpProductGateway } from '$infrastructure/api/product/http-product-gateway'
  import Button from '$presentation/components/ui/buttons/Button.svelte'
  import Modal from '$presentation/components/ui/modals/Modal.svelte'
  import './create-product-modal.css'

  let {
    open = false,
    onclose,
    oncreated
  }: {
    open?:      boolean
    onclose?:   () => void
    oncreated?: (product: Product) => void
  } = $props()

  const gateway     = new HttpProductGateway()
  const createUseCase = new CreateProductUseCase(gateway)

  type FormState = {
    name:          string
    purchaseValue: number
    stock:         number
    active:        'true' | 'false'
  }

  let submitting = $state(false)
  let error: string | null = $state(null)
  let form: FormState = $state({
    name:          '',
    purchaseValue: 0,
    stock:         0,
    active:        'true'
  })

  const formId = 'create-product-form'

  const reset = () => {
    form  = { name: '', purchaseValue: 0, stock: 0, active: 'true' }
    error = null
  }

  const handleClose = () => {
    reset()
    onclose?.()
  }

  const handleSubmit = async () => {
    submitting = true
    error      = null
    try {
      const payload: CreateProductDto = {
        name:          form.name.trim(),
        purchaseValue: form.purchaseValue,
        stock:         form.stock,
        active:        form.active === 'true'
      }
      const created = await createUseCase.execute(payload)
      oncreated?.(created)
      handleClose()
    } catch (err) {
      error = err instanceof Error ? err.message : 'Ocurrió un error'
    } finally {
      submitting = false
    }
  }
</script>

<Modal {open} title="Crear producto" onclose={handleClose}>

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
    <Button type="button" variant="success" disabled={submitting} onclick={handleSubmit}>
      {submitting ? 'Guardando...' : 'Guardar'}
    </Button>
  {/snippet}

</Modal>
