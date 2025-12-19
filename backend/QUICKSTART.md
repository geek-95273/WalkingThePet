# 🚀 快速启动指南

## 最简化的启动步骤

### 前置条件
- ✅ JDK 8 或更高版本
- ✅ Maven 3.6+
- ✅ MySQL 8.0
- ✅ Nacos 2.2.0

---

## 📝 5分钟快速启动

### 第1步：启动MySQL（1分钟）

确保MySQL在3306端口运行，用户名root，密码1234

```bash
# 测试MySQL连接
mysql -uroot -p123456 -e "SELECT 'MySQL OK'"
```

### 第2步：初始化数据库（1分钟）

```bash
# 在backend目录下执行
mysql -uroot -p123456 < sql/init.sql
```

**验证数据库：**
```sql
SHOW DATABASES;
-- 应该看到：
-- walking_pet_user
-- walking_pet_pet  
-- walking_pet_bulletin
-- walking_pet_sitter
-- walking_pet_order
```

### 第3步：启动Nacos（1分钟）

**Windows:**
```bash
cd nacos/bin
startup.cmd -m standalone
```

**Linux/Mac:**
```bash
cd nacos/bin
sh startup.sh -m standalone
```

访问: http://localhost:8848/nacos
- 用户名: nacos
- 密码: nacos

### 第4步：启动微服务（2分钟）

**自动启动（推荐）：**

Windows:
```bash
cd backend
start.bat
```

Linux/Mac:
```bash
cd backend
chmod +x start.sh
./start.sh
```

**手动启动：**

打开4个终端窗口，分别执行：

```bash
# 终端1 - Gateway
cd backend/walking-pet-gateway
mvn spring-boot:run

# 终端2 - User Service  
cd backend/walking-pet-user-service
mvn spring-boot:run

# 终端3 - Pet Service
cd backend/walking-pet-pet-service
mvn spring-boot:run

# 终端4 - Order Service
cd backend/walking-pet-order-service
mvn spring-boot:run
```

### 第5步：验证（30秒）

**测试注册：**
```bash
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","confirmPassword":"123456"}'
```

**测试登录：**
```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

---

## ⚙️ 配置修改

### 如果MySQL密码不是123456

修改以下文件的密码配置：

1. `walking-pet-user-service/src/main/resources/application.yml`
2. `walking-pet-pet-service/src/main/resources/application.yml`
3. `walking-pet-order-service/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    password: 你的密码
```

### 如果端口被占用

修改对应服务的 `application.yml`:

```yaml
server:
  port: 新端口号
```

---

## 🔗 前端对接配置

在前端项目中修改API地址：

```javascript
// main.js 或 axios配置文件
axios.defaults.baseURL = 'http://localhost:8000';

// 添加Token拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器处理401
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // 跳转到登录页
      router.push('/login');
    }
    return Promise.reject(error);
  }
);
```

---

## 📱 API端点

所有请求通过网关 `http://localhost:8000`

### 用户认证
- `POST /api/auth/register` - 注册
- `POST /api/auth/login` - 登录
- `POST /api/auth/logout` - 登出

### 宠物档案
- `GET /api/pets` - 获取列表
- `POST /api/pets` - 创建档案
- `GET /api/pets/{id}` - 获取详情
- `PUT /api/pets/{id}` - 更新档案
- `DELETE /api/pets/{id}` - 删除档案

### 订单管理
- `GET /api/orders` - 获取列表
- `POST /api/orders` - 创建订单
- `GET /api/orders/{id}` - 获取详情
- `POST /api/orders/{id}/accept` - 接单
- `POST /api/orders/{id}/complete` - 完成订单

---

## 🐛 常见问题速查

### 1. 端口8848被占用
```bash
# 查找占用进程
netstat -ano | findstr 8848
# 结束进程
taskkill /PID 进程号 /F
```

### 2. Maven下载慢
配置阿里云镜像 `~/.m2/settings.xml`:
```xml
<mirror>
  <id>aliyun</id>
  <url>https://maven.aliyun.com/repository/public</url>
  <mirrorOf>*</mirrorOf>
</mirror>
```

### 3. 服务未注册到Nacos
- 检查Nacos是否启动
- 检查网络连接
- 等待30秒重试

### 4. 数据库连接失败
```bash
# 检查MySQL状态
mysql -uroot -p123456 -e "SELECT 1"

# 检查数据库是否存在
mysql -uroot -p123456 -e "SHOW DATABASES"
```

---

## 📊 服务监控

### Nacos控制台
http://localhost:8848/nacos

查看服务注册情况、配置管理

### 服务健康检查

```bash
# Gateway
curl http://localhost:8000/actuator/health

# User Service
curl http://localhost:8001/actuator/health

# Pet Service  
curl http://localhost:8002/actuator/health

# Order Service
curl http://localhost:8005/actuator/health
```

---

## 🎯 测试数据

### 测试账号
- 用户名: `admin`, 密码: `123456`
- 用户名: `test`, 密码: `123456`

### 创建测试宠物
```bash
# 先登录获取token
TOKEN=$(curl -s -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}' \
  | jq -r '.body.token')

# 创建宠物
curl -X POST http://localhost:8000/api/pets \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "type":"猫",
    "name":"小花",
    "age":"2岁",
    "gender":"母"
  }'
```

---

## 📞 需要帮助？

检查日志输出，通常包含详细的错误信息：
- Gateway: 8000端口的终端输出
- User Service: 8001端口的终端输出
- Pet Service: 8002端口的终端输出
- Order Service: 8005端口的终端输出

---

**祝开发顺利！** 🎉
