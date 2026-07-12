<script lang="ts">
  import Button from './Button.svelte'
  import './edit-button.css'
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

  const blueStyle =
    'background: linear-gradient(135deg,#4da3ff,#1f6feb);color:#eaf3ff;box-shadow:0 12px 24px rgba(31,110,235,0.25);'
</script>

<Button
  className="edit-btn"
  variant="primary"
  size="sm"
  {type}
  {disabled}
  {onclick}
  style={blueStyle}
>
  {@render children?.()}
</Button>