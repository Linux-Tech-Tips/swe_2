import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import security from 'eslint-plugin-security'
import react from 'eslint-plugin-react'
import { defineConfig, globalIgnores } from 'eslint/config'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{js,jsx}'],
    extends: [
      js.configs.recommended,
      reactHooks.configs['recommended-latest'],
      reactRefresh.configs.vite,
    ],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
      parserOptions: {
        ecmaVersion: 'latest',
        ecmaFeatures: { jsx: true },
        sourceType: 'module',
      },
    },
    plugins: {
      security,
      react,
    },
    settings: {
      react: { version: 'detect' }
    },
    rules: {
      'no-unused-vars': ['error', { varsIgnorePattern: '^[A-Z_]' }],

      /* General React rules */
      'react/jsx-no-duplicate-props': 'error',
      'react/self-closing-comp': 'warn',
      'react/no-array-index-key': 'warn',
      'react/jsx-curly-brace-presence': 'warn',

      /* React hooks rules */
      'react-hooks/rules-of-hooks': 'error',
      'react-hooks/exhaustive-deps': 'warn',

      /* security rules with eslint-plugin-security */
      'security/detect-eval-with-expression': 'error',
      'security/detect-unsafe-regex': 'error',
      'security/detect-non-literal-regexp': 'warn',
      'security/detect-object-injection': 'warn',
      'security/detect-child-process': 'error',

      /* React rules with eslint-plugin-react */
      'react/jsx-key': 'error',
      'react/no-unknown-property': 'error',
      'react/no-direct-mutation-state': 'error',
      'react/no-render-return-value': 'error',
      'react/no-children-prop': 'warn',
      'react/jsx-no-comment-textnodes': 'warn',
      'react/no-danger': 'warn',
      'react/no-find-dom-node': 'error',
      'react/jsx-no-script-url': 'error',
    },
  },
])
