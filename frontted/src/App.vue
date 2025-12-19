<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router';

const route = useRoute();
</script>

<template>
  <div class="app-shell">
    <header class="topbar">
      <div class="brand">
        <span class="brand__mark">WalkMe</span>
        <span class="brand__name">同城溜宠</span>
      </div>
      <nav class="topbar__nav">
        <RouterLink class="nav__link" :class="{ active: route.name === 'Home' }" :to="{ name: 'Home' }">
          首页
        </RouterLink>
        <RouterLink class="nav__link" :class="{ active: route.name === 'Sitters' || route.name === 'SitterDetail' }" :to="{ name: 'Sitters' }">
          宠托师
        </RouterLink>
        <RouterLink
          class="nav__link"
          :class="{ active: route.name === 'Bulletins' || route.name === 'BulletinDetail' }"
          :to="{ name: 'Bulletins' }"
        >
          喂溜公告
        </RouterLink>
      </nav>
      <RouterLink class="topbar__profile" :class="{ active: route.name === 'Profile' }" :to="{ name: 'Profile' }">
        <span class="profile__name">个人中心</span>
      </RouterLink>
    </header>

    <main class="page">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  background: linear-gradient(135deg, #fff8e1 0%, #fffdf5 60%, #fff7d6 100%);
  color: #302b1b;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 10;
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 16px;
  padding: 16px 32px;
  background: rgba(255, 248, 225, 0.9);
  backdrop-filter: blur(6px);
  border-bottom: 1px solid rgba(255, 214, 102, 0.4);
  box-shadow: 0 8px 24px rgba(255, 198, 63, 0.12);
}

.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
  letter-spacing: 0.02em;
}

.brand__mark {
  padding: 8px 10px;
  border-radius: 12px;
  background: linear-gradient(145deg, #ffc857, #ffdf85);
  color: #5c4300;
  font-size: 14px;
}

.brand__name {
  color: #4a3900;
}

.topbar__nav {
  display: flex;
  align-items: center;
  gap: 12px;
}

.nav__link {
  padding: 10px 14px;
  border-radius: 12px;
  color: #7a5d00;
  text-decoration: none;
  transition: all 0.2s ease;
}

.nav__link:hover {
  background: rgba(255, 214, 102, 0.2);
  color: #5b4000;
}

.nav__link.active {
  background: #ffd766;
  color: #3a2c00;
  box-shadow: 0 8px 16px rgba(255, 214, 102, 0.32);
}

.topbar__profile {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(255, 214, 102, 0.2);
  border-radius: 12px;
  color: #4a3900;
  border: 1px solid rgba(255, 214, 102, 0.4);
  text-decoration: none;
}

.topbar__profile.active {
  background: #ffd766;
  box-shadow: 0 8px 16px rgba(255, 214, 102, 0.32);
}

.profile__name {
  font-weight: 600;
}

.page {
  max-width: 1280px;
  margin: 0 auto;
  padding: 24px 28px 80px;
}

@media (max-width: 1080px) {
  .topbar {
    grid-template-columns: auto auto;
    grid-template-rows: auto auto;
    align-items: start;
  }
}
</style>
