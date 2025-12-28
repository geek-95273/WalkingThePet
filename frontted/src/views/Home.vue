<script setup>
import { computed, nextTick, reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { createBulletinApi } from '../api/bulletin';
import { createOrderApi } from '../api/order';
import { getPetsApi } from '../api/pet';

const router = useRouter();
const formRef = ref(null);
const petProfiles = ref([]);

const loadPets = async () => {
  try {
    const response = await getPetsApi();
    if (response.success && response.body) {
      petProfiles.value = response.body.map(pet => ({
        id: pet.petId,
        name: pet.name,
        type: pet.type,
        breed: pet.breed || pet.type
      }));
    }
  } catch (error) {
    console.error('加载宠物列表失败:', error);
  }
};

onMounted(() => {
  loadPets();
});

const form = reactive({
  serviceType: 'feed-cat',
  address: '',
  petId: '',
  petName: '',
  petType: '',
  serviceTime: '',
  walkerGender: '不限',
  remark: ''
});

const serviceCopy = computed(() =>
  form.serviceType === 'feed-cat' ? '上门喂猫 · 温柔陪伴' : '上门遛狗 · 安全守护'
);

const quickSelect = (type) => {
  form.serviceType = type;
  nextTick(() => {
    formRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  });
};

const selectPet = (petId) => {
  const pet = petProfiles.value.find(p => p.id === petId);
  if (pet) {
    form.petId = pet.id;
    form.petName = pet.name;
    form.petType = pet.breed || pet.type;
  }
};

const goToPetArchive = () => {
  router.push({ name: 'PetArchive' });
};

const handleSubmit = async () => {
  if (!form.address.trim() || !form.serviceTime.trim()) {
    alert('请填写地址与服务时间');
    return;
  }

  const title =
    form.serviceType === 'feed-cat'
      ? `上门喂猫 · ${form.petName || '小猫'}`
      : `上门遛狗 · ${form.petName || '小狗'}`;

  // 格式化服务时间为后端期望的格式 "yyyy-MM-dd HH:mm"
  let serviceTime = form.serviceTime;
  if (serviceTime) {
    // 将 "2025-12-27T15:13" 或 "2025-12-27T15:13:00" 转换为 "2025-12-27 15:13"
    serviceTime = serviceTime.substring(0, 16).replace('T', ' ');
  }

  try {
    const response = await createBulletinApi({
      title,
      serviceType: form.serviceType,
      address: form.address,
      petId: form.petId,
      petName: form.petName,
      petType: form.petType,
      serviceTime: serviceTime,
      walkerGender: form.walkerGender,
      remark: form.remark
    });

    if (response && response.body) {
      // 公告创建成功后，立即创建对应的订单
      try {
        const orderResponse = await createOrderApi({
          bulletinId: response.body.id,
          serviceType: form.serviceType,
          serviceTime: serviceTime,
          address: form.address,
          petId: form.petId,
          petName: form.petName,
          petType: form.petType,
          walkerGender: form.walkerGender,
          remark: form.remark,
          status: '待宠托师接单'
        });
        console.log('公告和订单创建成功', orderResponse);
      } catch (orderError) {
        console.error('创建订单失败:', orderError);
        alert('公告创建成功，但订单创建失败: ' + (orderError.message || '未知错误'));
        // 继续执行，不阻止跳转
      }
      
      alert('公告创建成功！');
      router.push({ name: 'BulletinDetail', params: { id: response.body.id } });
    } else {
      alert('创建失败，请重试');
    }
  } catch (error) {
    console.error('创建公告失败:', error);
    alert(error.message || '创建失败，请稍后重试');
  }
};
</script>

<template>
  <section class="home">
    <div class="hero">
      <div class="hero__copy">
        <p class="eyebrow">同城上门 · 喂猫遛狗</p>
        <h1>把宠物交给附近可信的宠托师</h1>
        <p class="subtitle">两步下单，生成喂溜公告，等待宠托师接单即可。</p>
        <div class="hero__actions">
          <button type="button" class="card card--feed" @click="quickSelect('feed-cat')">
            <span class="card__tag">上门喂猫</span>
            <strong>温柔陪伴，换水喂饭</strong>
            <small>适合短途外出/加班</small>
          </button>
          <button type="button" class="card card--walk" @click="quickSelect('walk-dog')">
            <span class="card__tag">上门遛狗</span>
            <strong>安全牵引，定时遛弯</strong>
            <small>适合夜晚或早晨遛弯</small>
          </button>
        </div>
      </div>
      <div class="hero__badge">
        <div class="badge__title">当前选择</div>
        <div class="badge__value">{{ serviceCopy }}</div>
      </div>
    </div>

    <form ref="formRef" class="order" @submit.prevent="handleSubmit">
      <header class="order__header">
        <div>
          <p class="eyebrow">快速下单</p>
          <h2>填写上门信息</h2>
          <p class="subtitle">添加地址、宠物、时间与备注，生成喂溜公告。</p>
        </div>
        <div class="pill">{{ form.serviceType === 'feed-cat' ? '上门喂猫' : '上门遛狗' }}</div>
      </header>

      <div class="order__grid">
        <label class="field">
          <span>服务类型</span>
          <div class="segmented">
            <button
              type="button"
              :class="{ active: form.serviceType === 'feed-cat' }"
              @click="form.serviceType = 'feed-cat'"
            >
              上门喂猫
            </button>
            <button
              type="button"
              :class="{ active: form.serviceType === 'walk-dog' }"
              @click="form.serviceType = 'walk-dog'"
            >
              上门遛狗
            </button>
          </div>
        </label>

        <label class="field">
          <span>添加地址 *</span>
          <input v-model="form.address" type="text" placeholder="如：和平里小区 5 号楼 2 单元" required />
        </label>

        <label class="field">
          <span>添加宠物</span>
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

        <label class="field">
          <span>选择服务时间 *</span>
          <input v-model="form.serviceTime" type="datetime-local" required />
        </label>

        <label class="field">
          <span>宠托师性别</span>
          <div class="segmented">
            <button
              type="button"
              :class="{ active: form.walkerGender === '不限' }"
              @click="form.walkerGender = '不限'"
            >
              不限
            </button>
            <button
              type="button"
              :class="{ active: form.walkerGender === '男生' }"
              @click="form.walkerGender = '男生'"
            >
              男生
            </button>
            <button
              type="button"
              :class="{ active: form.walkerGender === '女生' }"
              @click="form.walkerGender = '女生'"
            >
              女生
            </button>
          </div>
        </label>

        <label class="field field--full">
          <span>服务备注</span>
          <textarea
            v-model="form.remark"
            rows="4"
            placeholder="喂食量、猫砂/牵引注意、零食偏好等"
          ></textarea>
        </label>
      </div>

      <div class="order__actions">
        <button type="submit" class="cta">生成喂溜公告并下单</button>
      </div>
    </form>
  </section>
</template>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.hero {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 18px;
  padding: 20px;
  border-radius: 20px;
  background: linear-gradient(120deg, rgba(255, 215, 102, 0.32), rgba(255, 248, 225, 0.9));
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: 0 16px 34px rgba(88, 70, 0, 0.08);
}

.hero__copy {
  display: flex;
  flex-direction: column;
  gap: 12px;
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
  margin: 0;
  font-size: 30px;
  color: #2e2200;
}

.subtitle {
  margin: 0;
  color: rgba(46, 34, 0, 0.72);
}

.hero__actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.card {
  border: 1px solid rgba(255, 214, 102, 0.4);
  border-radius: 14px;
  padding: 14px;
  background: #fffaf0;
  box-shadow: 0 12px 28px rgba(88, 70, 0, 0.08);
  text-align: left;
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.18s ease;
}

.card--feed {
  background: linear-gradient(145deg, #fff6d8, #ffe08c);
}

.card--walk {
  background: linear-gradient(145deg, #fff8e1, #ffc857);
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 16px 30px rgba(88, 70, 0, 0.12);
}

.card__tag {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 10px;
  background: rgba(46, 34, 0, 0.12);
  color: #2e2200;
  font-weight: 700;
}

.card strong {
  display: block;
  margin: 8px 0 2px;
  color: #2e2200;
}

.card small {
  color: rgba(46, 34, 0, 0.72);
}

.hero__badge {
  border-radius: 16px;
  background: #fffaf0;
  border: 1px solid rgba(255, 214, 102, 0.4);
  padding: 16px;
  align-self: center;
  box-shadow: 0 12px 24px rgba(88, 70, 0, 0.08);
}

.badge__title {
  color: rgba(46, 34, 0, 0.7);
  margin-bottom: 6px;
}

.badge__value {
  font-weight: 800;
  color: #2e2200;
}

.order {
  background: #fffaf0;
  border-radius: 18px;
  padding: 18px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: 0 16px 34px rgba(88, 70, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

h2 {
  margin: 4px 0;
  color: #2e2200;
}

.pill {
  padding: 10px 14px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.22);
  border: 1px solid rgba(255, 214, 102, 0.38);
  font-weight: 700;
  color: #4a3900;
}

.order__grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
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

.inline {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
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
select,
textarea {
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
select:focus,
textarea:focus {
  border-color: rgba(255, 214, 102, 0.8);
  box-shadow: 0 0 0 3px rgba(255, 214, 102, 0.35);
}

.field--full {
  grid-column: span 2;
}

.segmented {
  display: inline-flex;
  gap: 8px;
  flex-wrap: wrap;
}

.segmented button {
  padding: 10px 14px;
  border-radius: 12px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: #fffef8;
  color: #4a3900;
  cursor: pointer;
  font-weight: 700;
  transition: all 0.15s ease;
}

.segmented button.active {
  background: #ffd766;
  color: #2e2200;
  border-color: rgba(255, 214, 102, 0.8);
  box-shadow: 0 8px 16px rgba(255, 214, 102, 0.32);
}

.order__actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.cta,
.ghost {
  padding: 12px 16px;
  border-radius: 12px;
  font-weight: 800;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.4);
  transition: transform 0.12s ease, box-shadow 0.12s ease;
}

.cta {
  background: linear-gradient(145deg, #ffc857, #ffb347);
  color: #2e2200;
  box-shadow: 0 12px 20px rgba(255, 200, 87, 0.32);
}

.ghost {
  background: rgba(255, 214, 102, 0.14);
  color: #4a3900;
}

.cta:hover,
.ghost:hover {
  transform: translateY(-1px);
}

@media (max-width: 1080px) {
  .hero {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 780px) {
  .order__grid {
    grid-template-columns: 1fr;
  }

  .field--full {
    grid-column: span 1;
  }
}
</style>
