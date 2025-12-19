<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { pets } from '../data/pets';

const route = useRoute();
const router = useRouter();

const keyword = computed(() => (route.query.q || '').toString().trim().toLowerCase());

const filteredPets = computed(() => {
  const term = keyword.value;
  if (!term) return pets;
  return pets.filter((pet) => {
    const haystack = [
      pet.name,
      pet.breed,
      pet.personality,
      pet.distance,
      pet.tags.join(' ')
    ]
      .join(' ')
      .toLowerCase();
    return haystack.includes(term);
  });
});

const goDetail = (id) => {
  const query = route.query.q ? { q: route.query.q } : {};
  router.push({ name: 'PetDetail', params: { id }, query });
};
</script>

<template>
  <section class="pet-page">
    <header class="page__header">
      <div>
        <p class="eyebrow">同城上门 · 温馨陪伴</p>
        <h1>附近的萌宠</h1>
        <p class="subtitle">按距离、品种快速筛选，点击卡片查看详情与注意事项。</p>
        <p v-if="keyword" class="filter-tip">当前筛选：{{ route.query.q }}</p>
      </div>
    </header>

    <div v-if="filteredPets.length" class="waterfall">
      <article
        v-for="pet in filteredPets"
        :key="pet.id"
        class="pet-card"
        tabindex="0"
        role="button"
        @click="goDetail(pet.id)"
        @keyup.enter="goDetail(pet.id)"
        @keyup.space.prevent="goDetail(pet.id)"
      >
        <div class="pet-card__media" :style="{ background: pet.coverGradient }">
          <span class="pet-card__distance">{{ pet.distance }}</span>
          <div class="pet-card__avatar">{{ pet.name.slice(0, 1) }}</div>
        </div>
        <div class="pet-card__body">
          <div class="pet-card__title">
            <div>
              <h3>{{ pet.name }}</h3>
              <span class="breed">{{ pet.breed }}</span>
            </div>
            <span class="age">{{ pet.age }}</span>
          </div>
          <div class="pet-card__meta">
            <span>{{ pet.weight }}</span>
            <span>距离 {{ pet.distance }}</span>
          </div>
          <p class="pet-card__desc">{{ pet.personality }}</p>
          <div class="pet-card__tags">
            <span v-for="tag in pet.tags" :key="tag">{{ tag }}</span>
          </div>
        </div>
      </article>
    </div>
    <div v-else class="empty">暂无匹配宠物，换个关键词试试吧。</div>
  </section>
</template>

<style scoped>
.pet-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.page__header {
  background: rgba(255, 214, 102, 0.14);
  border: 1px solid rgba(255, 214, 102, 0.32);
  border-radius: 18px;
  padding: 18px 20px;
  box-shadow: 0 10px 22px rgba(88, 70, 0, 0.06);
}

.eyebrow {
  margin: 0 0 4px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
  color: #b17a00;
  text-transform: uppercase;
}

h1 {
  margin: 0;
  font-size: 28px;
  color: #2e2200;
}

.subtitle {
  margin: 6px 0 0;
  color: rgba(46, 34, 0, 0.68);
}

.filter-tip {
  margin: 10px 0 0;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  background: rgba(255, 214, 102, 0.24);
  border-radius: 10px;
  color: #5c4300;
  border: 1px solid rgba(255, 214, 102, 0.35);
}

.waterfall {
  column-count: 3;
  column-gap: 16px;
}

.pet-card {
  display: inline-flex;
  flex-direction: column;
  width: 100%;
  background: #fffaf0;
  border-radius: 18px;
  margin: 0 0 16px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: var(--card-shadow);
  cursor: pointer;
  transition: transform 0.18s ease, box-shadow 0.2s ease, border-color 0.18s ease;
  break-inside: avoid;
  outline: none;
}

.pet-card:focus-visible {
  box-shadow: 0 0 0 3px rgba(255, 214, 102, 0.65), var(--card-shadow);
}

.pet-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 28px rgba(88, 70, 0, 0.12);
  border-color: rgba(255, 214, 102, 0.6);
}

.pet-card__media {
  position: relative;
  height: 220px;
  border-radius: 16px;
  margin: 10px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.4);
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  overflow: hidden;
}

.pet-card__distance {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(46, 34, 0, 0.12);
  color: #2e2200;
  font-weight: 600;
  backdrop-filter: blur(4px);
}

.pet-card__avatar {
  margin: 0 0 12px 12px;
  width: 48px;
  height: 48px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.86);
  color: #5c4300;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 14px rgba(88, 70, 0, 0.16);
}

.pet-card__body {
  padding: 0 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.pet-card__title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.pet-card__title h3 {
  margin: 0;
  font-size: 18px;
  color: #2e2200;
}

.breed {
  display: inline-block;
  margin-top: 4px;
  color: rgba(46, 34, 0, 0.7);
}

.age {
  padding: 6px 10px;
  border-radius: 10px;
  background: rgba(255, 214, 102, 0.22);
  border: 1px solid rgba(255, 214, 102, 0.36);
  color: #5c4300;
  font-weight: 600;
}

.pet-card__meta {
  display: flex;
  gap: 8px;
  color: rgba(46, 34, 0, 0.66);
  font-weight: 600;
}

.pet-card__desc {
  margin: 0;
  color: rgba(46, 34, 0, 0.8);
}

.pet-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.pet-card__tags span {
  padding: 6px 10px;
  background: #fff6d8;
  color: #5c4300;
  border-radius: 10px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  font-weight: 600;
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
  .waterfall {
    column-count: 1;
  }
}
</style>
