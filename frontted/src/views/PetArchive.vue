<script setup>
import { useRouter } from 'vue-router';
import { petProfiles, removePetProfile } from '../data/petProfiles';

const router = useRouter();

const goDetail = (id) => router.push({ name: 'PetDetail', params: { id } });
const goCreate = () => router.push({ name: 'PetCreate' });
const editPet = (id) => router.push({ name: 'PetEdit', params: { id } });
const deletePet = (id) => {
  if (confirm('确认删除该宠物档案？')) {
    removePetProfile(id);
  }
};
</script>

<template>
  <section class="archive">
    <header class="page__header">
      <div>
        <p class="eyebrow">宠物档案</p>
        <h1>管理我的宠物</h1>
        <p class="subtitle">查看、编辑、删除宠物信息，支持添加新宠物。</p>
      </div>
    </header>

    <div v-if="petProfiles.length" class="waterfall">
      <article
        v-for="pet in petProfiles"
        :key="pet.id"
        class="card"
        role="button"
        tabindex="0"
        @click="goDetail(pet.id)"
        @keyup.enter="goDetail(pet.id)"
        @keyup.space.prevent="goDetail(pet.id)"
      >
        <div class="thumb" :style="{ backgroundImage: `url(${pet.image})` }"></div>
        <div class="body">
          <div class="title-row">
            <h3>{{ pet.name }}</h3>
            <span class="pill">{{ pet.type }}</span>
          </div>
          <p class="meta">{{ pet.age }} · {{ pet.gender }} · {{ pet.weight }} · {{ pet.breed }}</p>
          <p class="intro">{{ pet.intro }}</p>
          <div class="actions">
            <button type="button" class="ghost" @click.stop="editPet(pet.id)">编辑</button>
            <button type="button" class="danger" @click.stop="deletePet(pet.id)">删除</button>
          </div>
        </div>
      </article>
    </div>
    <div v-else class="empty">暂无宠物档案，添加一个吧。</div>

    <div class="add">
      <button class="cta" type="button" @click="goCreate">添加宠物</button>
    </div>
  </section>
</template>

<style scoped>
.archive {
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

.waterfall {
  column-count: 3;
  column-gap: 16px;
}

.card {
  display: inline-flex;
  flex-direction: column;
  width: 100%;
  background: #fffaf0;
  border-radius: 16px;
  margin: 0 0 16px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: var(--card-shadow);
  break-inside: avoid;
  cursor: pointer;
  overflow: hidden;
}

.thumb {
  height: 180px;
  background-size: cover;
  background-position: center;
}

.body {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h3 {
  margin: 0;
  color: #2e2200;
}

.pill {
  padding: 6px 10px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.2);
  border: 1px solid rgba(255, 214, 102, 0.36);
  font-weight: 700;
  color: #4a3900;
}

.meta {
  margin: 0;
  color: rgba(46, 34, 0, 0.7);
}

.intro {
  margin: 0;
  color: rgba(46, 34, 0, 0.82);
}

.actions {
  display: flex;
  gap: 8px;
}

.ghost,
.danger {
  padding: 8px 12px;
  border-radius: 10px;
  font-weight: 700;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.45);
  background: rgba(255, 214, 102, 0.14);
  color: #4a3900;
}

.danger {
  border-color: rgba(200, 80, 0, 0.35);
  background: rgba(200, 80, 0, 0.12);
  color: #a03a00;
}

.add {
  display: flex;
  justify-content: center;
}

.cta {
  padding: 12px 16px;
  border-radius: 12px;
  font-weight: 800;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: linear-gradient(145deg, #ffc857, #ffb347);
  color: #2e2200;
  box-shadow: 0 12px 20px rgba(255, 200, 87, 0.32);
  transition: transform 0.12s ease;
}

.cta:hover {
  transform: translateY(-1px);
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
