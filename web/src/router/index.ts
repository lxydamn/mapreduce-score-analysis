import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'login',
    // 按需加载
    component: () => import('../views/Home.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
