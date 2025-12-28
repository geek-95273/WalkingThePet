<script setup>
import { computed, reactive, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getPetDetailApi, createPetApi, updatePetApi } from '../api/pet';

const route = useRoute();
const router = useRouter();
const isEdit = computed(() => Boolean(route.params.id));
const loading = ref(false);

const form = reactive({
  type: '猫',
  name: '',
  age: '',
  gender: '公',
  weight: '',
  breed: '',
  aggressive: false,
  rabiesVaccine: false,
  intro: '',
  image: 'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?auto=format&fit=crop&w=400&q=80'
});

// 加载宠物详情（编辑模式）
const loadPetDetail = async () => {
  if (!isEdit.value || !route.params.id) return;
  
  try {
    loading.value = true;
    const data = await getPetDetailApi(route.params.id);
    if (data) {
      Object.assign(form, {
        type: data.type || '猫',
        name: data.name || '',
        age: data.age || '',
        gender: data.gender || '公',
        weight: data.weight || '',
        breed: data.breed || '',
        aggressive: data.aggressive || false,
        rabiesVaccine: data.rabiesVaccine || false,
        intro: data.intro || '',
        image: data.image || form.image
      });
    }
  } catch (error) {
    console.error('加载宠物详情失败:', error);
    alert('加载宠物信息失败，请重试');
  } finally {
    loading.value = false;
  }
};

const handleSubmit = async () => {
  if (!form.name.trim()) {
    alert('请填写名字');
    return;
  }
  
  try {
    loading.value = true;
    const payload = { ...form };
    
    if (isEdit.value && route.params.id) {
      // 更新宠物
      await updatePetApi(route.params.id, payload);
      alert('更新成功！');
      router.push({ name: 'PetDetail', params: { id: route.params.id } });
    } else {
      // 创建新宠物
      const result = await createPetApi(payload);
      alert('添加成功！');
      router.push({ name: 'PetArchive' });
    }
  } catch (error) {
    console.error('保存失败:', error);
    alert(error.message || '保存失败，请重试');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadPetDetail();
});

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    console.log('选择的文件:', file.name, '大小:', (file.size / 1024).toFixed(2) + 'KB');
    const reader = new FileReader();
    reader.onload = (e) => {
      form.image = e.target.result;
      console.log('图片Base64长度:', form.image.length);
      console.log('图片Base64前100字符:', form.image.substring(0, 100));
    };
    reader.readAsDataURL(file);
  }
};

const goBack = () => router.push({ name: 'PetArchive' });
</script>

<template>
  <section class="pet-form">
    <header class="page__header">
      <div>
        <p class="eyebrow">宠物档案</p>
        <h1>{{ isEdit ? '编辑宠物' : '添加宠物' }}</h1>
        <p class="subtitle">填写宠物详细信息与照片。</p>
      </div>
      <button class="back" type="button" @click="goBack">返回列表</button>
    </header>

    <form class="card" @submit.prevent="handleSubmit">
      <div v-if="loading" style="text-align: center; padding: 20px;">
        <p>{{ isEdit ? '加载中...' : '保存中...' }}</p>
      </div>
      <div v-else class="grid">
        <label class="field">
          <span>类型</span>
          <select v-model="form.type">
            <option value="猫">猫</option>
            <option value="狗">狗</option>
            <option value="其他">其他</option>
          </select>
        </label>
        <label class="field">
          <span>名字 *</span>
          <input v-model="form.name" type="text" required />
        </label>
        <label class="field">
          <span>年龄</span>
          <input v-model="form.age" type="text" placeholder="如：1岁2个月" />
        </label>
        <label class="field">
          <span>性别</span>
          <select v-model="form.gender">
            <option value="公">公</option>
            <option value="母">母</option>
          </select>
        </label>
        <label class="field">
          <span>体重</span>
          <input v-model="form.weight" type="text" placeholder="如：4.5kg" />
        </label>
        <label class="field">
          <span>品种</span>
          <input v-model="form.breed" type="text" placeholder="如：英短/柯基" />
        </label>
        <label class="field">
          <span>是否具有攻击性</span>
          <select v-model="form.aggressive">
            <option :value="false">否</option>
            <option :value="true">是</option>
          </select>
        </label>
        <label class="field">
          <span>是否接种狂犬疫苗</span>
          <select v-model="form.rabiesVaccine">
            <option :value="true">是</option>
            <option :value="false">否</option>
          </select>
        </label>
        <label class="field field--full">
          <span>宠物简介</span>
          <textarea v-model="form.intro" rows="4" placeholder="性格、禁忌、与人互动偏好等"></textarea>
        </label>
        <label class="field field--full">
          <span>宠物图片</span>
          <div class="image-upload">
            <input type="file" accept="image/*" @change="handleImageChange" />
            <div v-if="form.image" class="image-preview" :style="{ backgroundImage: `url(${form.image})` }"></div>
          </div>
        </label>
      </div>
      <div class="actions">
        <button type="submit" class="cta" :disabled="loading">
          {{ loading ? '保存中...' : '保存' }}
        </button>
      </div>
    </form>
  </section>
</template>

<style scoped>
.pet-form {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.back {
  border: none;
  background: rgba(255, 214, 102, 0.2);
  color: #4a3900;
  padding: 8px 12px;
  border-radius: 10px;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.38);
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

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
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

.image-upload {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.image-upload input[type="file"] {
  padding: 8px;
}

.image-preview {
  width: 100%;
  height: 200px;
  border-radius: 12px;
  background-size: cover;
  background-position: center;
  border: 1px solid rgba(255, 214, 102, 0.4);
}

.actions {
  display: flex;
  justify-content: flex-end;
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

@media (max-width: 720px) {
  .field--full {
    grid-column: span 1;
  }
}
</style>
