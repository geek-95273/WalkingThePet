<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getOrdersApi } from '../api/order';
import { getPetsApi } from '../api/pet';
import { getMySitterInfoApi } from '../api/sitter';
import { user, logout, isLoggedIn } from '../data/user';

const router = useRouter();

const ordersCount = ref(0);
const petsCount = ref(0);
const sitterInfo = ref(null);

const loadData = async () => {
  if (!isLoggedIn()) return;
  
  try {
    const ordersRes = await getOrdersApi();
    if (ordersRes.success && ordersRes.body) {
      ordersCount.value = ordersRes.body.length;
    }
    
    const petsRes = await getPetsApi();
    if (petsRes.success && petsRes.body) {
      petsCount.value = petsRes.body.length;
    }
    
    // 加载宠托师信息
    try {
      const sitterRes = await getMySitterInfoApi();
      if (sitterRes.success && sitterRes.body) {
        sitterInfo.value = sitterRes.body;
      }
    } catch (error) {
      // 未入驻是正常情况，不需要报错
      console.log('未入驻宠托师');
    }
  } catch (error) {
    console.error('加载数据失败:', error);
  }
};

onMounted(() => {
  loadData();
});

const goOrders = () => router.push({ name: 'Orders' });
const goPets = () => router.push({ name: 'PetArchive' });
const goJoin = () => router.push({ name: 'SitterJoin' });

const handleAuthAction = () => {
  if (isLoggedIn()) {
    logout();
    alert('已退出登录');
  }
  router.push({ name: 'Login' });
};
</script>

<template>
  <section class="profile">
    <header class="page__header">
      <div>
        <p class="eyebrow">个人中心</p>
        <h1>{{ isLoggedIn() ? `${user.username}，欢迎回来` : '欢迎回来' }}</h1>
        <p class="subtitle">在这里查看订单、管理宠物档案。</p>
      </div>
    </header>

    <div class="grid">
      <article class="card" role="button" tabindex="0" @click="goOrders" @keyup.enter="goOrders">
        <div class="card__header">
          <h3>我的订单</h3>
          <span class="pill">共 {{ ordersCount }} 条</span>
        </div>
        <p class="desc">点击查看所有订单状态与记录。</p>
      </article>

      <article class="card" role="button" tabindex="0" @click="goPets" @keyup.enter="goPets">
        <div class="card__header">
          <h3>宠物档案</h3>
          <span class="pill">共 {{ petsCount }} 只</span>
        </div>
        <p class="desc">管理你的宠物信息、照片与特殊说明。</p>
      </article>

      <article class="card" role="button" tabindex="0" @click="goJoin" @keyup.enter="goJoin">
        <div class="card__header">
          <h3>{{ sitterInfo ? '宠托师信息' : '入驻宠托师' }}</h3>
          <span v-if="sitterInfo" class="pill success">✓ 已入驻</span>
          <span v-else class="pill">去入驻</span>
        </div>
        <p v-if="sitterInfo" class="desc">
          {{ sitterInfo.name }} · {{ sitterInfo.gender }}<br>
          {{ sitterInfo.slogan || '可信赖的本地宠托师' }}
        </p>
        <p v-else class="desc">完善个人介绍、服务与宠物展示，入驻后可接单。</p>
      </article>
    </div>

    <div class="auth-action">
      <button type="button" class="auth-btn" @click="handleAuthAction">
        {{ isLoggedIn() ? '退出登录' : '立即登录' }}
      </button>
    </div>
  </section>
</template>

<style scoped>
.profile {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page__header {
  background: rgba(255, 214, 102, 0.14);
  border: 1px solid rgba(255, 214, 102, 0.32);
  border-radius: 16px;
  padding: 14px 16px;
  box-shadow: 0 10px 22px rgba(88, 70, 0, 0.06);
}

.eyebrow {
  margin: 0;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
  color: #b17a00;
  text-transform: uppercase;
}

h1 {
  margin: 2px 0 4px;
  color: #2e2200;
}

.subtitle {
  margin: 0;
  color: rgba(46, 34, 0, 0.68);
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 12px;
}

.card {
  background: #fffaf0;
  border-radius: 16px;
  padding: 14px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: var(--card-shadow);
  display: flex;
  flex-direction: column;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.12s ease, box-shadow 0.12s ease, border-color 0.12s ease;
}

.card:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 28px rgba(88, 70, 0, 0.12);
  border-color: rgba(255, 214, 102, 0.55);
}

.card__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
}

h3 {
  margin: 0;
  color: #2e2200;
}

.pill {
  padding: 8px 10px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.2);
  border: 1px solid rgba(255, 214, 102, 0.36);
  font-weight: 700;
  color: #4a3900;
}

.pill.success {
  background: rgba(76, 175, 80, 0.2);
  color: #2d5f2e;
  border: 1px solid rgba(76, 175, 80, 0.4);
}

.desc {
  margin: 0;
  color: rgba(46, 34, 0, 0.7);
}

.auth-action {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.auth-btn {
  padding: 12px 32px;
  border-radius: 12px;
  font-weight: 700;
  font-size: 15px;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: linear-gradient(145deg, #ffc857, #ffb347);
  color: #2e2200;
  box-shadow: 0 8px 16px rgba(255, 200, 87, 0.28);
  transition: transform 0.12s ease, box-shadow 0.15s ease;
}

.auth-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(255, 200, 87, 0.35);
}

.auth-btn:active {
  transform: translateY(0);
}
</style>
