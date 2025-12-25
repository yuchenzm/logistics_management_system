#!/bin/bash

echo "🚀 启动物流管理系统..."
echo "================================"

# 检查端口是否被占用
check_port() {
    local port=$1
    local service=$2
    if lsof -i :$port > /dev/null 2>&1; then
        echo "⚠️  端口 $port 已被占用 ($service)"
        return 1
    fi
    return 0
}

# 启动后端服务
start_backend() {
    echo "📡 启动后端服务 (端口: 8080)..."
    if check_port 8080 "后端服务"; then
        cd backend
        mvn spring-boot:run > ../logs/backend.log 2>&1 &
        BACKEND_PID=$!
        echo "   后端服务 PID: $BACKEND_PID"
        cd ..
        sleep 3
    fi
}

# 启动管理员后台
start_admin() {
    echo "🖥️  启动管理员后台 (端口: 3000)..."
    if check_port 3000 "管理员后台"; then
        cd admin-web
        npm run dev > ../logs/admin.log 2>&1 &
        ADMIN_PID=$!
        echo "   管理员后台 PID: $ADMIN_PID"
        cd ..
        sleep 2
    fi
}

# 启动客户端
start_client() {
    echo "👤 启动客户端 (端口: 3001)..."
    if check_port 3001 "客户端"; then
        cd client-web
        npm run dev > ../logs/client.log 2>&1 &
        CLIENT_PID=$!
        echo "   客户端 PID: $CLIENT_PID"
        cd ..
        sleep 2
    fi
}

# 启动用户端
start_user() {
    echo "👥 启动用户端 (端口: 3002)..."
    if check_port 3002 "用户端"; then
        cd user-web
        npm run dev > ../logs/user.log 2>&1 &
        USER_PID=$!
        echo "   用户端 PID: $USER_PID"
        cd ..
        sleep 2
    fi
}

# 创建日志目录
mkdir -p logs

# 清理旧的日志文件
rm -f logs/*.log

# 启动所有服务
start_backend
start_admin
start_client
start_user

echo ""
echo "✅ 所有服务启动完成！"
echo "================================"
echo "🌐 访问地址："
echo "   - 后端API:     http://localhost:8080/api"
echo "   - 管理员后台:   http://localhost:3000"
echo "   - 客户端:      http://localhost:3001"
echo "   - 用户端:      http://localhost:3002"
echo ""
echo "📋 演示账号："
echo "   - 管理员: admin / 123456"
echo "   - 客户: alibaba / 123456"
echo "   - 员工: employee1 / 123456"
echo ""
echo "🔍 查看日志："
echo "   tail -f logs/backend.log"
echo "   tail -f logs/admin.log"
echo "   tail -f logs/client.log"
echo "   tail -f logs/user.log"
echo ""
echo "⏹️  停止服务："
echo "   ./stop-all.sh"
echo ""

# 保存PID到文件
echo "$BACKEND_PID" > logs/backend.pid
echo "$ADMIN_PID" > logs/admin.pid
echo "$CLIENT_PID" > logs/client.pid
echo "$USER_PID" > logs/user.pid

echo "🎉 系统已就绪，按 Ctrl+C 停止所有服务"

# 等待用户中断
trap 'echo ""; echo "🛑 正在停止所有服务..."; ./stop-all.sh; exit 0' INT
wait 