/*
 * @Author: yuchenzm Wcm136677@163.com
 * @Date: 2025-07-02 20:27:11
 * @LastEditors: yuchenzm Wcm136677@163.com
 * @LastEditTime: 2025-07-07 19:10:23
 * @FilePath: /wuliu guanli/logistics-system/client-web/vite.config.ts
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx()
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  server: {
    port: 3001,
    strictPort: true, // Exit if port is already in use
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        configure: (proxy, options) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            console.log('Proxying request:', req.method, req.url);
          });
          proxy.on('error', (err, req, res) => {
            console.error('Proxy error:', err);
          });
        },
      }
    }
  },
  define: {
    global: 'globalThis',
  },
  optimizeDeps: {
    include: ['vue', 'vue-router', 'element-plus']
  }
})
