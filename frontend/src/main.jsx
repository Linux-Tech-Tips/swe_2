import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
/**
 * Frontend application entry point.
 *
 * This file mounts the React application into the DOM using React 18's
 * `createRoot` API. The application is wrapped in `<StrictMode>` to help
 * detect unsafe lifecycle patterns and highlight potential issues in
 * development.
 */
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
);
