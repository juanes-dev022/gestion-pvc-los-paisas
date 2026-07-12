<script lang="ts">
  import type { Snippet } from 'svelte'
  import Button from '$presentation/components/ui/buttons/Button.svelte'
  import DeleteButton from '$presentation/components/ui/buttons/delete-button.svelte'
  import Modal from './Modal.svelte'

  let {
    open      = false,
    title     = 'Confirmar',
    message   = '¿Desea continuar?',
    oncancel,
    onconfirm
  }: {
    open?:      boolean
    title?:     string
    message?:   string
    oncancel?:  () => void
    onconfirm?: () => void
  } = $props()
</script>

<Modal {open} {title} onclose={oncancel}>
  {#snippet children()}
    <p>{message}</p>
  {/snippet}

  {#snippet actions()}
    <Button variant="ghost" type="button" onclick={oncancel}>Cancelar</Button>
    <DeleteButton type="button" onclick={onconfirm}>Eliminar</DeleteButton>
  {/snippet}
</Modal>
