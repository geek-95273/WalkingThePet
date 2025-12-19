# 前端对接后端API配置指南

## ✅ 已完成的配置

### 1. API请求配置
已创建以下文件用于与后端API通信：

#### `src/api/request.js` - Axios请求封装
- 配置了后端网关地址：`http://localhost:8000`
- 自动在请求头中添加JWT Token
- 统一处理401未授权错误（自动跳转登录页）
- 统一处理响应格式

#### `src/api/user.js` - 用户相关API
- `registerApi()` - 用户注册
- `loginApi()` - 用户登录
- `logoutApi()` - 用户登出

#### `src/api/pet.js` - 宠物相关API
- `getPetsApi()` - 获取宠物列表
- `getPetDetailApi(id)` - 获取宠物详情
- `createPetApi(data)` - 创建宠物档案
- `updatePetApi(id, data)` - 更新宠物档案
- `deletePetApi(id)` - 删除宠物档案

#### `src/api/order.js` - 订单相关API
- `getOrdersApi()` - 获取订单列表
- `getOrderDetailApi(id)` - 获取订单详情
- `createOrderApi(data)` - 创建订单
- `acceptOrderApi(id)` - 宠托师接单
- `completeOrderApi(id, data)` - 完成订单

### 2. 用户状态管理
已更新 `src/data/user.js`：
- 登录/注册改为调用后端API（异步函数）
- Token自动存储在localStorage
- 页面刷新时自动恢复登录状态
- 登出时清除所有本地存储

### 3. 视图组件更新
已更新以下组件支持异步操作：
- `src/views/Login.vue` - 支持异步登录
- `src/views/Register.vue` - 支持异步注册

---

## 🔧 需要手动完成的步骤

### 步骤1：安装axios依赖

在前端项目目录下运行：

```bash
cd d:\桌面\Walking the pet\frontted
npm install axios
```

或使用pnpm/yarn：
```bash
pnpm add axios
# 或
yarn add axios
```

### 步骤2：启动后端服务

确保后端服务已启动（参考backend文件夹下的启动文档）：

1. MySQL数据库已运行并初始化
2. Nacos服务注册中心已启动
3. 后端微服务已启动：
   - Gateway (8000)
   - User Service (8001)
   - Pet Service (8002)
   - Order Service (8005)

### 步骤3：启动前端开发服务器

```bash
cd d:\桌面\Walking the pet\frontted
npm run dev
```

### 步骤4：测试功能

1. **注册测试**：
   - 访问注册页面
   - 输入用户名：testuser
   - 输入密码：123456
   - 点击注册按钮
   - 检查是否跳转到登录页

2. **登录测试**：
   - 使用刚注册的账号登录
   - 或使用测试账号：admin / 123456
   - 检查登录成功后token是否存储
   - 打开浏览器开发者工具 → Application → Local Storage
   - 应该看到token、userId、username

3. **API调用测试**：
   - 登录后访问需要认证的页面
   - 检查Network面板，所有请求应该自动带上Authorization头
   - 检查响应数据格式是否正确

---

## 📝 后续开发指南

### 如何在其他组件中调用API

#### 示例1：获取宠物列表

```javascript
<script setup>
import { ref, onMounted } from 'vue';
import { getPetsApi } from '../api/pet';

const pets = ref([]);
const loading = ref(false);

const loadPets = async () => {
  loading.value = true;
  try {
    const response = await getPetsApi();
    if (response.success) {
      pets.value = response.body || [];
    }
  } catch (error) {
    console.error('获取宠物列表失败:', error);
    alert('获取宠物列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadPets();
});
</script>
```

#### 示例2：创建宠物档案

```javascript
<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { createPetApi } from '../api/pet';

const router = useRouter();
const form = reactive({
  type: '猫',
  name: '',
  age: '',
  gender: '公',
  weight: '',
  breed: '',
  image: ''
});

const handleSubmit = async () => {
  try {
    const response = await createPetApi({
      petType: form.type,
      petName: form.name,
      petAge: parseInt(form.age),
      petGender: form.gender,
      petWeight: parseFloat(form.weight),
      petBreed: form.breed,
      petImage: form.image // Base64格式
    });
    
    if (response.success) {
      alert('创建成功');
      router.push({ name: 'PetList' });
    }
  } catch (error) {
    console.error('创建失败:', error);
    alert('创建失败');
  }
};
</script>
```

#### 示例3：创建订单

```javascript
<script setup>
import { reactive } from 'vue';
import { createOrderApi } from '../api/order';

const form = reactive({
  orderType: 'feed-cat',
  serviceDate: '',
  location: '',
  petId: null,
  petName: '',
  petBreed: '',
  price: 50
});

const handleCreateOrder = async () => {
  try {
    const response = await createOrderApi({
      orderType: form.orderType,
      serviceDate: form.serviceDate,
      location: form.location,
      petId: form.petId,
      petName: form.petName,
      petBreed: form.petBreed,
      price: form.price
    });
    
    if (response.success) {
      alert('订单创建成功');
      // 订单ID: response.body.orderId
    }
  } catch (error) {
    console.error('创建订单失败:', error);
    alert('创建订单失败');
  }
};
</script>
```

---

## 🔍 API响应格式说明

### 成功响应
```json
{
  "code": 200,
  "success": true,
  "message": "操作成功",
  "body": {
    // 实际数据
  }
}
```

### 失败响应
```json
{
  "code": 400,
  "success": false,
  "message": "错误信息",
  "body": null
}
```

### 用户登录成功响应
```json
{
  "code": 200,
  "success": true,
  "message": "登录成功",
  "body": {
    "userId": "u-1000",
    "username": "admin",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

## 🐛 常见问题

### 问题1：跨域错误（CORS）
**现象**：浏览器控制台显示CORS错误

**解决**：后端Gateway已配置CORS，允许以下来源：
- http://localhost:5173 (Vite默认端口)
- http://localhost:8080

如果前端端口不同，需要修改后端Gateway配置。

### 问题2：401 Unauthorized
**现象**：请求返回401错误

**原因**：
- Token过期或无效
- Token未正确发送

**解决**：
- 检查localStorage中是否有token
- 检查Network面板，请求头是否包含`Authorization: Bearer <token>`
- 重新登录获取新token

### 问题3：网络请求失败
**现象**：ERR_CONNECTION_REFUSED

**原因**：后端服务未启动

**解决**：
1. 检查后端服务是否启动
2. 访问 http://localhost:8000 确认Gateway是否可访问
3. 访问 http://localhost:8848/nacos 确认服务是否注册

---

## 📊 开发环境检查清单

- [ ] 已安装axios依赖
- [ ] MySQL数据库已启动并初始化
- [ ] Nacos服务已启动 (http://localhost:8848/nacos)
- [ ] 后端Gateway已启动 (http://localhost:8000)
- [ ] 后端User Service已注册到Nacos
- [ ] 后端Pet Service已注册到Nacos
- [ ] 后端Order Service已注册到Nacos
- [ ] 前端开发服务器已启动
- [ ] 浏览器可以正常访问前端页面
- [ ] 注册功能正常
- [ ] 登录功能正常
- [ ] Token自动添加到请求头

---

## 🎯 下一步开发建议

1. **更新其他数据文件**：
   - `src/data/pets.js` - 改为调用宠物API
   - `src/data/orders.js` - 改为调用订单API
   - `src/data/bulletins.js` - 如果后端已实现公告服务，改为调用API

2. **优化用户体验**：
   - 添加加载状态指示器
   - 使用更友好的提示信息（替换alert）
   - 添加表单验证提示
   - 添加错误重试机制

3. **完善功能**：
   - 实现宠物档案CRUD页面与API对接
   - 实现订单列表页面与API对接
   - 实现订单详情页面与API对接
   - 实现宠托师相关功能

---

## 📞 技术支持

如果遇到问题：
1. 检查浏览器开发者工具的Console和Network面板
2. 检查后端服务日志
3. 确认数据库连接正常
4. 确认Nacos服务注册正常

**测试账号**：
- 用户名：admin
- 密码：123456

**API网关地址**：http://localhost:8000

**Nacos控制台**：http://localhost:8848/nacos (nacos/nacos)
