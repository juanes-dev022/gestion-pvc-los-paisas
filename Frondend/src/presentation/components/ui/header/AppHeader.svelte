<script lang="ts">
  import './app-header.css'

  export let title = ''
  export let subtitle = ''
</script>

<header class="app-header">
  <div class="app-header__brand">
    <slot name="logo">
      <div class="app-header__logo">◎</div>
    </slot>
    <div>
      <h1>{title}</h1>
      {#if subtitle}<p>{subtitle}</p>{/if}
    </div>
  </div>

  <nav class="app-header__actions">
    <slot name="actions" />
  </nav>
</header>
