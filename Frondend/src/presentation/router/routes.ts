import type { Component } from 'svelte'

export interface Route {
  id: string
  path: string
  label: string
  icon: string
  component: () => Promise<{ default: Component }>
}

export const routes: Route[] = [
  {
    id: 'dashboard',
    path: '#/dashboard',
    label: 'Dashboard',
    icon: '📊',
    component: () => import('$presentation/pages/dashboard/DashboardPage.svelte')
  },
  {
    id: 'products',
    path: '#/products',
    label: 'Productos',
    icon: '📦',
    component: () => import('$presentation/pages/product/ProductListPage.svelte')
  },
  {
    id: 'sales',
    path: '#/sales',
    label: 'Ventas',
    icon: '🧾',
    component: () => import('$presentation/pages/sales/SalesListPage.svelte')
  }
]

export const defaultRoute = routes[0]
