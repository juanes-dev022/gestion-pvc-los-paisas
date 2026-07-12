import svelteParser from 'svelte-eslint-parser';
import svelte from 'eslint-plugin-svelte';
import tseslint from 'typescript-eslint';
import js from '@eslint/js';

export default [
  {
    ignores: ['node_modules', 'dist', '.svelte-kit']
  },
  js.configs.recommended,
  ...tseslint.configs.recommended,
  ...svelte.configs['flat/recommended'],
  {
    files: ['**/*.svelte'],
    languageOptions: {
      parser: svelteParser,
      parserOptions: {
        parser: tseslint.parser,
        project: './tsconfig.app.json',
        extraFileExtensions: ['.svelte']
      },
      globals: {
        window: 'readonly',
        document: 'readonly'
    }
    }
  },
  {
    files: ['**/*.{ts,tsx,js,jsx,mjs,cjs}'],
    languageOptions: {
      parser: tseslint.parser,
      parserOptions: {
        project: './tsconfig.app.json'
      }
    }
  },
  {
    rules: {
      '@typescript-eslint/explicit-module-boundary-types': 'off',
      '@typescript-eslint/no-unused-vars': [
        'warn',
        { argsIgnorePattern: '^_', varsIgnorePattern: '^_' }
      ],
      'svelte/no-at-html-tags': 'error'
    }
  }
];