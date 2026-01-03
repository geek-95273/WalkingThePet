#!/bin/bash

echo "=========================================="
echo "Walking Pet 微服务健康检查"
echo "=========================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 等待时间（秒）
WAIT_TIME=5

# 检查服务健康状态的函数
check_service() {
    local service_name=$1
    local url=$2
    local max_retries=30
    local retry=0

    echo -n "检查 $service_name ... "
    
    while [ $retry -lt $max_retries ]; do
        if curl -s -f "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✓ 运行正常${NC}"
            return 0
        fi
        retry=$((retry + 1))
        sleep 2
    done
    
    echo -e "${RED}✗ 服务未响应${NC}"
    return 1
}

# 检查 MySQL
echo "1. 检查 MySQL 数据库..."
if docker exec walking-pet-mysql mysqladmin ping -uroot -p1234 --silent > /dev/null 2>&1; then
    echo -e "${GREEN}✓ MySQL 运行正常${NC}"
else
    echo -e "${RED}✗ MySQL 连接失败${NC}"
    exit 1
fi
echo ""

# 检查 Nacos
echo "2. 检查 Nacos 注册中心..."
check_service "Nacos" "http://localhost:8848/nacos"
echo ""

# 等待服务启动
echo "等待微服务启动..."
sleep $WAIT_TIME
echo ""

# 检查微服务
echo "3. 检查微服务..."
check_service "Gateway (8000)" "http://localhost:8000/actuator/health"
check_service "User Service (8001)" "http://localhost:8001/actuator/health"
check_service "Pet Service (8002)" "http://localhost:8002/actuator/health"
check_service "Bulletin Service (8003)" "http://localhost:8003/actuator/health"
check_service "Sitter Service (8004)" "http://localhost:8004/actuator/health"
check_service "Order Service (8005)" "http://localhost:8005/actuator/health"
echo ""

# 检查 Nacos 服务注册
echo "4. 检查 Nacos 服务注册..."
services=("gateway" "user-service" "pet-service" "bulletin-service" "sitter-service" "order-service")

for service in "${services[@]}"; do
    echo -n "检查 $service 注册状态 ... "
    if curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=$service" | grep -q "\"healthy\":true"; then
        echo -e "${GREEN}✓ 已注册${NC}"
    else
        echo -e "${YELLOW}⚠ 未注册或不健康${NC}"
    fi
done
echo ""

# API 功能测试
echo "5. API 功能测试..."

# 测试注册
echo -n "测试用户注册 ... "
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8000/api/auth/register \
    -H "Content-Type: application/json" \
    -d '{"username":"testuser","password":"123456","confirmPassword":"123456"}')

if echo "$REGISTER_RESPONSE" | grep -q "\"success\":true\|已存在"; then
    echo -e "${GREEN}✓ 通过${NC}"
else
    echo -e "${RED}✗ 失败${NC}"
fi

# 测试登录
echo -n "测试用户登录 ... "
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8000/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"testuser","password":"123456"}')

if echo "$LOGIN_RESPONSE" | grep -q "\"success\":true"; then
    echo -e "${GREEN}✓ 通过${NC}"
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    
    # 测试宠物列表
    if [ ! -z "$TOKEN" ]; then
        echo -n "测试宠物列表 ... "
        PETS_RESPONSE=$(curl -s -X GET http://localhost:8000/api/pets \
            -H "Authorization: Bearer $TOKEN")
        
        if echo "$PETS_RESPONSE" | grep -q "\"success\":true"; then
            echo -e "${GREEN}✓ 通过${NC}"
        else
            echo -e "${RED}✗ 失败${NC}"
        fi
    fi
else
    echo -e "${RED}✗ 失败${NC}"
fi

echo ""
echo "=========================================="
echo "健康检查完成！"
echo "=========================================="
echo ""
echo "访问地址："
echo "  - API 网关: http://localhost:8000"
echo "  - Nacos 控制台: http://localhost:8848/nacos (nacos/nacos)"
echo ""
