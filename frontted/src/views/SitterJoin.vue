<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { joinSitterApi } from '../api/sitter';
import { setSitterId, user } from '../data/user';

const router = useRouter();

const form = reactive({
  name: user.username || '',
  gender: '女生',
  slogan: '',
  tags: '',
  petName: '',
  petDesc: '',
  petImage: '',
  feedPrice: '￥58/次',
  feedDuration: '30分钟',
  walkPrice: '￥68/次',
  walkDuration: '30分钟'
});

const handleSubmit = async () => {
  if (!form.name.trim()) {
    alert('请填写姓名');
    return;
  }
  
  try {
    const response = await joinSitterApi({
      name: form.name,
      gender: form.gender,
      slogan: form.slogan || '可信赖的本地宠托师',
      tags: form.tags
        ? form.tags
            .split(',')
            .map((t) => t.trim())
            .filter(Boolean)
        : ['时间灵活', '反馈及时'],
      pets: [
        {
          name: form.petName || '我的宠物',
          desc: form.petDesc || '温柔可爱',
          cover: form.petImage
        }
      ],
      services: [
        { type: 'feed-cat', title: '上门喂猫', price: form.feedPrice, duration: form.feedDuration },
        { type: 'walk-dog', title: '上门遛狗', price: form.walkPrice, duration: form.walkDuration }
      ]
    });

    if (response.success && response.body) {
      setSitterId(response.body.sitter_id);
      alert('入驻成功，可前往喂溜公告接单');
      router.push({ name: 'Sitters' });
    } else {
      alert(response.message || '入驻失败');
    }
  } catch (error) {
    console.error('入驻失败:', error);
    alert('入驻失败，请稍后重试');
  }
};

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      form.petImage = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const goBack = () => router.back();
</script>

<template>
  <section class="join">
    <header class="page__header">
      <div>
        <p class="eyebrow">{{ isEdit ? '编辑宠托师信息' : '入驻宠托师' }}</p>
        <h1>{{ isEdit ? '更新信息' : '完善信息，开始接单' }}</h1>
        <p v-if="isEdit" class="subtitle" style="color: #4caf50; font-weight: 600;">✓ 已入驻，可修改信息并重新提交</p>
        <p v-else class="subtitle">填写个人简介、标签、养宠展示及服务价格。</p>
      </div>
      <button class="back" type="button" @click="goBack">返回</button>
    </header>

    <form class="card" @submit.prevent="handleSubmit">
      <div class="grid">
        <label class="field">
          <span>姓名 *</span>
          <input v-model="form.name" type="text" required />
        </label>
        <label class="field">
          <span>性别</span>
          <select v-model="form.gender">
            <option value="女生">女生</option>
            <option value="男生">男生</option>
          </select>
        </label>
        <label class="field">
          <span>个人简介</span>
          <input v-model="form.slogan" type="text" placeholder="擅长点/时间优势" />
        </label>
        <label class="field field--full">
          <span>标签（逗号分隔）</span>
          <input v-model="form.tags" type="text" placeholder="猫咪经验, 可陪跑, 时间灵活" />
        </label>
        <label class="field">
          <span>宠物名称</span>
          <input v-model="form.petName" type="text" placeholder="展示一只你的宠物" />
        </label>
        <label class="field">
          <span>宠物描述</span>
          <input v-model="form.petDesc" type="text" placeholder="如：英短蓝白/性格温和" />
        </label>
        <label class="field field--full">
          <span>宠物图片</span>
          <input type="file" accept="image/*" @change="handleImageChange" class="file-input" />
          <div v-if="form.petImage" class="image-preview" :style="{ backgroundImage: `url(${form.petImage})` }"></div>
        </label>
        <label class="field">
          <span>喂猫价格</span>
          <input v-model="form.feedPrice" type="text" />
        </label>
        <label class="field">
          <span>喂猫时长</span>
          <input v-model="form.feedDuration" type="text" />
        </label>
        <label class="field">
          <span>遛狗价格</span>
          <input v-model="form.walkPrice" type="text" />
        </label>
        <label class="field">
          <span>遛狗时长</span>
          <input v-model="form.walkDuration" type="text" />
        </label>
      </div>
      <div class="actions">
        <button type="submit" class="cta">{{ isEdit ? '更新信息' : '提交入驻' }}</button>
      </div>
    </form>
  </section>
</template>

<style scoped>
.join {
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
select:focus {
  border-color: rgba(255, 214, 102, 0.8);
  box-shadow: 0 0 0 3px rgba(255, 214, 102, 0.35);
}

.field--full {
  grid-column: span 2;
}

.file-input {
  padding: 8px !important;
}

.image-preview {
  width: 100%;
  height: 200px;
  margin-top: 12px;
  border-radius: 12px;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
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
