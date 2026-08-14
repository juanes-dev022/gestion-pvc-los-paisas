<script lang="ts">
  import Button from './Button.svelte'

  let {
    saving = false,
    disabled = false,
    onclick
  }: {
    saving?: boolean
    disabled?: boolean
    onclick?: () => void
  } = $props()

  const saveStyle =
    'background: linear-gradient(135deg,#1ddf72,#0fb863);color:#041206;font-weight:700;box-shadow:0 8px 20px rgba(15,184,99,0.3);margin-top:1rem;padding:0.65rem 1.5rem;'
</script>

<Button
  {onclick}
  disabled={disabled || saving}
  style={saveStyle}
  className="btn-save"
>
  {#if saving}
    <span class="spinner">⏳</span> Guardando...
  {:else}
    💾 Guardar Venta
  {/if}
</Button>