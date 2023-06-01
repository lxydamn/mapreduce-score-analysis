import { createApp } from 'vue'
import 'ant-design-vue/dist/antd.css';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import router from './router'
import { createPinia } from 'pinia'
import App from './App.vue'
import './style.css'
const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate)
app.use(pinia).use(router).mount('#app');
