import { derived, writable } from 'svelte/store'
import { defaultRoute, routes, type Route } from '../router/routes'

function getRouteFromHash(): Route {
  const hash = window.location.hash
  return routes.find((r) => r.path === hash) ?? defaultRoute
}

function createRouter() {
  const { subscribe, set } = writable<Route>(getRouteFromHash())

  function navigate(route: Route): void {
    window.location.hash = route.path
    set(route)
  }

  function syncFromHash(): void {
    set(getRouteFromHash())
  }

  return { subscribe, navigate, syncFromHash }
}

export const router = createRouter()

export const activeRouteId = derived(router, ($route) => $route.id)
