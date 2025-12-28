<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getOrdersApi, completeOrderApi, acceptOrderApi } from '../api/order';
import { user } from '../data/user';

const orders = ref([]);

const loadOrders = async () => {
  try {
    const response = await getOrdersApi();
    if (response.success && response.body) {
      orders.value = response.body.map(order => ({
        id: order.orderId,
        userId: order.userId,
        sitterId: order.sitterId,
        sitterName: order.sitterName || '宠托师',
        serviceType: order.serviceType,
        status: order.status,
        serviceTime: order.serviceTime,
        address: order.address,
        remark: order.remark,
        createdAt: order.createdAt,
        completeContent: order.completeContent,
        completeImage: order.completeImage,
        isMyOrder: order.userId === user.userId, // 是我下的单
        isMySitterOrder: order.sitterId === user.userId // 是我作为宠托师接的单
      }));
    }
  } catch (error) {
    console.error('加载订单失败:', error);
    alert('加载订单失败，请稍后重试');
  }
};

onMounted(() => {
  loadOrders();
});

const serviceLabel = (type) => (type === 'feed-cat' ? '上门喂猫' : '上门溜狗');

const showCompleteModal = ref(false);
const currentOrderId = ref('');
const completeForm = reactive({
  content: '',
  image: ''
});

const openCompleteModal = (orderId) => {
  currentOrderId.value = orderId;
  completeForm.content = '';
  completeForm.image = '';
  showCompleteModal.value = true;
};

const handleImageChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      completeForm.image = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const handleComplete = async () => {
  if (!completeForm.content.trim()) {
    alert('请填写完成内容');
    return;
  }
  if (!completeForm.image) {
    alert('请上传完成图片');
    return;
  }
  
  try {
    const response = await completeOrderApi(currentOrderId.value, {
      completeContent: completeForm.content,
      completeImage: completeForm.image
    });
    
    if (response.success) {
      alert('订单已完成');
      showCompleteModal.value = false;
      await loadOrders(); // 重新加载订单列表
    } else {
      alert(response.message || '完成失败');
    }
  } catch (error) {
    console.error('完成订单失败:', error);
    alert('完成失败，请稍后重试');
  }
};

const cancelComplete = () => {
  showCompleteModal.value = false;
};

const handleAccept = async (orderId) => {
  try {
    const response = await acceptOrderApi(orderId);
    if (response.success) {
      alert('接单成功！');
      await loadOrders(); // 重新加载订单列表
    } else {
      alert(response.message || '接单失败');
    }
  } catch (error) {
    console.error('接单失败:', error);
    alert(error.message || '接单失败，请稍后重试');
  }
};
</script>

<template>
  <section class="orders-page">
    <header class="page__header">
      <div>
        <p class="eyebrow">我的订单</p>
        <h1>查看所有订单</h1>
        <p class="subtitle">包含已创建的宠托师订单和完成情况。</p>
      </div>
    </header>

    <div v-if="orders.length" class="orders">
      <article v-for="order in orders" :key="order.id" class="card">
        <div class="row">
          <div>
            <div class="badge">{{ serviceLabel(order.serviceType) }}</div>
            <span v-if="order.isMyOrder" class="role-tag owner">我下的单</span>
            <span v-if="order.isMySitterOrder" class="role-tag sitter">我接的单</span>
            <h3>{{ order.sitterName }}</h3>
            <p class="meta">下单时间：{{ order.createdAt }}</p>
          </div>
          <div class="status">
            <span class="pill">状态：{{ order.status }}</span>
          </div>
        </div>
        <p class="meta">服务时间：{{ order.serviceTime }}</p>
        <p class="meta">地址：{{ order.address }}</p>
        <p class="remark">备注：{{ order.remark || '无' }}</p>
        
        <div v-if="order.status === '已完成' && order.completeContent" class="complete-info">
          <div class="complete-divider"></div>
          <h4>完成反馈</h4>
          <p class="complete-content">{{ order.completeContent }}</p>
          <div v-if="order.completeImage" class="complete-image" :style="{ backgroundImage: `url(${order.completeImage})` }"></div>
        </div>
        
        <div class="actions">
          <!-- 宠托师接的单，状态为已接单时可以完成 -->
          <button 
            v-if="order.isMySitterOrder && order.status === '已接单'" 
            class="cta" 
            type="button" 
            @click="openCompleteModal(order.id)"
          >
            完成订单
          </button>
          <!-- 宠托师看到待接单的订单可以接单 -->
          <button 
            v-if="!order.isMyOrder && order.status === '待宠托师接单'" 
            class="cta accept" 
            type="button" 
            @click="handleAccept(order.id)"
          >
            接单
          </button>
          <!-- 用户下的单，待接单状态显示等待提示 -->
          <span v-if="order.isMyOrder && order.status === '待宠托师接单'" class="waiting-tip">等待宠托师接单...</span>
        </div>
      </article>
    </div>
    <div v-else class="empty">暂无订单，前往宠托师页下单。</div>

    <!-- 完成订单弹窗 -->
    <div v-if="showCompleteModal" class="modal-overlay" @click="cancelComplete">
      <div class="modal" @click.stop>
        <h2>完成订单</h2>
        <div class="modal-form">
          <label class="field">
            <span>完成内容 *</span>
            <textarea 
              v-model="completeForm.content" 
              rows="4" 
              placeholder="请简短描述服务完成情况，如：已喂食、换水、陪玩等"
            ></textarea>
          </label>
          
          <label class="field">
            <span>上传图片 *</span>
            <input type="file" accept="image/*" @change="handleImageChange" class="file-input" />
            <div v-if="completeForm.image" class="image-preview" :style="{ backgroundImage: `url(${completeForm.image})` }"></div>
          </label>
        </div>
        
        <div class="modal-actions">
          <button type="button" class="ghost" @click="cancelComplete">取消</button>
          <button type="button" class="cta" @click="handleComplete">已完成</button>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.orders-page {
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

.orders {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card {
  background: #fffaf0;
  border-radius: 16px;
  padding: 14px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: var(--card-shadow);
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.row {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: center;
}

.badge {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.24);
  border: 1px solid rgba(255, 214, 102, 0.4);
  color: #4a3900;
  font-weight: 800;
}

.role-tag {
  display: inline-block;
  font-size: 11px;
  font-weight: 700;
  padding: 4px 10px;
  border-radius: 6px;
  margin-left: 8px;
}

.role-tag.owner {
  background: rgba(102, 178, 255, 0.2);
  color: #1a5490;
  border: 1px solid rgba(102, 178, 255, 0.4);
}

.role-tag.sitter {
  background: rgba(76, 175, 80, 0.2);
  color: #2d5f2e;
  border: 1px solid rgba(76, 175, 80, 0.4);
}

h3 {
  margin: 6px 0 2px;
  color: #2e2200;
}

.meta {
  margin: 0;
  color: rgba(46, 34, 0, 0.7);
}

.remark {
  margin: 0;
  color: rgba(46, 34, 0, 0.82);
}

.status .pill {
  padding: 10px 14px;
  border-radius: 12px;
  background: rgba(255, 214, 102, 0.2);
  border: 1px solid rgba(255, 214, 102, 0.36);
  font-weight: 700;
  color: #4a3900;
}

.actions {
  display: flex;
  gap: 8px;
}

.ghost {
  padding: 10px 14px;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.45);
  background: rgba(255, 214, 102, 0.14);
  color: #4a3900;
  transition: transform 0.12s ease;
}

.ghost:hover {
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

.cta {
  padding: 10px 20px;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
  border: none;
  background: linear-gradient(135deg, #ffd666 0%, #ffc832 100%);
  color: #2e2200;
  transition: transform 0.12s ease;
}

.cta:hover {
  transform: translateY(-1px);
}

.waiting-tip {
  color: #b17a00;
  font-size: 14px;
  font-style: italic;
}

.complete-info {
  margin-top: 12px;
}

.complete-divider {
  height: 1px;
  background: rgba(255, 214, 102, 0.3);
  margin-bottom: 12px;
}

.complete-info h4 {
  margin: 0 0 8px;
  color: #4a3900;
  font-size: 14px;
}

.complete-content {
  margin: 6px 0;
  color: rgba(46, 34, 0, 0.85);
  line-height: 1.5;
}

.complete-image {
  margin-top: 8px;
  width: 100%;
  height: 200px;
  border-radius: 12px;
  background-size: cover;
  background-position: center;
  border: 1px solid rgba(255, 214, 102, 0.3);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: #fffaf0;
  border-radius: 16px;
  padding: 24px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.modal h2 {
  margin: 0 0 16px;
  color: #2e2200;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field span {
  font-weight: 700;
  color: #4a3900;
}

.field textarea {
  padding: 12px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  border-radius: 12px;
  background: #fff;
  font-family: inherit;
  color: #2e2200;
  resize: vertical;
}

.file-input {
  padding: 12px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  border-radius: 12px;
  background: #fff;
  cursor: pointer;
}

.image-preview {
  width: 100%;
  height: 200px;
  border-radius: 12px;
  background-size: cover;
  background-position: center;
  border: 1px solid rgba(255, 214, 102, 0.3);
  margin-top: 8px;
}

.modal-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  justify-content: flex-end;
}

.modal-actions button {
  padding: 10px 20px;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
}
</style>
