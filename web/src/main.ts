import { createApp } from 'vue'
import 'ant-design-vue/dist/antd.css';
import router from './router'
import App from './App.vue'
import './style.css'
const app = createApp(App);
app.use(router).mount('#app');
