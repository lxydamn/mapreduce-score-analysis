import {createRouter, createWebHistory} from 'vue-router'


const routes = [
    {
        path:'/',
        redirect:'/index'
    },
    {
        path: '/home',
        name: 'home',
        component: () => import(/* webpackChunkName: "home" */ '../views/home.vue'),
        children: [
            {
                path: '/index',
                name: 'index',
                component: () => import(/* webpackChunkName: "index" */ '../views/hello.vue'),
            },
        ]
    },
    {
        path: '/login',
        component: () => import(/* webpackChunkName: "index" */ '../views/login.vue')
    },
    {
        path: '/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "index" */ '../views/register.vue'),
    }

];

const router = createRouter({
    history : createWebHistory(),
    routes,
})

export default router