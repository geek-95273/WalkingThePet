<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getBulletinDetailApi, acceptBulletinApi } from '../api/bulletin';
import { user } from '../data/user';

const route = useRoute();
const router = useRouter();
const bulletin = ref(null);
const loading = ref(true);

const serviceLabel = (type) => (type === 'feed-cat' ? '上门喂猫' : '上门遛狗');

const goBack = () => {
  router.push({ name: 'Bulletins' });
};

const loadBulletinDetail = async () => {
  try {
    loading.value = true;
    const response = await getBulletinDetailApi(route.params.id);
    if (response && response.body) {
      bulletin.value = response.body;
    }
  } catch (error) {
    console.error('加载公告详情失败:', error);
    alert('加载失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};

const handleAccept = async () => {
  if (!bulletin.value) return;
  
  try {
    // 先调用后端接口获取宠托师信息
    const { getMySitterInfoApi } = await import('../api/sitter');
    const sitterRes = await getMySitterInfoApi();
    
    if (!sitterRes.success || !sitterRes.body) {
      alert('请先入驻宠托师后再接单');
      router.push({ name: 'SitterJoin' });
      return;
    }
    
    const mySitter = sitterRes.body;
    
    // 接受公告（会自动更新对应的订单状态和宠托师信息）
    await acceptBulletinApi(bulletin.value.id, {
      sitterId: mySitter.sitterId // bulletin表使用sitter表的sitterId
    });
    
    alert('已接单，订单已更新，可在个人中心查看');
    router.push({ name: 'Profile' });
  } catch (error) {
    console.error('接单失败:', error);
    // 如果是404错误，说明未入驻
    if (error.message && error.message.includes('404')) {
      alert('请先入驻宠托师后再接单');
      router.push({ name: 'SitterJoin' });
    } else {
      alert(error.message || '接单失败，请稍后重试');
    }
  }
};

onMounted(() => {
  loadBulletinDetail();
});
</script>

<template>
  <section v-if="bulletin" class="detail">
    <button class="back" type="button" @click="goBack">← 返回公告列表</button>
    <div class="detail__hero">
      <div class="badge">{{ serviceLabel(bulletin.service_type) }}</div>
      <div class="distance">{{ bulletin.distance }}</div>
      <h1>{{ bulletin.title }}</h1>
      <p class="sub">创建时间：{{ bulletin.created_at }}</p>
    </div>

    <div class="card">
      <div class="row">
        <div>
          <span class="label">上门地址</span>
          <p class="value">{{ bulletin.address }}</p>
        </div>
        <div>
          <span class="label">服务时间</span>
          <p class="value">{{ bulletin.service_time }}</p>
        </div>
      </div>
      <div class="row">
        <div>
          <span class="label">宠物</span>
          <p class="value">{{ bulletin.pet_name || '未填写' }} · {{ bulletin.pet_type || '未填写' }}</p>
        </div>
        <div>
          <span class="label">宠托师性别</span>
          <p class="value">{{ bulletin.walker_gender }}</p>
        </div>
      </div>
      <div class="row">
        <div>
          <span class="label">备注</span>
          <p class="value">{{ bulletin.remark || '无' }}</p>
        </div>
      </div>
      <div class="status">
        <span class="pill">状态：{{ bulletin.status }}</span>
      </div>

      <div class="cta">
        <button type="button" class="cta__primary" @click="handleAccept">接单</button>
        <button type="button" class="cta__ghost" @click="goBack">返回</button>
      </div>
    </div>
  </section>

  <section v-else class="empty">
    未找到该公告，返回列表重试。
    <button class="back" type="button" @click="goBack">返回</button>
  </section>
</template>

<style scoped>
.detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.back {
  align-self: flex-start;
  border: none;
  background: rgba(255, 214, 102, 0.2);
  color: #4a3900;
  padding: 8px 12px;
  border-radius: 10px;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.38);
  transition: transform 0.12s ease;
}

.back:hover {
  transform: translateY(-1px);
}

.detail__hero {
  padding: 16px;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(255, 214, 102, 0.32), rgba(255, 184, 71, 0.24));
  border: 1px solid rgba(255, 214, 102, 0.4);
  box-shadow: 0 12px 24px rgba(88, 70, 0, 0.1);
}

.badge {
  display: inline-block;
  padding: 8px 10px;
  border-radius: 12px;
  background: #ffd766;
  color: #2e2200;
  font-weight: 800;
}

.distance {
  margin-top: 6px;
  color: rgba(46, 34, 0, 0.72);
  font-weight: 700;
}

h1 {
  margin: 8px 0 4px;
  color: #2e2200;
}

.sub {
  margin: 0;
  color: rgba(46, 34, 0, 0.7);
}

.card {
  background: #fffaf0;
  border-radius: 18px;
  padding: 18px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: 0 12px 30px rgba(88, 70, 0, 0.08);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
}

.label {
  display: block;
  color: rgba(46, 34, 0, 0.6);
  margin-bottom: 4px;
}

.value {
  margin: 0;
  color: #2e2200;
}

.status .pill {
  padding: 10px 14px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.2);
  border: 1px solid rgba(255, 214, 102, 0.36);
  font-weight: 700;
  color: #4a3900;
}

.cta {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.cta__primary,
.cta__ghost {
  padding: 12px 16px;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.45);
  transition: transform 0.12s ease, box-shadow 0.12s ease;
}

.cta__primary {
  background: linear-gradient(145deg, #ffc857, #ffb347);
  color: #3a2c00;
  box-shadow: 0 12px 20px rgba(255, 200, 87, 0.32);
}

.cta__ghost {
  background: rgba(255, 214, 102, 0.14);
  color: #4a3900;
}

.cta__primary:hover,
.cta__ghost:hover {
  transform: translateY(-1px);
}

.empty {
  padding: 32px;
  background: rgba(255, 214, 102, 0.1);
  border-radius: 14px;
  color: #5c4300;
  border: 1px dashed rgba(255, 214, 102, 0.4);
}
</style>
