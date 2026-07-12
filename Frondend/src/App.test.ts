import { render, screen } from '@testing-library/svelte'
import { afterAll, beforeAll, expect, test, vi } from 'vitest'
import App from './App.svelte'

const fetchMock = vi.fn()

beforeAll(() => {
  vi.stubGlobal(
    'fetch',
    fetchMock.mockResolvedValue({
      ok: true,
      json: () => Promise.resolve([])
    }) as unknown as typeof fetch
  )
})

afterAll(() => {
  vi.unstubAllGlobals()
})

test('muestra el componente principal', async () => {
  render(App)
  expect(await screen.findByRole('heading', { name: /productos/i })).toBeInTheDocument()
})
