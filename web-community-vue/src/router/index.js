import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import MainView from '@/views/MainView.vue'
import SectionView from '@/views/SectionView.vue'
import PostView from '@/views/PostView.vue'
import UserView from '@/views/UserView.vue'
import PersonalView from '@/views/PersonalView.vue'
import SearchPostView from '@/views/SearchPostView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    redirect: { name: 'login' }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/main',
    name: 'main',
    component: MainView,
  },
  {
    path: '/section',
    name: 'section',
    component: SectionView
  },
  {
    path: '/post',
    name: 'post',
    component: PostView
  },
  {
    path: '/user',
    name: 'user',
    component: UserView
  },
  {
    path: '/personal',
    name: 'personal',
    component: PersonalView
  },
  {
    path: '/searchPost',
    name: 'searchPost',
    component: SearchPostView
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
