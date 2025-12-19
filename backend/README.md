# Walking Pet 后端微服务项目

## 📋 项目说明

这是一个基于 Spring Boot + Spring Cloud Alibaba 的宠物喂溜服务微服务后端项目。

## 🏗️ 架构设计

### 微服务列表

| 服务名称 | 端口 | 数据库 | 说明 |
|---------|------|--------|------|
| gateway-service | 8000 | - | API网关 |
| user-service | 8001 | walking_pet_user | 用户服务 |
| pet-service | 8002 | walking_pet_pet | 宠物档案服务 |
| bulletin-service | 8003 | walking_pet_bulletin | 喂溜公告服务 |
| sitter-service | 8004 | walking_pet_sitter | 宠托师服务 |
| order-service | 8005 | walking_pet_order | 订单服务 |

### 技术栈

- **Spring Boot**: 2.7.18
- **Spring Cloud**: 2021.0.8
- **Spring Cloud Alibaba**: 2021.0.5.0
- **Nacos**: 2.2.0 (服务注册与配置中心)
- **MyBatis Plus**: 3.5.3.1
- **MySQL**: 8.0
- **JWT**: 0.11.5

## 🚀 本地环境配置

### 1. 安装必要软件

#### 1.1 安装 JDK 8+
```bash
# 检查Java版本
java -version
```

#### 1.2 安装 Maven 3.6+
```bash
# 检查Maven版本
mvn -version
```

#### 1.3 安装 MySQL 8.0
下载地址: https://dev.mysql.com/downloads/mysql/

**安装后配置：**
- 端口: 3306
- 用户名: root
- 密码: 123456 (或者修改各服务的application.yml中的密码)

#### 1.4 下载并启动 Nacos
下载地址: https://github.com/alibaba/nacos/releases/tag/2.2.0

**Windows启动：**
```bash
cd nacos/bin
startup.cmd -m standalone
```

**Linux/Mac启动：**
```bash
cd nacos/bin
sh startup.sh -m standalone
```

启动成功后访问: http://localhost:8848/nacos
- 默认用户名: nacos
- 默认密码: nacos

---

## 💾 数据库初始化

### 步骤1: 创建数据库

在MySQL中执行 `sql/init.sql` 脚本：

```bash
# 方式1: 使用MySQL命令行
mysql -uroot -p123456 < sql/init.sql

# 方式2: 使用MySQL Workbench或其他客户端
# 打开 sql/init.sql 文件并执行
```

### 步骤2: 验证数据库

登录MySQL，检查数据库是否创建成功：

```sql
SHOW DATABASES;
```

应该能看到以下数据库：
- walking_pet_user
- walking_pet_pet
- walking_pet_bulletin
- walking_pet_sitter
- walking_pet_order

---

## 🔧 项目构建

### 1. 修改配置文件

如果MySQL密码不是 `123456`，需要修改以下文件中的数据库密码：

- `walking-pet-user-service/src/main/resources/application.yml`
- `walking-pet-pet-service/src/main/resources/application.yml`
- `walking-pet-order-service/src/main/resources/application.yml`

**修改位置：**
```yaml
spring:
  datasource:
    password: 你的密码  # 修改这里
```

### 2. Maven构建

在项目根目录执行：

```bash
# 清理并编译
mvn clean install

# 如果遇到测试失败，可以跳过测试
mvn clean install -DskipTests
```

---

## ▶️ 启动服务

### 启动顺序

**必须按以下顺序启动：**

1. **启动 Nacos** (必须先启动)
2. **启动 Gateway** (网关)
3. **启动各个微服务** (顺序不限)

### 方式1: IDE启动 (推荐开发时使用)

使用 IntelliJ IDEA 或 Eclipse 依次运行以下Main类：

1. `GatewayApplication` (8000端口)
2. `UserServiceApplication` (8001端口)
3. `PetServiceApplication` (8002端口)
4. `OrderServiceApplication` (8005端口)

### 方式2: Maven命令启动

打开多个终端窗口，分别执行：

```bash
# 终端1 - 启动Gateway
cd walking-pet-gateway
mvn spring-boot:run

# 终端2 - 启动User Service
cd walking-pet-user-service
mvn spring-boot:run

# 终端3 - 启动Pet Service
cd walking-pet-pet-service
mvn spring-boot:run

# 终端4 - 启动Order Service
cd walking-pet-order-service
mvn spring-boot:run
```

### 方式3: JAR包启动

先构建JAR包：
```bash
mvn clean package -DskipTests
```

然后启动：
```bash
java -jar walking-pet-gateway/target/walking-pet-gateway-1.0.0.jar
java -jar walking-pet-user-service/target/walking-pet-user-service-1.0.0.jar
java -jar walking-pet-pet-service/target/walking-pet-pet-service-1.0.0.jar
java -jar walking-pet-order-service/target/walking-pet-order-service-1.0.0.jar
```

---

## ✅ 验证服务启动

### 1. 检查Nacos服务注册

访问 Nacos 控制台: http://localhost:8848/nacos

在 **服务管理 -> 服务列表** 中应该能看到：
- gateway-service
- user-service
- pet-service
- order-service

### 2. 测试API

#### 测试用户注册
```bash
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "confirmPassword": "123456"
  }'
```

#### 测试用户登录
```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

**成功响应示例：**
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

#### 测试需要认证的接口

使用登录返回的token测试宠物接口：
```bash
curl -X GET http://localhost:8000/api/pets \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## 🔗 前端对接

### 修改前端配置

前端项目需要将API请求地址改为网关地址：

```javascript
// 前端配置
const API_BASE_URL = 'http://localhost:8000';

// 所有API请求都通过网关
axios.defaults.baseURL = API_BASE_URL;

// 添加Token拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

### API路由映射

| 前端路径 | 网关地址 | 后端服务 |
|---------|---------|---------|
| /api/auth/** | http://localhost:8000/api/auth/** | user-service |
| /api/pets/** | http://localhost:8000/api/pets/** | pet-service |
| /api/orders/** | http://localhost:8000/api/orders/** | order-service |

---

## 📝 开发测试账号

数据库初始化脚本中已包含测试账号：

| 用户名 | 密码 | 用户ID |
|-------|------|--------|
| admin | 123456 | u-1000 |
| test | 123456 | u-1001 |

---

## 🐛 常见问题

### 1. Nacos连接失败
**错误:** `unable to connect to nacos server`

**解决:** 确保Nacos已启动，检查端口8848是否被占用

### 2. 数据库连接失败
**错误:** `Communications link failure`

**解决:** 
- 检查MySQL是否启动
- 检查用户名密码是否正确
- 检查数据库是否已创建

### 3. 端口被占用
**错误:** `Port 8000 was already in use`

**解决:** 
- 关闭占用端口的程序
- 或修改 application.yml 中的端口号

### 4. Maven依赖下载慢
**解决:** 配置阿里云Maven镜像

在 `~/.m2/settings.xml` 中添加：
```xml
<mirror>
  <id>aliyunmaven</id>
  <mirrorOf>*</mirrorOf>
  <name>阿里云公共仓库</name>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

---

## 📚 项目结构

```
backend/
├── pom.xml                              # 父POM
├── sql/
│   └── init.sql                         # 数据库初始化脚本
├── walking-pet-common/                  # 公共模块
│   ├── common-core/                     # 核心工具类
│   │   └── src/main/java/
│   │       └── com/walkingpet/common/core/
│   │           ├── domain/Result.java   # 统一响应
│   │           └── exception/           # 异常处理
│   └── common-security/                 # 安全模块
│       └── src/main/java/
│           └── com/walkingpet/common/security/
│               └── utils/
│                   ├── JwtUtil.java     # JWT工具
│                   └── PasswordUtil.java # 密码加密
├── walking-pet-gateway/                 # API网关
│   └── src/main/java/
│       └── com/walkingpet/gateway/
│           ├── GatewayApplication.java
│           └── filter/AuthFilter.java   # 鉴权过滤器
├── walking-pet-user-service/            # 用户服务
│   └── src/main/java/
│       └── com/walkingpet/user/
│           ├── controller/              # 控制层
│           ├── service/                 # 服务层
│           ├── mapper/                  # 数据访问层
│           ├── entity/                  # 实体类
│           └── dto/                     # 数据传输对象
├── walking-pet-pet-service/             # 宠物服务
├── walking-pet-order-service/           # 订单服务
└── README.md                            # 本文件
```

---

## 📞 技术支持

如果遇到问题：
1. 检查日志输出
2. 确认所有依赖服务已启动
3. 查看Nacos控制台服务注册情况
4. 使用Postman测试API接口

---

## 🎯 后续扩展

当前项目已实现核心功能，后续可以扩展：

1. **宠托师服务** (sitter-service) - 宠托师入驻、查询
2. **公告服务** (bulletin-service) - 喂溜公告发布、接单
3. **配置中心** - 使用Nacos Config统一管理配置
4. **服务监控** - 集成Spring Boot Admin
5. **限流降级** - 集成Sentinel
6. **分布式追踪** - 集成Zipkin/Skywalking

---

## ✨ 项目特点

- ✅ 微服务架构，服务独立部署
- ✅ 统一网关，集中鉴权和路由
- ✅ JWT Token认证
- ✅ MyBatis Plus简化开发
- ✅ 统一异常处理
- ✅ 统一响应格式
- ✅ 服务注册与发现
- ✅ 负载均衡
