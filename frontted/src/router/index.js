import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import BulletinList from '../views/BulletinList.vue';
import BulletinDetail from '../views/BulletinDetail.vue';
import SitterList from '../views/SitterList.vue';
import SitterDetail from '../views/SitterDetail.vue';
import Profile from '../views/Profile.vue';
import Orders from '../views/Orders.vue';
import PetArchive from '../views/PetArchive.vue';
import PetDetail from '../views/PetDetail.vue';
import PetForm from '../views/PetForm.vue';
import SitterJoin from '../views/SitterJoin.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/bulletins',
      name: 'Bulletins',
      component: BulletinList
    },
    {
      path: '/bulletins/:id',
      name: 'BulletinDetail',
      component: BulletinDetail,
      props: true
    },
    {
      path: '/sitters',
      name: 'Sitters',
      component: SitterList
    },
    {
      path: '/sitters/:id',
      name: 'SitterDetail',
      component: SitterDetail,
      props: true
    },
    {
      path: '/profile',
      name: 'Profile',
      component: Profile
    },
    {
      path: '/profile/sitter/join',
      name: 'SitterJoin',
      component: SitterJoin
    },
    {
      path: '/orders',
      name: 'Orders',
      component: Orders
    },
    {
      path: '/profile/pets',
      name: 'PetArchive',
      component: PetArchive
    },
    {
      path: '/profile/pets/new',
      name: 'PetCreate',
      component: PetForm
    },
    {
      path: '/profile/pets/:id',
      name: 'PetDetail',
      component: PetDetail,
      props: true
    },
    {
      path: '/profile/pets/:id/edit',
      name: 'PetEdit',
      component: PetForm,
      props: true
    }
  ],
  scrollBehavior() {
    return { top: 0 };
  }
});

export default router;
