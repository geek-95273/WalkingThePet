<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { findPetProfile, removePetProfile } from '../data/petProfiles';

const route = useRoute();
const router = useRouter();
const pet = computed(() => findPetProfile(route.params.id));

const editPet = () => {
  if (!pet.value) return;
  router.push({ name: 'PetEdit', params: { id: pet.value.id } });
};

const deletePet = () => {
  if (!pet.value) return;
  if (confirm('确认删除该宠物档案？')) {
    removePetProfile(pet.value.id);
    router.push({ name: 'PetArchive' });
  }
};

const goBack = () => router.push({ name: 'PetArchive' });
</script>

<template>
  <section v-if="pet" class="detail">
    <button class="back" type="button" @click="goBack">← 返回宠物档案</button>

    <div class="hero">
      <div class="hero__cover" :style="{ backgroundImage: `url(${pet.image})` }"></div>
      <div class="hero__info">
        <div class="title">
          <h1>{{ pet.name }}</h1>
          <span class="pill">{{ pet.type }}</span>
        </div>
        <p class="meta">{{ pet.age }} · {{ pet.gender }} · {{ pet.weight }} · {{ pet.breed }}</p>
        <p class="intro">{{ pet.intro }}</p>
      </div>
    </div>

    <div class="card">
      <h2>基础信息</h2>
      <div class="grid">
        <div class="item">
          <span class="label">类型</span>
          <p class="value">{{ pet.type }}</p>
        </div>
        <div class="item">
          <span class="label">年龄</span>
          <p class="value">{{ pet.age }}</p>
        </div>
        <div class="item">
          <span class="label">性别</span>
          <p class="value">{{ pet.gender }}</p>
        </div>
        <div class="item">
          <span class="label">体重</span>
          <p class="value">{{ pet.weight }}</p>
        </div>
        <div class="item">
          <span class="label">品种</span>
          <p class="value">{{ pet.breed }}</p>
        </div>
        <div class="item">
          <span class="label">是否具有攻击性</span>
          <p class="value">{{ pet.aggressive ? '是' : '否' }}</p>
        </div>
        <div class="item">
          <span class="label">是否接种狂犬疫苗</span>
          <p class="value">{{ pet.rabiesVaccine ? '是' : '否' }}</p>
        </div>
      </div>
    </div>

    <div class="actions">
      <button class="ghost" type="button" @click="editPet">编辑</button>
      <button class="danger" type="button" @click="deletePet">删除</button>
    </div>
  </section>

  <section v-else class="empty">
    未找到该宠物，返回列表重试。
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

.hero {
  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 14px;
  align-items: center;
}

.hero__cover {
  border-radius: 16px;
  height: 240px;
  background-size: cover;
  background-position: center;
  box-shadow: 0 12px 24px rgba(88, 70, 0, 0.12);
}

.hero__info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.title {
  display: flex;
  gap: 8px;
  align-items: center;
}

h1 {
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

.card {
  background: #fffaf0;
  border-radius: 18px;
  padding: 18px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: 0 12px 30px rgba(88, 70, 0, 0.08);
}

h2 {
  margin: 0 0 10px;
  color: #2e2200;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
}

.item {
  padding: 10px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.1);
  border: 1px solid rgba(255, 214, 102, 0.2);
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

.actions {
  display: flex;
  gap: 10px;
}

.ghost,
.danger {
  padding: 10px 14px;
  border-radius: 12px;
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

.empty {
  padding: 32px;
  text-align: center;
  background: rgba(255, 214, 102, 0.1);
  border-radius: 14px;
  color: #5c4300;
  border: 1px dashed rgba(255, 214, 102, 0.4);
}

@media (max-width: 920px) {
  .hero {
    grid-template-columns: 1fr;
  }
}
</style>
