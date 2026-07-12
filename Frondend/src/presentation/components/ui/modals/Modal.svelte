<script lang="ts">
  import './modal.css'
  import { onDestroy, onMount } from 'svelte'
  import type { Snippet } from 'svelte'

  let {
    open = $bindable(false),
    title = '',
    onclose,
    children,
    actions
  }: {
    open?:     boolean
    title?:    string
    onclose?:  () => void
    children?: Snippet
    actions?:  Snippet
  } = $props()

  const close = () => {
    if (!open) return
    open = false
    onclose?.()
  }

  $effect(() => {
    if (typeof document !== 'undefined') {
      document.body.classList.toggle('modal-open', open)
    }
  })

  onMount(() => {
    if (typeof window === 'undefined') return
    const keyListener = (event: KeyboardEvent) => {
      if (event.key === 'Escape') close()
    }
    window.addEventListener('keydown', keyListener)
    return () => window.removeEventListener('keydown', keyListener)
  })

  onDestroy(() => {
    if (typeof document !== 'undefined') {
      document.body.classList.remove('modal-open')
    }
  })
</script>

{#if open}
  <!-- svelte-ignore a11y_click_events_have_key_events -->
  <!-- svelte-ignore a11y_no_static_element_interactions -->
  <div class="modal-backdrop" role="presentation" onclick={close}></div>
  <!-- svelte-ignore a11y_click_events_have_key_events -->
  <!-- svelte-ignore a11y_no_static_element_interactions -->
  <div class="modal-container" role="dialog" aria-modal="true" onclick={(e) => e.stopPropagation()}>
    <header class="modal-header">
      <h2>{title}</h2>
      <button type="button" class="modal-close" onclick={close} aria-label="Cerrar">×</button>
    </header>
    <section class="modal-body">
      {@render children?.()}
    </section>
    <footer class="modal-footer">
      {@render actions?.()}
    </footer>
  </div>
{/if}
