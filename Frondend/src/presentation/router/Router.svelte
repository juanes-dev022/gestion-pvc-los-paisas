<script lang="ts">
  import { onDestroy, onMount } from 'svelte'
  import type { Component } from 'svelte'
  import { router } from '../stores/store-navigation'
  import { defaultRoute } from './routes'

  let PageComponent: Component | null = null
  let loading = true

  async function loadPage() {
    loading = true
    try {
      const mod = await $router.component()
      PageComponent = mod.default
    } finally {
      loading = false
    }
  }

  // Sincroniza con cambios del hash (back/forward del browser)
  function onHashChange() {
    router.syncFromHash()
  }

  onMount(() => {
    // Redirige a la ruta por defecto si el hash está vacío
    if (!window.location.hash) {
      window.location.hash = defaultRoute.path
    }
    window.addEventListener('hashchange', onHashChange)
    loadPage()
  })

  onDestroy(() => {
    window.removeEventListener('hashchange', onHashChange)
  })

  $: ($router, loadPage())
</script>

{#if loading}
  <p style="padding:2rem">Cargando...</p>
{:else if PageComponent}
  <svelte:component this={PageComponent} />
{:else}
  <p style="padding:2rem">Página no encontrada</p>
{/if}
