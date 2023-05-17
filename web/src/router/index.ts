import {createRouter, createWebHistory} from "vue-router"
import HelloWorldVue from '../components/HelloWorld.vue'


const routes = [
    {
        path:'/',
        redirect:'/index',
    },
    {
        path:'/index',
        component: HelloWorldVue,
    }
]
const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
  