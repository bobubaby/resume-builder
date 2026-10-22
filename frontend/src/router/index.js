import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue'), meta: { public: true } },
  { path: '/', component: () => import('../views/ResumeList.vue') },
  { path: '/editor/:id', component: () => import('../views/Editor.vue') },
  { path: '/preview/:id', component: () => import('../views/Preview.vue') }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to) => {
  if (!to.meta.public && !localStorage.getItem('token')) {
    return '/login'
  }
  if (to.path === '/login' && localStorage.getItem('token')) {
    return '/'
  }
})

export default router
