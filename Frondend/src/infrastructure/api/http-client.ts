const API_BASE_URL: string = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:7070'

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response: Response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...init?.headers
    },
    ...init
  })

  if (!response.ok) {
    const message: string = await response.text()
    throw new Error(message || `HTTP ${response.status}`)
  }

  const hasBody: boolean =
    response.status !== 204 &&
    response.status !== 205 &&
    response.headers.get('content-length') !== '0'

  if (!hasBody) return undefined as T

  const contentType: string = response.headers.get('content-type') ?? ''
  if (!contentType.includes('application/json')) return undefined as T

  return (await response.json()) as T
}

export const httpClient = {
  get: <T>(path: string) => request<T>(path),
  post: <TRequest, TResponse>(path: string, body: TRequest) =>
    request<TResponse>(path, { method: 'POST', body: JSON.stringify(body) }),
  put: <TRequest, TResponse>(path: string, body: TRequest) =>
    request<TResponse>(path, { method: 'PUT', body: JSON.stringify(body) }),
  delete: <T>(path: string) => request<T>(path, { method: 'DELETE' })
}
