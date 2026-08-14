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

  const greenStyle =
    'background: linear-gradient(135deg,#1ddf72,#0fb863);color:#041206;box-shadow:0 12px 24px rgba(15,184,99,0.25);'
</script>

<Button
  className="create-btn"
  size="md"
  {type}
  {disabled}
  {onclick}
  style={greenStyle}
>
  {@render children?.()}
</Button>