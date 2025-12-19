@echo off
echo ========================================
echo Walking Pet Backend 启动脚本
echo ========================================
echo.

echo [1/4] 检查Nacos是否启动...
curl -s http://192.168.163.1:8080/index.html > nul
if %errorlevel% neq 0 (
    echo [ERROR] Nacos未启动！请先启动Nacos
    echo 启动命令: cd nacos/bin ^&^& startup.cmd -m standalone
    pause
    exit /b 1
)
echo [OK] Nacos已启动

echo.
echo [2/4] 检查MySQL是否启动...
mysql -uroot -p1234 -e "SELECT 1" > nul 2>&1
if %errorlevel% neq 0 (
    echo [WARNING] MySQL连接失败，请确认MySQL已启动且密码正确
    echo 继续启动服务...
)
echo [OK] MySQL检查完成

echo.
echo [3/4] 编译项目...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] 编译失败！
    pause
    exit /b 1
)
echo [OK] 编译成功

echo.
echo [4/4] 启动微服务...
echo.

echo 启动 Gateway (8000)...
start "Gateway" cmd /k "cd walking-pet-gateway && mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo 启动 User Service (8001)...
start "User Service" cmd /k "cd walking-pet-user-service && mvn spring-boot:run"
timeout /t 5 /nobreak > nul

echo 启动 Pet Service (8002)...
start "Pet Service" cmd /k "cd walking-pet-pet-service && mvn spring-boot:run"
timeout /t 5 /nobreak > nul

echo 启动 Bulletin Service (8003)...
start "Bulletin Service" cmd /k "cd walking-pet-bulletin-service && mvn spring-boot:run"
timeout /t 5 /nobreak > nul

echo 启动 Sitter Service (8004)...
start "Sitter Service" cmd /k "cd walking-pet-sitter-service && mvn spring-boot:run"
timeout /t 5 /nobreak > nul

echo 启动 Order Service (8005)...
start "Order Service" cmd /k "cd walking-pet-order-service && mvn spring-boot:run"

echo.
echo ========================================
echo 所有服务启动中，请等待40秒...
echo 访问 Nacos: http://192.168.163.1:8080/index.html
echo 网关地址: http://localhost:8000
echo.
echo 已启动的服务:
echo - Gateway (8000)
echo - User Service (8001)
echo - Pet Service (8002)
echo - Bulletin Service (8003)
echo - Sitter Service (8004)
echo - Order Service (8005)
echo ========================================
pause
