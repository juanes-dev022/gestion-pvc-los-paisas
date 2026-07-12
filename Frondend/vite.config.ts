import { defineConfig } from 'vitest/config'
import { svelte } from '@sveltejs/vite-plugin-svelte'
import path from 'node:path'

export default defineConfig({
  plugins: [svelte()],
  resolve: {
    conditions: ['svelte', 'browser'],
    alias: {
      $application: path.resolve(__dirname, 'src/application'),
      $domain: path.resolve(__dirname, 'src/domain'),
      $infrastructure: path.resolve(__dirname, 'src/infrastructure'),
      $presentation: path.resolve(__dirname, 'src/presentation'),
      $shared: path.resolve(__dirname, 'src/shared')
    }
  },
  test: {
    environment: 'jsdom',
    environmentOptions: {
      customExportConditions: ['svelte', 'browser']
    },
    setupFiles: './vitest.setup.ts',
    globals: true,
    coverage: {
      reporter: ['text', 'html']
    }
  }
})
