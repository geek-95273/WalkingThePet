<script setup>
import { computed, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getBulletinsApi } from '../api/bulletin';

const router = useRouter();
const filter = ref('all');
const bulletins = ref([]);

const loadBulletins = async () => {
  try {
    const params = {};
    if (filter.value !== 'all') {
      params.service_type = filter.value;
    }
    const response = await getBulletinsApi(params);
    if (response.success && response.body) {
      bulletins.value = response.body.list || [];
    }
  } catch (error) {
    console.error('加载公告列表失败:', error);
    alert('加载失败，请稍后重试');
  }
};

onMounted(() => {
  loadBulletins();
});

const filtered = computed(() => {
  if (filter.value === 'all') return bulletins.value;
  return bulletins.value.filter((item) => item.service_type === filter.value);
});

const serviceLabel = (type) => (type === 'feed-cat' ? '上门喂猫' : '上门遛狗');

const goDetail = (id) => {
  router.push({ name: 'BulletinDetail', params: { id } });
};
</script>

<template>
  <section class="bulletins">
    <header class="page__header">
      <div>
        <p class="eyebrow">喂溜公告</p>
        <h1>附近的上门喂猫、遛狗需求</h1>
        <p class="subtitle">瀑布流展示，点击查看详情和接单。</p>
      </div>
      <div class="filters">
        <button :class="{ active: filter === 'all' }" @click="filter = 'all'">全部</button>
        <button :class="{ active: filter === 'feed-cat' }" @click="filter = 'feed-cat'">只看喂猫</button>
        <button :class="{ active: filter === 'walk-dog' }" @click="filter = 'walk-dog'">只看遛狗</button>
      </div>
    </header>

    <div v-if="filtered.length" class="waterfall">
      <article
        v-for="item in filtered"
        :key="item.id"
        class="card"
        role="button"
        tabindex="0"
        @click="goDetail(item.id)"
        @keyup.enter="goDetail(item.id)"
        @keyup.space.prevent="goDetail(item.id)"
      >
        <div class="card__top">
          <span class="badge" :data-type="item.service_type">{{ serviceLabel(item.service_type) }}</span>
          <span class="distance">{{ item.distance }}</span>
        </div>
        <h3>{{ item.title }}</h3>
        <p class="meta">
          {{ item.address }} · {{ item.service_time }}
        </p>
        <p class="desc">{{ item.remark }}</p>
        <div class="tags">
          <span>{{ item.pet_name || '宠物' }} · {{ item.pet_type || '未填写' }}</span>
          <span>性别：{{ item.walker_gender }}</span>
          <span>状态：{{ item.status }}</span>
        </div>
      </article>
    </div>
    <div v-else class="empty">暂无公告，稍后再来看看~</div>
  </section>
</template>

<style scoped>
.bulletins {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page__header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
  background: rgba(255, 214, 102, 0.14);
  border-radius: 16px;
  padding: 14px 16px;
  border: 1px solid rgba(255, 214, 102, 0.32);
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

.filters {
  display: inline-flex;
  gap: 8px;
}

.filters button {
  padding: 10px 12px;
  border-radius: 12px;
  border: 1px solid rgba(255, 214, 102, 0.35);
  background: #fffaf0;
  color: #4a3900;
  cursor: pointer;
  font-weight: 700;
  transition: all 0.15s ease;
}

.filters button.active {
  background: #ffd766;
  color: #2e2200;
  box-shadow: 0 8px 16px rgba(255, 214, 102, 0.32);
}

.waterfall {
  column-count: 3;
  column-gap: 16px;
}

.card {
  display: inline-flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
  padding: 14px;
  background: #fffaf0;
  border-radius: 16px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: var(--card-shadow);
  margin: 0 0 16px;
  break-inside: avoid;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.2s ease, border-color 0.15s ease;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 28px rgba(88, 70, 0, 0.12);
  border-color: rgba(255, 214, 102, 0.55);
}

.card__top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.badge {
  padding: 8px 10px;
  border-radius: 12px;
  font-weight: 800;
  color: #2e2200;
  background: rgba(255, 214, 102, 0.28);
}

.badge[data-type='walk-dog'] {
  background: rgba(255, 200, 87, 0.42);
}

.distance {
  color: rgba(46, 34, 0, 0.65);
  font-weight: 700;
}

h3 {
  margin: 0;
  color: #2e2200;
}

.meta {
  margin: 0;
  color: rgba(46, 34, 0, 0.68);
}

.desc {
  margin: 0;
  color: rgba(46, 34, 0, 0.82);
}

.tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tags span {
  padding: 6px 10px;
  background: #fff6d8;
  border-radius: 10px;
  border: 1px solid rgba(255, 214, 102, 0.38);
  color: #4a3900;
  font-weight: 700;
  font-size: 12px;
}

.empty {
  padding: 32px;
  text-align: center;
  background: rgba(255, 214, 102, 0.1);
  border-radius: 14px;
  color: #5c4300;
  border: 1px dashed rgba(255, 214, 102, 0.4);
}

@media (max-width: 1200px) {
  .waterfall {
    column-count: 2;
  }
}

@media (max-width: 820px) {
  .page__header {
    flex-direction: column;
    align-items: flex-start;
  }

  .waterfall {
    column-count: 1;
  }
}
</style>
