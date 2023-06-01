import {createRouter, createWebHistory} from 'vue-router'


const routes = [
    {
        path:'/',
        redirect:'/index'
    },
    {
        path: '/home',
        name: 'home',
        meta: {
            author:true
        },
        component: () => import(/* webpackChunkName: "home" */ '../views/home.vue'),
        children: [
            {
                path: '/index',
                name: 'index',
                meta: {
                    author:true
                },
                component: () => import(/* webpackChunkName: "index" */ '../views/hello.vue'),
            },
        ]
    },
    {
        path: '/login',
        name:'login',
        meta: {
            author:false
        },
        component: () => import(/* webpackChunkName: "index" */ '../views/login.vue')
    },
    {
        path: '/register',
        name: 'register',
        meta: {
            author:false
        },
        component: () => import(/* webpackChunkName: "index" */ '../views/register.vue'),
    }

];

const router = createRouter({
    history : createWebHistory(),
    routes,
})

router.beforeEach((to, _from, next) => {
    if (to.meta.author && !sessionStorage.getItem("is_login")) {
        next({name:'login'})
    } else {
        next();
    }
})
export default router