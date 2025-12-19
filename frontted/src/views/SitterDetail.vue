<script setup>
import { computed, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { findSitter } from '../data/sitters';
import { createOrder } from '../data/orders';
import { petProfiles } from '../data/petProfiles';

const route = useRoute();
const router = useRouter();
const sitter = computed(() => findSitter(route.params.id));

const form = reactive({
  serviceType: 'feed-cat',
  serviceTime: '',
  address: '',
  petId: '',
  remark: ''
});

const serviceLabel = (type) => (type === 'feed-cat' ? '上门喂猫' : '上门遛狗');
const selectPet = (petId) => {
  const pet = petProfiles.find(p => p.id === petId);
  if (pet) {
    form.petId = pet.id;
  }
};

const goToPetArchive = () => {
  router.push({ name: 'PetArchive' });
};
const handleSubmit = () => {
  if (!sitter.value) return;
  if (!form.serviceTime.trim() || !form.address.trim()) {
    alert('请填写地址与服务时间');
    return;
  }
  const id = createOrder({
    sitterId: sitter.value.id,
    serviceType: form.serviceType,
    serviceTime: form.serviceTime,
    address: form.address,
    petId: form.petId,
    remark: form.remark
  });
  router.push({ name: 'Profile', query: { order: id } });
};

const goBack = () => router.push({ name: 'Sitters' });
</script>

<template>
  <section v-if="sitter" class="detail">
    <button class="back" type="button" @click="goBack">← 返回宠托师列表</button>

    <div class="hero">
      <div>
        <div class="badge">评分 {{ sitter.rating }} · {{ sitter.orders }} 单 · {{ sitter.distance }}</div>
        <h1>{{ sitter.name }} <span class="gender">· {{ sitter.gender }}</span></h1>
        <p class="slogan">{{ sitter.slogan }}</p>
        <div class="tags">
          <span v-for="tag in sitter.tags" :key="tag">{{ tag }}</span>
        </div>
      </div>
      <div class="hero__avatar">{{ sitter.name.slice(0, 1) }}</div>
    </div>

    <div class="card">
      <h2>拥有宠物</h2>
      <div class="gallery">
        <div v-for="pet in sitter.pets" :key="pet.name" class="gallery__item" :style="{ 
          background: pet.cover.startsWith('data:') || pet.cover.startsWith('http') 
            ? `url(${pet.cover}) center/cover` 
            : pet.cover 
        }">
          <div class="gallery__meta">
            <strong>{{ pet.name }}</strong>
            <span>{{ pet.desc }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <h2>服务项目</h2>
      <div class="services">
        <button
          v-for="srv in sitter.services"
          :key="srv.type"
          type="button"
          class="service"
          :class="{ active: form.serviceType === srv.type }"
          @click="form.serviceType = srv.type"
        >
          <div class="service__title">{{ srv.title }}</div>
          <div class="service__meta">{{ srv.price }} · {{ srv.duration }}</div>
        </button>
      </div>
    </div>

    <form class="card form" @submit.prevent="handleSubmit">
      <h2>下单信息</h2>
      <div class="grid">
        <label class="field">
          <span>服务类型</span>
          <div class="pill">{{ serviceLabel(form.serviceType) }}</div>
        </label>
        <label class="field">
          <span>服务时间 *</span>
          <input v-model="form.serviceTime" type="datetime-local" required />
        </label>
        <label class="field field--full">
          <span>上门地址 *</span>
          <input v-model="form.address" type="text" placeholder="如：和平里小区 5 号楼 2 单元" required />
        </label>
        <label class="field field--full">
          <span>选择宠物</span>
          <div class="pet-selector">
            <select v-model="form.petId" @change="selectPet(form.petId)">
              <option value="">选择宠物</option>
              <option v-for="pet in petProfiles" :key="pet.id" :value="pet.id">
                {{ pet.name }} ({{ pet.type }})
              </option>
            </select>
            <button type="button" class="add-pet-btn" @click="goToPetArchive" title="添加新宠物">
              +
            </button>
          </div>
        </label>
        <label class="field field--full">
          <span>服务备注</span>
          <textarea
            v-model="form.remark"
            rows="4"
            placeholder="喂食量/牵引注意/零食偏好等"
          ></textarea>
        </label>
      </div>
      <div class="actions">
        <button type="submit" class="cta">下单，等待宠托师接单</button>
      </div>
    </form>
  </section>

  <section v-else class="empty">
    未找到该宠托师，返回列表重试。
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
  padding: 16px;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(255, 214, 102, 0.32), rgba(255, 184, 71, 0.24));
  border: 1px solid rgba(255, 214, 102, 0.4);
  box-shadow: 0 12px 24px rgba(88, 70, 0, 0.1);
  display: grid;
  grid-template-columns: 3fr 1fr;
  gap: 12px;
  align-items: center;
}

.badge {
  display: inline-block;
  padding: 8px 10px;
  border-radius: 12px;
  background: #ffd766;
  color: #2e2200;
  font-weight: 800;
}

h1 {
  margin: 8px 0 4px;
  color: #2e2200;
}

.gender {
  color: rgba(46, 34, 0, 0.7);
  font-size: 16px;
}

.slogan {
  margin: 0;
  color: rgba(46, 34, 0, 0.8);
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 8px;
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

.hero__avatar {
  width: 72px;
  height: 72px;
  border-radius: 24px;
  background: linear-gradient(145deg, #ffdf85, #ffc857);
  color: #5c4300;
  font-weight: 800;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 20px rgba(88, 70, 0, 0.18);
  justify-self: end;
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

h2 {
  margin: 0;
  color: #2e2200;
}

.gallery {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
}

.gallery__item {
  position: relative;
  border-radius: 14px;
  min-height: 140px;
  overflow: hidden;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.4);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.gallery__meta {
  position: absolute;
  bottom: 10px;
  left: 10px;
  padding: 8px 10px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.82);
  color: #3a2c00;
  font-weight: 700;
}

.gallery__meta span {
  display: block;
  color: rgba(46, 34, 0, 0.7);
  font-weight: 600;
  margin-top: 2px;
}

.services {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.service {
  flex: 1 1 200px;
  padding: 12px;
  border-radius: 14px;
  border: 1px solid rgba(255, 214, 102, 0.38);
  background: #fffef8;
  cursor: pointer;
  text-align: left;
  transition: all 0.15s ease;
}

.service.active {
  background: #ffd766;
  color: #2e2200;
  box-shadow: 0 10px 18px rgba(255, 214, 102, 0.32);
  border-color: rgba(255, 214, 102, 0.8);
}

.service__title {
  font-weight: 800;
}

.service__meta {
  color: rgba(46, 34, 0, 0.7);
}

.form .grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 10px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field span {
  color: #4a3900;
  font-weight: 700;
}

.pet-selector {
  display: flex;
  gap: 8px;
  align-items: stretch;
}

.pet-selector select {
  flex: 1;
}

.add-pet-btn {
  width: 44px;
  padding: 0;
  border-radius: 12px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: linear-gradient(145deg, #ffc857, #ffb347);
  color: #2e2200;
  font-size: 24px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.15s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-pet-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 16px rgba(255, 214, 102, 0.4);
}

input,
textarea,
select {
  padding: 12px 12px;
  border-radius: 12px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: #fffef8;
  outline: none;
  color: #2e2200;
  font-size: 14px;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

input:focus,
textarea:focus,
select:focus {
  border-color: rgba(255, 214, 102, 0.8);
  box-shadow: 0 0 0 3px rgba(255, 214, 102, 0.35);
}

.field--full {
  grid-column: span 2;
}

.pill {
  display: inline-block;
  padding: 8px 10px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.22);
  border: 1px solid rgba(255, 214, 102, 0.38);
  color: #4a3900;
  font-weight: 800;
}

.actions {
  margin-top: 8px;
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
  transition: transform 0.12s ease, box-shadow 0.12s ease;
}

.cta:hover {
  transform: translateY(-1px);
}

.empty {
  padding: 32px;
  background: rgba(255, 214, 102, 0.1);
  border-radius: 14px;
  color: #5c4300;
  border: 1px dashed rgba(255, 214, 102, 0.4);
}

@media (max-width: 900px) {
  .hero {
    grid-template-columns: 1fr;
  }

  .hero__avatar {
    justify-self: start;
  }

  .field--full {
    grid-column: span 1;
  }
}
</style>
