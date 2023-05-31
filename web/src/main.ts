import { createApp } from 'vue'
import 'ant-design-vue/dist/antd.css';
import router from './router'
import { createPinia } from 'pinia'
import App from './App.vue'

const app = createApp(App);
const pinia = createPinia();
app.use(pinia).use(router).mount('#app');
