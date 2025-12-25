#!/bin/bash

echo "🛑 停止物流管理系统所有服务..."
echo "================================"

# 停止进程
stop_process() {
    local pid_file=$1
    local service_name=$2
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if kill -0 "$pid" 2>/dev/null; then
            echo "🔴 停止 $service_name (PID: $pid)"
            kill "$pid"
            sleep 2
            # 如果进程还在运行，强制杀死
            if kill -0 "$pid" 2>/dev/null; then
                echo "   强制停止 $service_name"
                kill -9 "$pid"
            fi
        else
            echo "⚪ $service_name 已停止"
        fi
        rm -f "$pid_file"
    else
        echo "⚪ $service_name 未运行"
    fi
}

# 创建日志目录（如果不存在）
mkdir -p logs

# 停止所有服务
stop_process "logs/backend.pid" "后端服务"
stop_process "logs/admin.pid" "管理员后台"
stop_process "logs/client.pid" "客户端"
stop_process "logs/user.pid" "用户端"

# 额外检查并停止可能遗留的进程
echo ""
echo "🔍 检查遗留进程..."

# 停止可能的Spring Boot进程
pkill -f "spring-boot:run" 2>/dev/null && echo "   停止Spring Boot进程"

# 停止可能的npm dev进程  
pkill -f "npm.*run.*dev" 2>/dev/null && echo "   停止npm dev进程"

# 停止可能的vite进程
pkill -f "vite" 2>/dev/null && echo "   停止vite进程"

echo ""
echo "✅ 所有服务已停止"
echo "================================"
