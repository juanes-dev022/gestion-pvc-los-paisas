<script lang="ts" context="module">
  export type SidebarItem = {
    id: string | number
    label: string
    icon?: string
    active?: boolean
  }
</script>

<script lang="ts">
  import { router, activeRouteId } from '$presentation/stores/navigation.store'
  import { routes } from '$presentation/router/routes'
  import type { Snippet } from 'svelte'
  import './sidebar.css'

  
  let{
    title = '',
    items = [] as SidebarItem[],
    onSelect,
    footer,
    header
  } :{
    title?: string
    items?: SidebarItem[]
    onSelect: (item: SidebarItem) => void
    header?: Snippet         
    footer?: Snippet
  } = $props()

  function handleNavigate(item: SidebarItem) {
    const route = routes.find((r) => r.id === item.id)
    if (route) router.navigate(route)
    onSelect?.(item)
  }
</script>

<aside class="sidebar">
  {#if title}
    <div class="sidebar__header">
      <slot name="header">
        <h2>{title}</h2>
      </slot>
    </div>
  {/if}

  <nav class="sidebar__nav">
    {#each items as item (item.id)}
      <button
        type="button"
        class="sidebar__item"
        class:active={$activeRouteId === item.id}
        on:click={() => handleNavigate(item)}
      >
        {#if item.icon}
          <span class="sidebar__icon" aria-hidden="true">{item.icon}</span>
        {/if}
        <span class="sidebar__label">{item.label}</span>
      </button>
    {/each}
  </nav>

  <div class="sidebar__footer">
    <slot name="footer" />
  </div>
</aside>
