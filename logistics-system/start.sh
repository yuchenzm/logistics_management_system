#!/bin/bash

echo "🚀 启动物流管理系统"
echo "===================="

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "❌ 未找到Java环境，请先安装Java 17+"
    exit 1
fi

# 检查Node.js环境
if ! command -v node &> /dev/null; then
    echo "❌ 未找到Node.js环境，请先安装Node.js 16+"
    exit 1
fi

# 检查MySQL连接
echo "🔍 检查MySQL连接..."
if ! command -v mysql &> /dev/null; then
    echo "⚠️  未找到MySQL客户端，请确保MySQL服务已启动"
fi

echo ""
echo "📊 启动后端服务..."
echo "===================="

# 进入后端目录并启动
cd backend
echo "📍 当前目录: $(pwd)"

# 检查Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ 未找到Maven，请先安装Maven"
    exit 1
fi

echo "🔨 启动Spring Boot应用..."
mvn spring-boot:run &
BACKEND_PID=$!

echo "✅ 后端服务启动中... (PID: $BACKEND_PID)"
echo "📍 后端地址: http://localhost:8080/api"

# 等待后端启动
sleep 10

echo ""
echo "🎨 启动前端服务..."
echo "====================" 

# 进入前端目录
cd ../frontend
echo "📍 当前目录: $(pwd)"

# 检查是否已安装依赖
if [ ! -d "node_modules" ]; then
    echo "📦 安装前端依赖..."
    npm install
fi

echo "🎯 启动Vue.js开发服务器..."
npm run dev &
FRONTEND_PID=$!

echo "✅ 前端服务启动中... (PID: $FRONTEND_PID)"
echo "📍 前端地址: http://localhost:3000"

echo ""
echo "🎉 系统启动完成！"
echo "=================="
echo "🔗 前端访问地址: http://localhost:3000"
echo "🔗 后端API地址: http://localhost:8080/api"
echo ""
echo "👥 演示账号:"
echo "   管理员: admin / 123456"
echo "   经理: manager1 / 123456"
echo "   司机: driver1 / 123456"
echo ""
echo "⚠️  按 Ctrl+C 停止所有服务"

# 等待用户中断
wait 