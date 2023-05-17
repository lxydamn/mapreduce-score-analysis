import { createApp } from 'vue'
import 'bootstrap/dist/css/bootstrap.css' //引用bootstrap的样式
import 'bootstrap/dist/js/bootstrap.min.js' //引用bootstrap的js
import router from './router'

import App from './App.vue'

createApp(App).use(router).mount('#app')
