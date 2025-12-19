## 用户注册

**接口名称：** 用户注册  
**功能描述：** 新用户注册账户，创建用户信息。  
**接口地址：** /api/auth/register  
**请求方式：** POST

### 功能说明
- 校验用户名唯一性。
- 校验密码长度（至少6位）。
- 创建新用户账户。

### 请求参数
```json
{
  "username": "zhangsan",
  "password": "123456",
  "confirm_password": "123456"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| username | string | 是 | 用户名（至少3位，不能重复） | zhangsan |
| password | string | 是 | 密码（至少6位） | 123456 |
| confirm_password | string | 是 | 确认密码（需与password一致） | 123456 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "user_id": "u-1001",
    "username": "zhangsan"
  },
  "message": "注册成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码（0表示成功，非0表示失败） | 0 |
| body | object | 是 | 响应数据 | |
| body.user_id | string | 是 | 用户ID | u-1001 |
| body.username | string | 是 | 用户名 | zhangsan |
| message | string | 是 | 返回信息 | 注册成功 |
| success | bool | 是 | 是否成功 | true |

---

## 用户登录

**接口名称：** 用户登录  
**功能描述：** 用户使用用户名和密码登录系统。  
**接口地址：** /api/auth/login  
**请求方式：** POST

### 功能说明
- 验证用户名和密码。
- 登录成功后返回用户信息。
- 前端保存登录状态。

### 请求参数
```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| username | string | 是 | 用户名 | zhangsan |
| password | string | 是 | 密码 | 123456 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "user_id": "u-1001",
    "username": "zhangsan",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "message": "登录成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码（0表示成功，非0表示失败） | 0 |
| body | object | 是 | 响应数据 | |
| body.user_id | string | 是 | 用户ID | u-1001 |
| body.username | string | 是 | 用户名 | zhangsan |
| body.token | string | 否 | 登录令牌（可选，用于后续请求鉴权） | eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9... |
| message | string | 是 | 返回信息 | 登录成功 |
| success | bool | 是 | 是否成功 | true |

---

## 用户登出

**接口名称：** 用户登出  
**功能描述：** 用户退出登录，清除登录状态。  
**接口地址：** /api/auth/logout  
**请求方式：** POST

### 功能说明
- 清除用户登录状态。
- 清除相关缓存信息。

### 请求参数
无需请求参数（需携带token或session）

### 响应参数
```json
{
  "error": 0,
  "body": null,
  "message": "登出成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 否 | 响应数据 | null |
| message | string | 是 | 返回信息 | 登出成功 |
| success | bool | 是 | 是否成功 | true |

---

## 创建喂溜公告

**接口名称：** 创建喂溜公告  
**功能描述：** 用户填写上门喂猫/遛狗信息，生成一条待接单的喂溜公告。  
**接口地址：** /api/bulletins  
**请求方式：** POST

### 功能说明
- 校验服务类型、地址、时间等必填信息。
- 创建公告并返回公告详情，初始状态为“待接单”。

### 请求参数
```json
{
  "service_type": "feed-cat",
  "address": "和平里小区 5 号楼 2 单元",
  "pet_id": "pet-001",
  "pet_name": "奶糖",
  "pet_type": "英短",
  "service_time": "2025-12-10 18:30",
  "walker_gender": "不限",
  "remark": "换水、湿粮 60g，陪玩 15 分钟，检查猫砂。"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| service_type | string | 是 | 服务类型：`feed-cat`（喂猫）或 `walk-dog`（遛狗） | feed-cat |
| address | string | 是 | 上门地址 | 和平里小区 5 号楼 2 单元 |
| pet_id | string | 否 | 宠物档案ID（从宠物档案中选择） | pet-001 |
| pet_name | string | 否 | 宠物昵称 | 奶糖 |
| pet_type | string | 否 | 宠物品种/特点 | 英短 |
| service_time | string | 是 | 服务时间（本地时间，yyyy-MM-dd HH:mm） | 2025-12-10 18:30 |
| walker_gender | string | 否 | 宠托师性别偏好：`不限`/`男生`/`女生` | 不限 |
| remark | string | 否 | 备注说明 | 换水、湿粮 60g，陪玩 15 分钟 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "b-001",
    "status": "待接单",
    "service_type": "feed-cat",
    "title": "上门喂猫 · 奶糖",
    "address": "和平里小区 5 号楼 2 单元",
    "pet_id": "pet-001",
    "pet_name": "奶糖",
    "pet_type": "英短",
    "service_time": "2025-12-10 18:30",
    "walker_gender": "不限",
    "remark": "换水、湿粮 60g，陪玩 15 分钟"
  },
  "message": "创建成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 公告详情 | |
| body.id | string | 是 | 公告唯一 ID | b-001 |
| body.status | string | 是 | 公告状态：待接单/已接单 | 待接单 |
| body.service_type | string | 是 | 服务类型 | feed-cat |
| body.title | string | 是 | 公告标题 | 上门喂猫 · 奶糖 |
| body.address | string | 是 | 上门地址 | 和平里小区 5 号楼 2 单元 |
| body.pet_id | string | 否 | 宠物档案ID | pet-001 |
| body.pet_name | string | 否 | 宠物昵称 | 奶糖 |
| body.pet_type | string | 否 | 宠物品种/特点 | 英短 |
| body.service_time | string | 是 | 服务时间 | 2025-12-10 18:30 |
| body.walker_gender | string | 否 | 性别偏好 | 不限 |
| body.remark | string | 否 | 备注 | 换水、湿粮 60g，陪玩 15 分钟 |
| message | string | 是 | 返回信息 | 创建成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取喂溜公告列表

**接口名称：** 获取喂溜公告列表  
**功能描述：** 分页返回喂溜公告，支持按服务类型筛选。  
**接口地址：** /api/bulletins  
**请求方式：** GET

### 功能说明
- 可按服务类型（喂猫/遛狗）筛选。
- 支持分页。

### 请求参数
```json
{
  "page": 1,
  "page_size": 10,
  "service_type": "walk-dog"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| page | int | 否 | 页码（默认 1） | 1 |
| page_size | int | 否 | 每页数量（默认 10） | 10 |
| service_type | string | 否 | 过滤：`feed-cat`/`walk-dog` | walk-dog |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "list": [
      {
        "id": "b-002",
        "title": "晚间遛狗 30 分钟",
        "status": "待接单",
        "service_type": "walk-dog",
        "address": "金辉府南门集合",
        "service_time": "2025-12-10 20:00",
        "walker_gender": "女生"
      }
    ],
    "page": 1,
    "page_size": 10,
    "total": 36
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.list | array | 是 | 公告列表 | |
| body.list[].id | string | 是 | 公告 ID | b-002 |
| body.list[].title | string | 是 | 公告标题 | 晚间遛狗 30 分钟 |
| body.list[].status | string | 是 | 状态 | 待接单 |
| body.list[].service_type | string | 是 | 服务类型 | walk-dog |
| body.list[].address | string | 是 | 地址 | 金辉府南门集合 |
| body.list[].service_time | string | 是 | 服务时间 | 2025-12-10 20:00 |
| body.list[].walker_gender | string | 否 | 性别偏好 | 女生 |
| body.page | int | 是 | 当前页 | 1 |
| body.page_size | int | 是 | 每页数量 | 10 |
| body.total | int | 是 | 总条数 | 36 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取喂溜公告详情

**接口名称：** 获取喂溜公告详情  
**功能描述：** 返回单条喂溜公告的完整信息。  
**接口地址：** /api/bulletins/{id}  
**请求方式：** GET

### 功能说明
- 按公告 ID 查询详情。

### 请求参数
```json
{
  "id": "b-001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 公告 ID | b-001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "b-001",
    "status": "待接单",
    "service_type": "feed-cat",
    "title": "上门喂猫 · 奶糖",
    "address": "和平里小区 5 号楼 2 单元",
    "pet_id": "pet-001",
    "pet_name": "奶糖",
    "pet_type": "英短",
    "service_time": "2025-12-10 18:30",
    "walker_gender": "不限",
    "remark": "换水、湿粮 60g，陪玩 15 分钟",
    "distance": "2.3km",
    "created_at": "2025-12-10 10:00"
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 公告详情 | |
| body.id | string | 是 | 公告 ID | b-001 |
| body.status | string | 是 | 状态 | 待接单 |
| body.service_type | string | 是 | 服务类型 | feed-cat |
| body.title | string | 是 | 标题 | 上门喂猫 · 奶糖 |
| body.address | string | 是 | 地址 | 和平里小区 5 号楼 2 单元 |
| body.pet_id | string | 否 | 宠物档案ID | pet-001 |
| body.pet_name | string | 否 | 宠物昵称 | 奶糖 |
| body.pet_type | string | 否 | 品种 | 英短 |
| body.service_time | string | 是 | 服务时间 | 2025-12-10 18:30 |
| body.walker_gender | string | 否 | 性别偏好 | 不限 |
| body.remark | string | 否 | 备注 | 换水、湿粮 60g |
| body.distance | string | 否 | 距离 | 2.3km |
| body.created_at | string | 否 | 创建时间 | 2025-12-10 10:00 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 宠托师接单

**接口名称：** 宠托师接单  
**功能描述：** 宠托师对喂溜公告进行接单。  
**接口地址：** /api/bulletins/{id}/accept  
**请求方式：** POST

### 功能说明
- 将公告状态更新为“已接单”，记录接单人信息（需鉴权）。
- 接单成功后自动在个人中心生成订单记录，用户可在订单列表查看。

### 请求参数
```json
{
  "id": "b-001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 公告 ID | b-001 |
| sitter_id | string | 是 | 接单宠托师 ID（必须已入驻） | s-001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "b-001",
    "status": "已接单"
  },
  "message": "接单成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.id | string | 是 | 公告 ID | b-001 |
| body.status | string | 是 | 更新后的状态 | 已接单 |
| body.sitter_id | string | 是 | 接单宠托师 ID | s-001 |
| message | string | 是 | 返回信息 | 接单成功 |
| success | bool | 是 | 是否成功 | true |

**说明：** 接单后会自动在订单列表生成订单记录，状态为"已接单"，订单包含公告的所有信息。宠托师接单和主人下单的区别：宠托师从公告接单时，订单状态直接为"已接单"，可立即完成订单；主人选择宠托师下单时，订单状态为"待宠托师接单"，需等待宠托师接单后才变为"已接单"。

---

## 完成订单

**接口名称：** 完成订单  
**功能描述：** 宠托师完成订单后填写完成内容和上传宠物图片。  
**接口地址：** /api/orders/{id}/complete  
**请求方式：** POST

### 功能说明
- 宠托师对已接单的订单进行完成操作。
- 需填写简短的完成内容描述服务情况。
- 需上传宠物图片（base64格式）。
- 订单状态更新为"已完成"。
- **注意：**只有状态为"已接单"的订单才能执行完成操作，"待宠托师接单"的订单需先接单。

### 请求参数
```json
{
  "order_id": "o-12345",
  "complete_content": "已按时完成喂食，猫咪状态良好，换了新鲜的水，陪玩半小时。",
  "complete_image": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD..."
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| order_id | string | 是 | 订单ID | o-12345 |
| complete_content | string | 是 | 完成内容描述（简短描述服务完成情况） | 已按时完成喂食，猫咪状态良好 |
| complete_image | string | 是 | 完成图片（base64格式或图片URL） | data:image/jpeg;base64,/9j/4AAQ... |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "order_id": "o-12345",
    "status": "已完成",
    "completed_at": "2024-01-15 16:30:00"
  },
  "message": "订单已完成",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.order_id | string | 是 | 订单ID | o-12345 |
| body.status | string | 是 | 更新后的状态 | 已完成 |
| body.completed_at | string | 是 | 完成时间 | 2024-01-15 16:30:00 |
| message | string | 是 | 返回信息 | 订单已完成 |
| success | bool | 是 | 是否成功 | true |

---

## 获取宠托师列表

**接口名称：** 获取宠托师列表  
**功能描述：** 按分页返回附近宠托师，支持按服务类型或性别偏好筛选。  
**接口地址：** /api/sitters  
**请求方式：** GET

### 功能说明
- 支持分页。
- 可按服务类型（喂猫/遛狗）或性别偏好筛选。

### 请求参数
```json
{
  "page": 1,
  "page_size": 10,
  "service_type": "walk-dog",
  "gender": "女生"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| page | int | 否 | 页码（默认 1） | 1 |
| page_size | int | 否 | 每页数量（默认 10） | 10 |
| service_type | string | 否 | 过滤：`feed-cat`/`walk-dog` | walk-dog |
| gender | string | 否 | 性别过滤：`男生`/`女生` | 女生 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "list": [
      {
        "id": "s-001",
        "name": "周小暖",
        "gender": "女生",
        "distance": "1.2km",
        "rating": 4.9,
        "orders": 126,
        "tags": ["猫咪经验丰富", "擅长服药"]
      }
    ],
    "page": 1,
    "page_size": 10,
    "total": 20
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.list | array | 是 | 宠托师列表 | |
| body.list[].id | string | 是 | 宠托师 ID | s-001 |
| body.list[].name | string | 是 | 昵称 | 周小暖 |
| body.list[].gender | string | 是 | 性别 | 女生 |
| body.list[].distance | string | 是 | 距离 | 1.2km |
| body.list[].rating | number | 是 | 评分 | 4.9 |
| body.list[].orders | int | 是 | 服务单量 | 126 |
| body.list[].tags | array | 否 | 标签 | ["猫咪经验丰富"] |
| body.page | int | 是 | 当前页 | 1 |
| body.page_size | int | 是 | 每页数量 | 10 |
| body.total | int | 是 | 总条数 | 20 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取宠托师详情

**接口名称：** 获取宠托师详情  
**功能描述：** 返回单个宠托师的完整信息与服务列表。  
**接口地址：** /api/sitters/{id}  
**请求方式：** GET

### 功能说明
- 按宠托师 ID 查询详细信息。

### 请求参数
```json
{
  "id": "s-001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 宠托师 ID | s-001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "s-001",
    "name": "周小暖",
    "gender": "女生",
    "distance": "1.2km",
    "rating": 4.9,
    "orders": 126,
    "slogan": "猫咪行为学初级，温柔陪伴，擅长安抚胆小猫。",
    "tags": ["猫咪经验丰富", "擅长服药"],
    "pets": [
      { "name": "团子", "desc": "英短蓝白", "cover": "https://img" }
    ],
    "services": [
      { "type": "feed-cat", "title": "上门喂猫", "price": "￥58/次", "duration": "30分钟" },
      { "type": "walk-dog", "title": "上门遛狗", "price": "￥68/次", "duration": "30分钟" }
    ]
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 宠托师详情 | |
| body.id | string | 是 | 宠托师 ID | s-001 |
| body.name | string | 是 | 昵称 | 周小暖 |
| body.gender | string | 是 | 性别 | 女生 |
| body.distance | string | 否 | 距离 | 1.2km |
| body.rating | number | 否 | 评分 | 4.9 |
| body.orders | int | 否 | 服务单量 | 126 |
| body.slogan | string | 否 | 个人简介 | 猫咪行为学初级... |
| body.tags | array | 否 | 标签 | ["猫咪经验丰富"] |
| body.pets | array | 否 | 养宠展示 | |
| body.pets[].name | string | 是 | 宠物名 | 团子 |
| body.pets[].desc | string | 否 | 简介 | 英短蓝白 |
| body.pets[].cover | string | 否 | 图片/封面 | https://img |
| body.services | array | 是 | 服务列表 | |
| body.services[].type | string | 是 | 服务类型 | feed-cat |
| body.services[].title | string | 是 | 服务名称 | 上门喂猫 |
| body.services[].price | string | 是 | 价格 | ￥58/次 |
| body.services[].duration | string | 是 | 时长 | 30分钟 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 创建宠托师订单

**接口名称：** 创建宠托师订单  
**功能描述：** 用户在指定宠托师详情页选择服务并下单，订单需宠托师接单后生效。  
**接口地址：** /api/sitters/{id}/orders  
**请求方式：** POST

### 功能说明
- 绑定宠托师 ID。
- 创建订单，状态初始为“待宠托师接单”。
- 需要宠托师接单后，订单状态才变为“已接单”，然后宠托师才能完成订单。

### 请求参数
```json
{
  "service_type": "feed-cat",
  "service_time": "2025-12-11 19:00",
  "address": "和平里小区 5 号楼 2 单元",
  "pet_id": "pet-001",
  "remark": "喂湿粮 60g，陪玩 10 分钟"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| service_type | string | 是 | 服务类型：`feed-cat`/`walk-dog` | feed-cat |
| service_time | string | 是 | 服务时间（yyyy-MM-dd HH:mm） | 2025-12-11 19:00 |
| address | string | 是 | 上门地址 | 和平里小区 5 号楼 2 单元 |
| pet_id | string | 否 | 宠物档案ID（从宠物档案中选择） | pet-001 |
| remark | string | 否 | 备注 | 喂湿粮 60g |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "o-1001",
    "sitter_id": "s-001",
    "status": "待宠托师接单",
    "service_type": "feed-cat",
    "service_time": "2025-12-11 19:00",
    "address": "和平里小区 5 号楼 2 单元",
    "pet_id": "pet-001",
    "remark": "喂湿粮 60g"
  },
  "message": "创建成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 订单详情 | |
| body.id | string | 是 | 订单 ID | o-1001 |
| body.sitter_id | string | 是 | 宠托师 ID | s-001 |
| body.status | string | 是 | 状态：待宠托师接单/已接单 | 待宠托师接单 |
| body.service_type | string | 是 | 服务类型 | feed-cat |
| body.service_time | string | 是 | 服务时间 | 2025-12-11 19:00 |
| body.address | string | 是 | 上门地址 | 和平里小区 5 号楼 2 单元 |
| body.pet_id | string | 否 | 宠物档案ID | pet-001 |
| body.walker_gender | string | 否 | 性别偏好 | 不限 |
| body.remark | string | 否 | 备注 | 喂湿粮 60g |
| message | string | 是 | 返回信息 | 创建成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取订单详情

**接口名称：** 获取订单详情  
**功能描述：** 返回单个订单的完整信息，用于个人中心查看。  
**接口地址：** /api/orders/{id}  
**请求方式：** GET

### 功能说明
- 按订单 ID 查询详情。

### 请求参数
```json
{
  "id": "o-1001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 订单 ID | o-1001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "o-1001",
    "sitter_id": "s-001",
    "sitter_name": "周小暖",
    "status": "待宠托师接单",
    "service_type": "feed-cat",
    "service_time": "2025-12-11 19:00",
    "address": "和平里小区 5 号楼 2 单元",
    "remark": "喂湿粮 60g",
    "created_at": "2025-12-11 15:00"
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 订单详情 | |
| body.id | string | 是 | 订单 ID | o-1001 |
| body.sitter_id | string | 是 | 宠托师 ID | s-001 |
| body.sitter_name | string | 否 | 宠托师姓名 | 周小暖 |
| body.status | string | 是 | 状态 | 待宠托师接单 |
| body.service_type | string | 是 | 服务类型 | feed-cat |
| body.service_time | string | 是 | 服务时间 | 2025-12-11 19:00 |
| body.address | string | 是 | 上门地址 | 和平里小区 5 号楼 2 单元 |
| body.remark | string | 否 | 备注 | 喂湿粮 60g |
| body.created_at | string | 否 | 创建时间 | 2025-12-11 15:00 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 宠托师订单接单

**接口名称：** 宠托师订单接单  
**功能描述：** 宠托师对用户指派的订单进行接单。  
**接口地址：** /api/orders/{id}/accept  
**请求方式：** POST

### 功能说明
- 将订单状态更新为“已接单”，记录接单信息（需鉴权）。

### 请求参数
```json
{
  "id": "o-1001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 订单 ID | o-1001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "o-1001",
    "status": "已接单"
  },
  "message": "接单成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.id | string | 是 | 订单 ID | o-1001 |
| body.status | string | 是 | 更新后的状态 | 已接单 |
| message | string | 是 | 返回信息 | 接单成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取订单列表

**接口名称：** 获取订单列表  
**功能描述：** 查询当前用户的订单列表，支持分页和按状态筛选。  
**接口地址：** /api/orders  
**请求方式：** GET

### 功能说明
- 返回当前用户的所有订单。
- 可按状态过滤（待宠托师接单/已接单）。

### 请求参数
```json
{
  "page": 1,
  "page_size": 10,
  "status": "待宠托师接单"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| page | int | 否 | 页码（默认 1） | 1 |
| page_size | int | 否 | 每页数量（默认 10） | 10 |
| status | string | 否 | 过滤状态：`待宠托师接单`/`已接单` | 待宠托师接单 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "list": [
      {
        "id": "o-1001",
        "sitter_name": "周小暖",
        "status": "待宠托师接单",
        "service_type": "feed-cat",
        "service_time": "2025-12-11 19:00",
        "address": "和平里小区 5 号楼 2 单元"
      }
    ],
    "page": 1,
    "page_size": 10,
    "total": 6
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.list | array | 是 | 订单列表 | |
| body.list[].id | string | 是 | 订单 ID | o-1001 |
| body.list[].sitter_name | string | 否 | 宠托师姓名 | 周小暖 |
| body.list[].status | string | 是 | 状态 | 待宠托师接单 |
| body.list[].service_type | string | 是 | 服务类型 | feed-cat |
| body.list[].service_time | string | 是 | 服务时间 | 2025-12-11 19:00 |
| body.list[].address | string | 是 | 上门地址 | 和平里小区 5 号楼 2 单元 |
| body.page | int | 是 | 当前页 | 1 |
| body.page_size | int | 是 | 每页数量 | 10 |
| body.total | int | 是 | 总条数 | 6 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取宠物档案列表

**接口名称：** 获取宠物档案列表  
**功能描述：** 返回当前用户的宠物档案，支持分页。  
**接口地址：** /api/pets  
**请求方式：** GET

### 功能说明
- 仅返回当前用户自己的宠物档案。

### 请求参数
```json
{
  "page": 1,
  "page_size": 10
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| page | int | 否 | 页码（默认 1） | 1 |
| page_size | int | 否 | 每页数量（默认 10） | 10 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "list": [
      {
        "id": "pet-001",
        "type": "猫",
        "name": "奶糖",
        "age": "1岁2个月",
        "gender": "母",
        "weight": "4.1kg",
        "breed": "英短蓝白",
        "aggressive": false,
        "rabies_vaccine": true,
        "intro": "性格温柔，喜欢逗猫棒",
        "image": "https://..."
      }
    ],
    "page": 1,
    "page_size": 10,
    "total": 2
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.list | array | 是 | 宠物档案列表 | |
| body.list[].id | string | 是 | 宠物 ID | pet-001 |
| body.list[].type | string | 是 | 类型 | 猫 |
| body.list[].name | string | 是 | 名字 | 奶糖 |
| body.list[].age | string | 否 | 年龄 | 1岁2个月 |
| body.list[].gender | string | 否 | 性别 | 母 |
| body.list[].weight | string | 否 | 体重 | 4.1kg |
| body.list[].breed | string | 否 | 品种 | 英短蓝白 |
| body.list[].aggressive | bool | 是 | 是否具有攻击性 | false |
| body.list[].rabies_vaccine | bool | 是 | 是否接种狂犬疫苗 | true |
| body.list[].intro | string | 否 | 简介 | 性格温柔 |
| body.list[].image | string | 否 | 图片链接 | https://... |
| body.page | int | 是 | 当前页 | 1 |
| body.page_size | int | 是 | 每页数量 | 10 |
| body.total | int | 是 | 总条数 | 2 |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 获取宠物档案详情

**接口名称：** 获取宠物档案详情  
**功能描述：** 返回单个宠物档案的详细信息。  
**接口地址：** /api/pets/{id}  
**请求方式：** GET

### 功能说明
- 按宠物 ID 查询当前用户的宠物档案。

### 请求参数
```json
{
  "id": "pet-001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 宠物 ID | pet-001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "pet-001",
    "type": "猫",
    "name": "奶糖",
    "age": "1岁2个月",
    "gender": "母",
    "weight": "4.1kg",
    "breed": "英短蓝白",
    "aggressive": false,
    "rabies_vaccine": true,
    "intro": "性格温柔，喜欢逗猫棒",
    "image": "https://..."
  },
  "message": "获取成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 宠物详情 | |
| body.id | string | 是 | 宠物 ID | pet-001 |
| body.type | string | 是 | 类型 | 猫 |
| body.name | string | 是 | 名字 | 奶糖 |
| body.age | string | 否 | 年龄 | 1岁2个月 |
| body.gender | string | 否 | 性别 | 母 |
| body.weight | string | 否 | 体重 | 4.1kg |
| body.breed | string | 否 | 品种 | 英短蓝白 |
| body.aggressive | bool | 是 | 是否具有攻击性 | false |
| body.rabies_vaccine | bool | 是 | 是否接种狂犬疫苗 | true |
| body.intro | string | 否 | 简介 | 性格温柔 |
| body.image | string | 否 | 图片链接 | https://... |
| message | string | 是 | 返回信息 | 获取成功 |
| success | bool | 是 | 是否成功 | true |

---

## 创建宠物档案

**接口名称：** 创建宠物档案  
**功能描述：** 为当前用户新增宠物档案。  
**接口地址：** /api/pets  
**请求方式：** POST

### 功能说明
- 校验必填字段，写入用户名下。

### 请求参数
```json
{
  "type": "猫",
  "name": "奶糖",
  "age": "1岁2个月",
  "gender": "母",
  "weight": "4.1kg",
  "breed": "英短蓝白",
  "aggressive": false,
  "rabies_vaccine": true,
  "intro": "性格温柔，喜欢逗猫棒",
  "image": "base64_encoded_image_data"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| type | string | 是 | 类型：猫/狗/其他 | 猫 |
| name | string | 是 | 名字 | 奶糖 |
| age | string | 否 | 年龄 | 1岁2个月 |
| gender | string | 否 | 性别 | 母 |
| weight | string | 否 | 体重 | 4.1kg |
| breed | string | 否 | 品种 | 英短蓝白 |
| aggressive | bool | 是 | 是否具有攻击性 | false |
| rabies_vaccine | bool | 是 | 是否接种狂犬疫苗 | true |
| intro | string | 否 | 简介 | 性格温柔 |
| image | string | 否 | 图片（base64或本地上传） | base64_encoded_image_data |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "pet-1001",
    "type": "猫",
    "name": "奶糖",
    "age": "1岁2个月",
    "gender": "母",
    "weight": "4.1kg",
    "breed": "英短蓝白",
    "aggressive": false,
    "rabies_vaccine": true,
    "intro": "性格温柔，喜欢逗猫棒",
    "image": "base64_encoded_image_data"
  },
  "message": "创建成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 宠物详情 | |
| body.id | string | 是 | 宠物 ID | pet-1001 |
| body.type | string | 是 | 类型 | 猫 |
| body.name | string | 是 | 名字 | 奶糖 |
| body.age | string | 否 | 年龄 | 1岁2个月 |
| body.gender | string | 否 | 性别 | 母 |
| body.weight | string | 否 | 体重 | 4.1kg |
| body.breed | string | 否 | 品种 | 英短蓝白 |
| body.aggressive | bool | 是 | 是否具有攻击性 | false |
| body.rabies_vaccine | bool | 是 | 是否接种狂犬疫苗 | true |
| body.intro | string | 否 | 简介 | 性格温柔 |
| body.image | string | 否 | 图片 | base64_encoded_image_data |
| message | string | 是 | 返回信息 | 创建成功 |
| success | bool | 是 | 是否成功 | true |

---

## 更新宠物档案

**接口名称：** 更新宠物档案  
**功能描述：** 更新指定宠物档案信息。  
**接口地址：** /api/pets/{id}  
**请求方式：** PUT

### 功能说明
- 校验字段后覆盖更新。

### 请求参数
同“创建宠物档案”，路径参数 `id` 为宠物 ID。

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "pet-1001",
    "status": "updated"
  },
  "message": "更新成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.id | string | 是 | 宠物 ID | pet-1001 |
| body.status | string | 是 | 更新状态 | updated |
| message | string | 是 | 返回信息 | 更新成功 |
| success | bool | 是 | 是否成功 | true |

---

## 删除宠物档案

**接口名称：** 删除宠物档案  
**功能描述：** 删除指定宠物档案。  
**接口地址：** /api/pets/{id}  
**请求方式：** DELETE

### 功能说明
- 删除当前用户名下的指定宠物档案。

### 请求参数
```json
{
  "id": "pet-1001"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| id | string | 是 | 宠物 ID | pet-1001 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "id": "pet-1001",
    "deleted": true
  },
  "message": "删除成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.id | string | 是 | 宠物 ID | pet-1001 |
| body.deleted | bool | 是 | 删除标记 | true |
| message | string | 是 | 返回信息 | 删除成功 |
| success | bool | 是 | 是否成功 | true |

---

## 入驻宠托师

**接口名称：** 入驻宠托师  
**功能描述：** 用户提交个人信息、养宠展示与服务配置，成为可接单的宠托师。  
**接口地址：** /api/sitters/join  
**请求方式：** POST

### 功能说明
- 创建新宠托师档案，赋予接单资格。
- 宠物图片通过本地文件上传，转换为base64格式存储。
- 图片上传后立即在下方显示预览，未上传时不显示预览区域。
- 入驻成功后，宠物图片会在宠托师详情页的"个人简介"模块展示。

### 请求参数
```json
{
  "name": "周小暖",
  "gender": "女生",
  "slogan": "猫咪行为学初级，温柔陪伴",
  "tags": ["猫咪经验丰富", "时间灵活"],
  "pets": [
    { "name": "团子", "desc": "英短蓝白", "cover": "base64_encoded_image_data" }
  ],
  "services": [
    { "type": "feed-cat", "title": "上门喂猫", "price": "￥58/次", "duration": "30分钟" },
    { "type": "walk-dog", "title": "上门遛狗", "price": "￥68/次", "duration": "30分钟" }
  ]
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| name | string | 是 | 姓名/昵称 | 周小暖 |
| gender | string | 是 | 性别 | 女生 |
| slogan | string | 否 | 个人简介 | 猫咪行为学初级... |
| tags | array | 否 | 标签集合 | ["猫咪经验丰富"] |
| pets | array | 否 | 养宠展示 | |
| pets[].name | string | 是 | 宠物名 | 团子 |
| pets[].desc | string | 否 | 简介 | 英短蓝白 |
| pets[].cover | string | 否 | 宠物图片（base64格式，从本地上传转换） | data:image/jpeg;base64,/9j/4AAQ... |
| services | array | 是 | 服务配置 | |
| services[].type | string | 是 | 服务类型：`feed-cat`/`walk-dog` | feed-cat |
| services[].title | string | 是 | 名称 | 上门喂猫 |
| services[].price | string | 是 | 价格 | ￥58/次 |
| services[].duration | string | 是 | 时长 | 30分钟 |

### 响应参数
```json
{
  "error": 0,
  "body": {
    "sitter_id": "s-1001",
    "status": "ready"
  },
  "message": "入驻成功",
  "success": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|-------|------|-----|------|--------|
| error | int | 是 | 错误码 | 0 |
| body | object | 是 | 响应数据 | |
| body.sitter_id | string | 是 | 新建宠托师 ID | s-1001 |
| body.status | string | 是 | 入驻状态 | ready |
| message | string | 是 | 返回信息 | 入驻成功 |
| success | bool | 是 | 是否成功 | true |
