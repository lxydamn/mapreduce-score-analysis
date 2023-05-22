import { defineConfig,loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
//自动按需引入 ant design vue
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers'
import Components from 'unplugin-vue-components/vite'

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  return {
      build: {
        assetsDir: "static", // 静态资源导出的文件名
      },
      plugins: [
        Components({
          resolvers: [AntDesignVueResolver()]
        }),
        vue(),
      ],
   }
});
