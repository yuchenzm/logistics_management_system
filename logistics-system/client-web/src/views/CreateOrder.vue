<template>
  <div class="create-order-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>创建新订单</h2>
        <p>请填写以下信息创建您的物流订单</p>
      </div>
      <div class="header-right">
        <el-button @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <el-button type="primary" @click="saveDraft">
          <el-icon><Document /></el-icon>
          保存草稿
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
              <el-tag type="danger" size="small">必填</el-tag>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="客户姓名" prop="customerName">
                <el-input
                  v-model="orderForm.customerName"
                  placeholder="请输入客户姓名"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="phone">
                <el-input
                  v-model="orderForm.phone"
                  placeholder="请输入联系电话"
                  clearable
                />
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
              <el-form-item label="预期送达时间" prop="expectedDeliveryTime">
                <el-date-picker
                  v-model="orderForm.expectedDeliveryTime"
                  type="datetime"
                  placeholder="选择预期送达时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        
        <!-- 取货信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>取货信息</h3>
              <el-tag type="danger" size="small">必填</el-tag>
            </div>
          </template>
          
          <el-form-item label="取货地址" prop="pickupAddress">
            <el-input
              v-model="orderForm.pickupAddress"
              type="textarea"
              :rows="3"
              placeholder="请输入详细的取货地址"
            />
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="取货联系人" prop="pickupContact">
                <el-input
                  v-model="orderForm.pickupContact"
                  placeholder="取货联系人姓名"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="pickupPhone">
                <el-input
                  v-model="orderForm.pickupPhone"
                  placeholder="取货联系人电话"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="取货时间" prop="pickupTime">
                <el-date-picker
                  v-model="orderForm.pickupTime"
                  type="datetime"
                  placeholder="选择取货时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="取货备注">
                <el-input
                  v-model="orderForm.pickupRemark"
                  placeholder="如有特殊要求请填写"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        
        <!-- 配送信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>配送信息</h3>
              <el-tag type="danger" size="small">必填</el-tag>
            </div>
          </template>
          
          <el-form-item label="配送地址" prop="deliveryAddress">
            <el-input
              v-model="orderForm.deliveryAddress"
              type="textarea"
              :rows="3"
              placeholder="请输入详细的配送地址"
            />
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="收货联系人" prop="deliveryContact">
                <el-input
                  v-model="orderForm.deliveryContact"
                  placeholder="收货联系人姓名"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系电话" prop="deliveryPhone">
                <el-input
                  v-model="orderForm.deliveryPhone"
                  placeholder="收货联系人电话"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="配送时间" prop="deliveryTime">
                <el-date-picker
                  v-model="orderForm.deliveryTime"
                  type="datetime"
                  placeholder="选择配送时间"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="配送备注">
                <el-input
                  v-model="orderForm.deliveryRemark"
                  placeholder="如有特殊要求请填写"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        
        <!-- 货物信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>货物信息</h3>
              <el-tag type="danger" size="small">必填</el-tag>
            </div>
          </template>
          
          <el-form-item label="货物描述" prop="goodsDescription">
            <el-input
              v-model="orderForm.goodsDescription"
              type="textarea"
              :rows="3"
              placeholder="请详细描述货物信息（如：电子设备、文件资料等）"
            />
          </el-form-item>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="重量(KG)" prop="weight">
                <el-input-number
                  v-model="orderForm.weight"
                  :min="0.1"
                  :precision="2"
                  placeholder="货物重量"
                  style="width: 100%"
                  @change="calculateShipping"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="体积(立方米)" prop="volume">
                <el-input-number
                  v-model="orderForm.volume"
                  :min="0.01"
                  :precision="3"
                  placeholder="货物体积"
                  style="width: 100%"
                  @change="calculateShipping"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="货物价值(元)" prop="goodsValue">
                <el-input-number
                  v-model="orderForm.goodsValue"
                  :min="0"
                  :precision="2"
                  placeholder="货物价值"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="货物类型" prop="goodsType">
            <el-select v-model="orderForm.goodsType" placeholder="请选择货物类型">
              <el-option label="文件资料" value="documents" />
              <el-option label="电子设备" value="electronics" />
              <el-option label="服装箱包" value="clothing" />
              <el-option label="食品饮料" value="food" />
              <el-option label="日用品" value="daily" />
              <el-option label="其他" value="other" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="特殊要求">
            <el-input
              v-model="orderForm.specialInstructions"
              type="textarea"
              :rows="2"
              placeholder="如需特殊处理，请在此说明（可选）"
            />
          </el-form-item>
        </el-card>
        
        <!-- 服务选择 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>服务选择</h3>
              <el-tag type="info" size="small">可选</el-tag>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="服务类型" prop="serviceType">
                <el-select 
                  v-model="orderForm.serviceType" 
                  placeholder="选择服务类型"
                  @change="calculateShipping"
                >
                  <el-option 
                    v-for="service in serviceTypes"
                    :key="service.value"
                    :label="service.label"
                    :value="service.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="保险服务">
                <el-switch
                  v-model="orderForm.insurance"
                  active-text="购买保险"
                  inactive-text="不购买"
                  @change="calculateShipping"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="上门取货">
                <el-switch
                  v-model="orderForm.doorToDoor"
                  active-text="上门取货"
                  inactive-text="自送到点"
                  @change="calculateShipping"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="签收确认">
                <el-switch
                  v-model="orderForm.signatureRequired"
                  active-text="需要签收"
                  inactive-text="不需要"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        
        <!-- 费用信息 -->
        <el-card class="form-section cost-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>费用信息</h3>
              <el-tag type="success" size="small">自动计算</el-tag>
            </div>
          </template>
          
          <div class="cost-breakdown">
            <div class="cost-item">
              <span class="cost-label">基础运费：</span>
              <span class="cost-value">¥{{ shippingCost.base }}</span>
            </div>
            <div class="cost-item">
              <span class="cost-label">附加服务费：</span>
              <span class="cost-value">¥{{ shippingCost.additional }}</span>
            </div>
            <div class="cost-item">
              <span class="cost-label">保险费：</span>
              <span class="cost-value">¥{{ shippingCost.insurance }}</span>
            </div>
            <div class="cost-item total">
              <span class="cost-label">总计：</span>
              <span class="cost-value">¥{{ shippingCost.total }}</span>
            </div>
          </div>
        </el-card>
        
        <!-- 备注信息 -->
        <el-card class="form-section" shadow="never">
          <template #header>
            <div class="section-header">
              <h3>备注信息</h3>
              <el-tag type="info" size="small">可选</el-tag>
            </div>
          </template>
          
          <el-form-item label="备注">
            <el-input
              v-model="orderForm.remarks"
              type="textarea"
              :rows="3"
              placeholder="如有其他需要说明的事项，请在此填写"
            />
          </el-form-item>
        </el-card>
      </el-form>
      
      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button size="large" @click="goBack">
          取消
        </el-button>
        <el-button type="info" size="large" @click="saveDraft">
          保存草稿
        </el-button>
        <el-button type="primary" size="large" @click="submitOrder" :loading="submitting">
          提交订单
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { ArrowLeft, Document } from '@element-plus/icons-vue'
import { orderApi } from '../utils/api'

const router = useRouter()
const orderFormRef = ref<FormInstance>()
const submitting = ref(false)

// 订单表单数据
const orderForm = reactive({
  customerName: '',
  phone: '',
  priority: 'low',
  expectedDeliveryTime: '',
  pickupAddress: '',
  pickupContact: '',
  pickupPhone: '',
  pickupTime: '',
  pickupRemark: '',
  deliveryAddress: '',
  deliveryContact: '',
  deliveryPhone: '',
  deliveryTime: '',
  deliveryRemark: '',
  goodsDescription: '',
  weight: null,
  volume: null,
  goodsValue: null,
  goodsType: '',
  specialInstructions: '',
  serviceType: 'standard',
  insurance: false,
  doorToDoor: false,
  signatureRequired: false,
  remarks: ''
})

// 运费计算
const shippingCost = reactive({
  base: 0,
  additional: 0,
  insurance: 0,
  total: 0
})

// 服务类型选项
const serviceTypes = [
  { label: '标准快递', value: 'standard' },
  { label: '特快专递', value: 'express' },
  { label: '当日送达', value: 'same_day' },
  { label: '次日送达', value: 'next_day' }
]

// 表单验证规则
const formRules: FormRules = {
  customerName: [
    { required: true, message: '请输入客户姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请选择订单优先级', trigger: 'change' }
  ],
  pickupAddress: [
    { required: true, message: '请输入取货地址', trigger: 'blur' }
  ],
  pickupContact: [
    { required: true, message: '请输入取货联系人', trigger: 'blur' }
  ],
  pickupPhone: [
    { required: true, message: '请输入取货联系电话', trigger: 'blur' }
  ],
  deliveryAddress: [
    { required: true, message: '请输入配送地址', trigger: 'blur' }
  ],
  deliveryContact: [
    { required: true, message: '请输入收货联系人', trigger: 'blur' }
  ],
  deliveryPhone: [
    { required: true, message: '请输入收货联系电话', trigger: 'blur' }
  ],
  goodsDescription: [
    { required: true, message: '请输入货物描述', trigger: 'blur' }
  ],
  weight: [
    { required: true, message: '请输入货物重量', trigger: 'blur' }
  ],
  volume: [
    { required: true, message: '请输入货物体积', trigger: 'blur' }
  ],
  goodsValue: [
    { required: true, message: '请输入货物价值', trigger: 'blur' }
  ],
  goodsType: [
    { required: true, message: '请选择货物类型', trigger: 'change' }
  ],
  serviceType: [
    { required: true, message: '请选择服务类型', trigger: 'change' }
  ]
}

onMounted(() => {
  // 初始化表单数据
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  if (userInfo.username) {
    orderForm.customerName = userInfo.username
  }
  
  // 初始化运费计算
  calculateShipping()
})

// 计算运费
const calculateShipping = () => {
  let basePrice = 0
  let additionalPrice = 0
  let insurancePrice = 0
  
  // 基础运费计算（根据重量和体积）
  if (orderForm.weight && orderForm.volume) {
    const weightCost = orderForm.weight * 5 // 每公斤5元
    const volumeCost = orderForm.volume * 100 // 每立方米100元
    basePrice = Math.max(weightCost, volumeCost)
  }
  
  // 服务类型加价
  switch (orderForm.serviceType) {
    case 'express':
      additionalPrice += basePrice * 0.5
      break
    case 'same_day':
      additionalPrice += basePrice * 1.0
      break
    case 'next_day':
      additionalPrice += basePrice * 0.3
      break
  }
  
  // 附加服务费
  if (orderForm.doorToDoor) {
    additionalPrice += 20
  }
  
  if (orderForm.signatureRequired) {
    additionalPrice += 10
  }
  
  // 保险费
  if (orderForm.insurance && orderForm.goodsValue) {
    insurancePrice = orderForm.goodsValue * 0.005 // 0.5%
  }
  
  // 优先级加价
  if (orderForm.priority === 'high') {
    additionalPrice += basePrice * 0.3
  } else if (orderForm.priority === 'medium') {
    additionalPrice += basePrice * 0.15
  }
  
  Object.assign(shippingCost, {
    base: Math.round(basePrice),
    additional: Math.round(additionalPrice),
    insurance: Math.round(insurancePrice),
    total: Math.round(basePrice + additionalPrice + insurancePrice)
  })
}

// 提交订单
const submitOrder = async () => {
  if (!orderFormRef.value) return
  
  await orderFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const orderData = {
          // 从localStorage获取customerId
          customerId: JSON.parse(localStorage.getItem('userInfo') || '{}').id,
          orderStatus: 'pending',
          priority: orderForm.priority,
          originAddress: orderForm.pickupAddress,
          destinationAddress: orderForm.deliveryAddress,
          totalWeight: Number(orderForm.weight) || 0,
          totalAmount: Number(shippingCost.total) || 0,
          expectedDeliveryTime: orderForm.expectedDeliveryTime,
          remarks: orderForm.remarks,
          goodsInfo: orderForm.goodsDescription, // 假设后端使用goodsInfo字段
        };

        const response: any = await orderApi.create(orderData);
        
        if (response.code === 200) {
          ElMessage.success('订单创建成功！');
          router.push('/orders');
        } else {
          ElMessage.error(response.message || '订单创建失败，请稍后重试');
        }
      } catch (error: any) {
        console.error('订单提交失败:', error);
        ElMessage.error(error.response?.data?.message || '订单创建失败，请检查网络或联系管理员');
      } finally {
        submitting.value = false;
      }
    } else {
      ElMessage.error('表单填写有误，请检查并修正');
    }
  });
};

// 保存草稿
const saveDraft = () => {
  const draftData = { ...orderForm }
  localStorage.setItem('orderDraft', JSON.stringify(draftData))
  ElMessage.success('草稿保存成功')
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.create-order-page {
  height: 100%;
  overflow-y: auto;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left h2 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.header-left p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.header-right {
  display: flex;
  gap: 12px;
}

.form-container {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.form-section {
  margin-bottom: 24px;
  border: 1px solid #e4e7ed;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.cost-section {
  background: #f8f9fa;
}

.cost-breakdown {
  padding: 16px 0;
}

.cost-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #e4e7ed;
}

.cost-item:last-child {
  border-bottom: none;
}

.cost-item.total {
  font-weight: 600;
  font-size: 18px;
  color: #e6a23c;
  padding-top: 16px;
  border-top: 2px solid #e6a23c;
}

.cost-label {
  color: #606266;
}

.cost-value {
  color: #303133;
  font-weight: 500;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding: 32px 0;
  border-top: 1px solid #e4e7ed;
  margin-top: 24px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .header-right {
    width: 100%;
    justify-content: flex-end;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
