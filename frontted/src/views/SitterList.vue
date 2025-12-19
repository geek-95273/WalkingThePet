<script setup>
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { sitters } from '../data/sitters';

const router = useRouter();
const sortKey = ref('distance');

const sortedSitters = computed(() => {
  const list = [...sitters];
  if (sortKey.value === 'rating') {
    return list.sort((a, b) => b.rating - a.rating);
  }
  return list;
});

const goDetail = (id) => {
  router.push({ name: 'SitterDetail', params: { id } });
};
</script>

<template>
  <section class="sitters">
    <header class="page__header">
      <div>
        <p class="eyebrow">宠托师</p>
        <h1>附近可信的上门宠托师</h1>
        <p class="subtitle">按瀑布流查看，了解简介与服务，点击进入详情下单。</p>
      </div>
      <div class="filters">
        <button :class="{ active: sortKey === 'distance' }" @click="sortKey = 'distance'">默认排序</button>
        <button :class="{ active: sortKey === 'rating' }" @click="sortKey = 'rating'">评分优先</button>
      </div>
    </header>

    <div class="waterfall">
      <article
        v-for="sitter in sortedSitters"
        :key="sitter.id"
        class="card"
        tabindex="0"
        role="button"
        @click="goDetail(sitter.id)"
        @keyup.enter="goDetail(sitter.id)"
        @keyup.space.prevent="goDetail(sitter.id)"
      >
        <div class="card__top">
          <div class="avatar">{{ sitter.name.slice(0, 1) }}</div>
          <div>
            <h3>{{ sitter.name }} <span class="gender">· {{ sitter.gender }}</span></h3>
            <p class="meta">评分 {{ sitter.rating }} · 已服务 {{ sitter.orders }} 单 · {{ sitter.distance }}</p>
          </div>
        </div>
        <p class="slogan">{{ sitter.slogan }}</p>
        <div class="tags">
          <span v-for="tag in sitter.tags" :key="tag">{{ tag }}</span>
        </div>
        <div class="services">
          <span v-for="srv in sitter.services" :key="srv.type" class="service">
            {{ srv.title }} · {{ srv.price }} · {{ srv.duration }}
          </span>
        </div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.sitters {
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
  gap: 10px;
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
  gap: 10px;
  align-items: center;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: linear-gradient(145deg, #ffdf85, #ffc857);
  color: #5c4300;
  font-weight: 800;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 16px rgba(88, 70, 0, 0.16);
}

h3 {
  margin: 0;
  color: #2e2200;
}

.gender {
  color: rgba(46, 34, 0, 0.7);
  font-size: 14px;
}

.meta {
  margin: 4px 0 0;
  color: rgba(46, 34, 0, 0.68);
}

.slogan {
  margin: 0;
  color: rgba(46, 34, 0, 0.82);
}

.tags,
.services {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tags span,
.service {
  padding: 6px 10px;
  background: #fff6d8;
  border-radius: 10px;
  border: 1px solid rgba(255, 214, 102, 0.38);
  color: #4a3900;
  font-weight: 700;
  font-size: 12px;
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
