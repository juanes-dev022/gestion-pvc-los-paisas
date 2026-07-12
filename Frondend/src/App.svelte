<script lang="ts">
  import Sidebar from '$presentation/components/ui/sidebar/Sidebar.svelte'
  import ProductListPage from '$presentation/pages/product/ProductListPage.svelte'
  import SalesListPage from '$presentation/pages/sales/SalesListPage.svelte'
  import Router from '$presentation/router/Router.svelte'
  import {routes} from '$presentation/router/routes'


  const menuItems = routes.map((r) => ({
    id: r.id,
    label: r.label,
    icon: r.icon
  }))
</script>

<main class="app-shell">
  <Sidebar title="Menú" items={menuItems} />
  <div class="page-content">
    <Router />
  </div>
</main>
