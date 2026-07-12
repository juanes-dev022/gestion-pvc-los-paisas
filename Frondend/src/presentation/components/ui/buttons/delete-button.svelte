<script lang="ts">
  import Button from './Button.svelte'
  import type { Snippet } from 'svelte'

  let {
    disabled = false,
    type = 'button',
    onclick,
    children
  }: {
    disabled?: boolean
    type?: 'button' | 'submit' | 'reset'
    onclick?: () => void
    children?: Snippet
  } = $props()

  const redStyle =
    'background: linear-gradient(135deg,#ff5f6d,#c81d25);color:#fff7f7;box-shadow:0 12px 24px rgba(200,29,37,0.25);'
</script>

<Button
  className="create-btn"
  size="md"
  {type}
  {disabled}
  {onclick}
  style={redStyle}
>
  {@render children?.()}
</Button>