<script lang="ts" module>
  export type Column = {
    key: string
    header: string
    format?: (value: unknown, row: Record<string, unknown>) => string
  }
</script>

<script lang="ts">
  import type { Snippet } from 'svelte'
  import './data-table.css'

  let {
    title        = '',
    columns      = [],
    rows         = [],
    emptyMessage = 'Sin registros',
    headerActions,
    toolbar,
    actionsHeader,
    rowActions,
    pagination,
    footerInfo
  }: {
    title?:         string
    columns?:       Column[]
    rows?:          Record<string, unknown>[]
    emptyMessage?:  string
    headerActions?: Snippet
    toolbar?:       Snippet
    actionsHeader?: Snippet
    rowActions?:    Snippet<[Record<string, unknown>]>
    pagination?:    Snippet
    footerInfo?:    Snippet
  } = $props()
</script>

<section class="table-card">
  {#if title}
    <header class="table-header">
      <h2>{title}</h2>
      {#if headerActions}
        {@render headerActions()}
      {/if}
    </header>
  {/if}

  <div class="table-toolbar">
    {#if toolbar}
      {@render toolbar()}
    {/if}
  </div>

  <div class="table-wrapper">
    <table>
      <thead>
        <tr>
          {#each columns as column (column.key)}
            <th>{column.header}</th>
          {/each}
          <th class="actions">
            {#if actionsHeader}
              {@render actionsHeader()}
            {/if}
          </th>
        </tr>
      </thead>
      <tbody>
        {#if rows.length === 0}
          <tr>
            <td class="empty" colspan={columns.length + 1}>{emptyMessage}</td>
          </tr>
        {:else}
          {#each rows as row, rowIndex (row.id ?? row.key ?? rowIndex)}
            <tr>
              {#each columns as column (column.key)}
                <td>
                  {#if column.format}
                    {column.format(row[column.key], row)}
                  {:else}
                    {row[column.key]}
                  {/if}
                </td>
              {/each}
              <td class="actions">
                {#if rowActions}
                  {@render rowActions(row)}
                {/if}
              </td>
            </tr>
          {/each}
        {/if}
      </tbody>
    </table>
  </div>

  <footer class="table-footer">
    <div>
      {#if pagination}
        {@render pagination()}
      {/if}
    </div>
    <div>
      {#if footerInfo}
        {@render footerInfo()}
      {/if}
    </div>
  </footer>
</section>