import type { SidebarItem } from '$presentation/components/ui/sidebar/Sidebar.svelte'
import { writable } from 'svelte/store'

export const menuItems = writable<SidebarItem[]>([
  { id: 'dashboard', label: 'Dashboard', icon: '📊', active: false },
  { id: 'products', label: 'Productos', icon: '📦', active: false },
  { id: 'sales', label: 'Ventas', icon: '🧾', active: false }
])
