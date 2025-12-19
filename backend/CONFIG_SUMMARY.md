# 📋 本地环境配置清单

## ✅ 需要创建的数据库表

已通过 `sql/init.sql` 脚本自动创建，包括：

### 1. walking_pet_user（用户服务数据库）
```sql
-- 用户表
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 2. walking_pet_pet（宠物服务数据库）
```sql
-- 宠物档案表
CREATE TABLE pets (
    pet_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    name VARCHAR(50) NOT NULL,
    age VARCHAR(20),
    gender VARCHAR(10),
    weight VARCHAR(20),
    breed VARCHAR(50),
    aggressive BOOLEAN DEFAULT FALSE,
    rabies_vaccine BOOLEAN DEFAULT FALSE,
    intro TEXT,
    image LONGTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 3. walking_pet_bulletin（公告服务数据库）
```sql
-- 喂溜公告表
CREATE TABLE bulletins (
    bulletin_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    service_type VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT '待接单',
    address VARCHAR(200) NOT NULL,
    pet_id VARCHAR(50),
    pet_name VARCHAR(50),
    pet_type VARCHAR(50),
    service_time DATETIME NOT NULL,
    walker_gender VARCHAR(10),
    remark TEXT,
    sitter_id VARCHAR(50),
    distance VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 4. walking_pet_sitter（宠托师服务数据库）
```sql
-- 宠托师表
CREATE TABLE sitters (
    sitter_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    slogan TEXT,
    rating DECIMAL(2,1) DEFAULT 5.0,
    orders INT DEFAULT 0,
    distance VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 宠托师标签表
CREATE TABLE sitter_tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sitter_id VARCHAR(50) NOT NULL,
    tag VARCHAR(50) NOT NULL
);

-- 宠托师宠物展示表
CREATE TABLE sitter_pets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sitter_id VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    cover LONGTEXT
);

-- 服务配置表
CREATE TABLE sitter_services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sitter_id VARCHAR(50) NOT NULL,
    service_type VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    price VARCHAR(20) NOT NULL,
    duration VARCHAR(20) NOT NULL
);
```

### 5. walking_pet_order（订单服务数据库）
```sql
-- 订单表
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    sitter_id VARCHAR(50) NOT NULL,
    sitter_name VARCHAR(50),
    bulletin_id VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    service_type VARCHAR(20) NOT NULL,
    service_time DATETIME NOT NULL,
    address VARCHAR(200) NOT NULL,
    pet_id VARCHAR(50),
    walker_gender VARCHAR(10),
    remark TEXT,
    complete_content TEXT,
    complete_image LONGTEXT,
    completed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 🔧 配置步骤

### 步骤1: 安装软件

#### 1. 安装 MySQL 8.0
- 下载: https://dev.mysql.com/downloads/mysql/
- 安装时设置root密码为: `123456`
- 确保端口: `3306`

#### 2. 安装 JDK 8+
- 下载: https://www.oracle.com/java/technologies/downloads/
- 配置JAVA_HOME环境变量

#### 3. 安装 Maven 3.6+
- 下载: https://maven.apache.org/download.cgi
- 配置MAVEN_HOME和PATH环境变量

#### 4. 下载 Nacos 2.2.0
- 下载: https://github.com/alibaba/nacos/releases/tag/2.2.0
- 解压到任意目录

---

### 步骤2: 初始化数据库

```bash
# 方法1: 命令行执行
cd backend
mysql -uroot -p123456 < sql/init.sql

# 方法2: 使用MySQL客户端
# 打开 Navicat/MySQL Workbench
# 连接到MySQL后执行 sql/init.sql 文件
```

**验证数据库创建成功：**
```sql
SHOW DATABASES;
```

应该看到5个数据库：
- ✅ walking_pet_user
- ✅ walking_pet_pet
- ✅ walking_pet_bulletin
- ✅ walking_pet_sitter
- ✅ walking_pet_order

---

### 步骤3: 修改配置（如果需要）

#### 如果MySQL密码不是123456

修改以下文件：
1. `walking-pet-user-service/src/main/resources/application.yml`
2. `walking-pet-pet-service/src/main/resources/application.yml`
3. `walking-pet-order-service/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    password: 你的密码  # 改这里
```

#### 如果MySQL端口不是3306

修改数据库URL：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:你的端口/数据库名?...
```

---

### 步骤4: 启动服务

#### 4.1 启动Nacos

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

**验证Nacos启动：**
访问: http://localhost:8848/nacos
- 用户名: `nacos`
- 密码: `nacos`

#### 4.2 启动微服务

**使用启动脚本（推荐）:**

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

**或者使用IDE启动：**

在IntelliJ IDEA中依次运行：
1. `GatewayApplication` (8000端口)
2. `UserServiceApplication` (8001端口)
3. `PetServiceApplication` (8002端口)
4. `OrderServiceApplication` (8005端口)

**等待30秒让服务完全启动**

---

### 步骤5: 验证服务

#### 5.1 检查Nacos服务注册

访问: http://localhost:8848/nacos

在 **服务管理 -> 服务列表** 应该看到：
- ✅ gateway-service (1个实例)
- ✅ user-service (1个实例)
- ✅ pet-service (1个实例)
- ✅ order-service (1个实例)

#### 5.2 测试API

**测试注册：**
```bash
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "confirmPassword": "123456"
  }'
```

**预期响应：**
```json
{
  "error": 0,
  "body": {
    "userId": "u-1703001234567",
    "username": "testuser"
  },
  "message": "注册成功",
  "success": true
}
```

**测试登录：**
```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

**预期响应：**
```json
{
  "error": 0,
  "body": {
    "userId": "u-1703001234567",
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "message": "登录成功",
  "success": true
}
```

---

## 📡 端口占用情况

| 服务 | 端口 | 用途 |
|-----|------|------|
| MySQL | 3306 | 数据库 |
| Nacos | 8848 | 服务注册与配置中心 |
| Gateway | 8000 | API网关 |
| User Service | 8001 | 用户服务 |
| Pet Service | 8002 | 宠物服务 |
| Bulletin Service | 8003 | 公告服务（待实现） |
| Sitter Service | 8004 | 宠托师服务（待实现） |
| Order Service | 8005 | 订单服务 |

---

## 🔗 前端配置

### 修改前端API地址

在前端项目中配置：

```javascript
// src/main.js 或 axios配置文件
import axios from 'axios';

// 设置基础URL为网关地址
axios.defaults.baseURL = 'http://localhost:8000';

// 请求拦截器 - 自动添加Token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截器 - 处理401未授权
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      // 跳转到登录页
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### 修改用户登录逻辑

```javascript
// 登录成功后保存token
async function login(username, password) {
  const response = await axios.post('/api/auth/login', {
    username,
    password
  });
  
  if (response.data.success) {
    // 保存token到localStorage
    localStorage.setItem('token', response.data.body.token);
    // 保存用户信息
    localStorage.setItem('userId', response.data.body.userId);
    localStorage.setItem('username', response.data.body.username);
  }
  
  return response.data;
}
```

---

## 📝 测试账号

数据库初始化时已创建测试账号：

| 用户名 | 密码 | 用户ID |
|-------|------|--------|
| admin | 123456 | u-1000 |
| test | 123456 | u-1001 |

---

## 🎯 项目结构

```
backend/
├── sql/
│   └── init.sql                    # 数据库初始化脚本
├── walking-pet-common/             # 公共模块
│   ├── common-core/                # 核心工具
│   └── common-security/            # 安全模块
├── walking-pet-gateway/            # API网关 (8000)
├── walking-pet-user-service/       # 用户服务 (8001)
├── walking-pet-pet-service/        # 宠物服务 (8002)
├── walking-pet-order-service/      # 订单服务 (8005)
├── pom.xml                         # 父POM
├── README.md                       # 详细文档
├── QUICKSTART.md                   # 快速启动指南
├── CONFIG_SUMMARY.md              # 本文件
├── start.bat                       # Windows启动脚本
├── start.sh                        # Linux/Mac启动脚本
└── docker-compose.yml             # Docker配置
```

---

## ✅ 配置完成检查清单

- [ ] MySQL 8.0已安装并启动（端口3306）
- [ ] 数据库已初始化（5个数据库已创建）
- [ ] JDK 8+已安装
- [ ] Maven 3.6+已安装
- [ ] Nacos 2.2.0已下载并启动（端口8848）
- [ ] 4个微服务已启动
- [ ] Nacos控制台显示4个服务已注册
- [ ] API测试通过（注册、登录接口正常）
- [ ] 前端已配置网关地址
- [ ] 前端已配置Token拦截器

---

**配置完成！可以开始开发了！** 🎉
