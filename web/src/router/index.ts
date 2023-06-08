import {createRouter, createWebHistory} from 'vue-router'


const routes = [
    {
        path:'/',
        redirect:'/index'
    },
    {
        path:'/index',
        name:'index',
        component:() => import(/* webpackChunkName: "index" */ '../views/index.vue'),
    }

];

const router = createRouter({
    history : createWebHistory(),
    routes,
})

export default router