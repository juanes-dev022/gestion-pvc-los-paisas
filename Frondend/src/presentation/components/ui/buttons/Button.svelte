<script lang="ts">
  import './button.css'
  import type { Snippet } from 'svelte'

  let {
    variant,
    size = 'md',
    type = 'button',
    disabled = false,
    className = '',
    style = '',
    onclick,
    children
  }: {
    variant?: 'primary' | 'secondary' | 'ghost' | 'success'
    size?: 'md' | 'sm'
    type?: 'button' | 'submit' | 'reset'
    disabled?: boolean
    className?: string
    style?: string
    onclick?: () => void
    children?: Snippet
  } = $props()

  const baseClasses = 'ui-btn'
  const variantClass = variant ? `ui-btn--${variant}` : ''
  const sizeClass = `ui-btn--${size}`
</script>

<button
  {type}
  {disabled}
  {style}
  {onclick}
  class={`${baseClasses} ${variantClass} ${sizeClass} ${className}`.trim()}
>
  {@render children?.()}
</button>