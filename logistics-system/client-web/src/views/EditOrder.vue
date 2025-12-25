<template>
  <div class="create-order-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>编辑订单</h2>
        <p>在此处修改您的订单信息</p>
      </div>
      <div class="header-right">
        <el-button @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <el-button type="primary" :loading="loading" @click="submitOrder">
          <el-icon><Select /></el-icon>
          保存更改
        </el-button>
      </div>
    </div>
    
    <!-- 订单表单 -->
    <div class="form-container">
      <el-form
        ref="orderFormRef"
        :model="orderForm"
        :rules="formRules"
        label-width="120px"
        size="large"
      >
        <!-- 基本信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>基本信息</h3>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="订单号">
                <el-input :value="orderForm.orderNumber" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
               <el-form-item label="订单状态" prop="orderStatus">
                <el-select v-model="orderForm.orderStatus" placeholder="请选择订单状态">
                  <el-option label="待处理" value="pending"></el-option>
                  <el-option label="已确认" value="confirmed"></el-option>
                  <el-option label="已取货" value="picked_up"></el-option>
                  <el-option label="运输中" value="in_transit"></el-option>
                  <el-option label="已送达" value="delivered"></el-option>
                  <el-option label="已取消" value="cancelled"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="订单优先级" prop="priority">
                <el-select v-model="orderForm.priority" placeholder="请选择优先级">
                  <el-option label="普通" value="low" />
                  <el-option label="中等" value="medium" />
                  <el-option label="紧急" value="high" />
                </el-select>
              </el-form-item>
            </el-col>
             <el-col :span="12">
              <el-form-item label="总金额">
                 <el-input v-model.number="orderForm.totalAmount" disabled>
                   <template #append>元</template>
                 </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        
        <!-- 地址信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>地址信息</h3>
            </div>
          </template>
          
          <el-form-item label="始发地址" prop="originAddress">
            <el-input
              v-model="orderForm.originAddress"
              type="textarea"
              :rows="2"
              placeholder="请输入详细的始发地址"
            />
          </el-form-item>
          <el-form-item label="目的地地址" prop="destinationAddress">
            <el-input
              v-model="orderForm.destinationAddress"
              type="textarea"
              :rows="2"
              placeholder="请输入详细的目的地地址"
            />
          </el-form-item>
        </el-card>
        
        
        <!-- 货物信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>货物信息</h3>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="总重量(KG)" prop="totalWeight">
                <el-input-number
                  v-model="orderForm.totalWeight"
                  :min="0.1"
                  :precision="2"
                  placeholder="货物重量"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="总体积(m³)" prop="totalVolume">
                <el-input-number
                  v-model="orderForm.totalVolume"
                  :min="0.01"
                  :precision="3"
                  placeholder="货物体积"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 备注信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>备注信息</h3>
            </div>
          </template>
           <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="orderForm.remarks"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-card>

      </el-form>
    </div>
    
    <!-- 底部操作栏 -->
    <div class="footer-actions">
       <el-button @click="goBack">取消</el-button>
       <el-button type="primary" :loading="loading" @click="submitOrder">
         保存更改
       </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElForm } from 'element-plus'
import { ArrowLeft, Select } from '@element-plus/icons-vue'
import { orderApi } from '@/utils/api'
import type { Order } from '@/types/api'

const router = useRouter()
const route = useRoute()
const orderFormRef = ref<InstanceType<typeof ElForm>>()
const loading = ref(false)

// 使用 Partial<Order> 允许表单在数据加载前为空
const orderForm = reactive<Partial<Order>>({})

// 表单验证规则
const formRules = {
  priority: [{ required: true, message: '请选择订单优先级', trigger: 'change' }],
  originAddress: [{ required: true, message: '请输入始发地址', trigger: 'blur' }],
  destinationAddress: [{ required: true, message: '请输入目的地地址', trigger: 'blur' }],
  totalWeight: [{ required: true, message: '请输入总重量', trigger: 'blur' }],
  orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
}

// 获取订单数据
const fetchOrderDetails = async (id: string) => {
  loading.value = true
  try {
    const res = await orderApi.detail(id)
    if (res.success && res.data) {
      // 将获取到的数据赋值给表单
      Object.assign(orderForm, res.data)
    } else {
      ElMessage.error(res.message || '获取订单详情失败')
      goBack() // 获取失败则返回上一页
    }
  } catch (error) {
    console.error('获取订单详情异常:', error)
    ElMessage.error('获取订单详情异常')
    goBack()
  } finally {
    loading.value = false
  }
}

// 提交订单更新
const submitOrder = async () => {
  if (!orderFormRef.value) return
  await orderFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // @ts-ignore
        const res = await orderApi.update(orderForm.id, orderForm)
        if (res.success) {
          ElMessage.success('订单更新成功！')
          goBack()
        } else {
          ElMessage.error(res.message || '订单更新失败')
        }
      } catch (error) {
        console.error('订单更新异常:', error)
        ElMessage.error('订单更新异常')
      } finally {
        loading.value = false
      }
    } else {
      ElMessage.error('请检查表单填写是否正确')
    }
  })
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 组件挂载时获取订单ID并拉取数据
onMounted(() => {
  const orderId = route.params.id as string
  if (orderId) {
    fetchOrderDetails(orderId)
  } else {
    ElMessage.error('无效的订单ID')
    goBack()
  }
})
</script>

<style scoped>
.create-order-page {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 50px); /* 减去顶部导航栏高度 */
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.header-left h2 {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.header-left p {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.form-container {
  background-color: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.form-section {
  margin-bottom: 24px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
}

.form-section:last-child {
  margin-bottom: 0;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-header h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #303133;
}

.footer-actions {
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 16px 48px;
  background-color: #fff;
  border-top: 1px solid #e4e7ed;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
  z-index: 100;
}

:deep(.el-card__header) {
  background-color: #fafafa;
}

:deep(.el-select) {
  width: 100%;
}
</style> 