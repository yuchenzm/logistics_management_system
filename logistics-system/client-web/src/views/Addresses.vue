<template>
  <div class="addresses-page">
    <div class="page-header">
      <h2>地址管理</h2>
      <el-button type="primary" @click="openForm()" :icon="Plus">添加新地址</el-button>
    </div>

    <div v-if="loading" v-loading="loading" class="loading-container">
       <el-skeleton :rows="5" animated />
    </div>
    
    <div v-else-if="apiError" class="error-container">
       <el-empty description="无法加载地址列表，后端服务可能正在开发中。">
         <el-button type="primary" @click="loadAddresses">点击重试</el-button>
       </el-empty>
    </div>

    <div v-else>
      <el-row :gutter="24" v-if="addresses.length > 0">
        <el-col :xs="24" :sm="12" :md="8" v-for="address in addresses" :key="address.id">
          <el-card class="address-card" :class="{ 'is-default': address.isDefault }">
            <template #header>
              <div class="card-header">
                <span>{{ address.contactName }}</span>
                <el-tag v-if="address.isDefault" type="success" size="small" effect="dark">默认</el-tag>
              </div>
            </template>
            <div class="address-details">
              <p><el-icon><Phone /></el-icon> {{ address.contactPhone }}</p>
              <p><el-icon><Location /></el-icon> {{ formatAddress(address) }}</p>
            </div>
            <div class="card-actions">
              <el-button-group>
                <el-button type="primary" :icon="Edit" @click="openForm(address)" />
                <el-button type="danger" :icon="Delete" @click="handleDelete(address.id)" />
              </el-button-group>
              <el-button v-if="!address.isDefault" text bg type="success" @click="setDefault(address)">设为默认</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else description="您还没有添加任何地址，快去添加一个吧！"></el-empty>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑地址' : '添加新地址'" width="600px" top="5vh">
      <el-form ref="addressFormRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入手机号码"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province" placeholder="请输入省份"></el-input>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="form.city" placeholder="请输入城市"></el-input>
        </el-form-item>
        <el-form-item label="区/县" prop="district">
          <el-input v-model="form.district" placeholder="请输入区/县"></el-input>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, ElCard, ElRow, ElCol, ElButton, ElIcon, ElTag, ElDialog, ElForm, ElFormItem, ElInput, ElCheckbox, ElEmpty, ElButtonGroup, ElSkeleton } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Plus, Phone, Location, Edit, Delete } from '@element-plus/icons-vue'
import { userApi } from '../utils/api'

interface Address {
  id: number
  contactName: string
  contactPhone: string
  province: string
  city: string
  district: string
  address: string
  isDefault: boolean
}

const loading = ref(true)
const saving = ref(false)
const addresses = ref<Address[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const addressFormRef = ref<FormInstance>()
const apiError = ref(false);

const form = reactive({
  id: null as number | null,
  contactName: '',
  contactPhone: '',
  province: '',
  city: '',
  district: '',
  address: '',
  isDefault: false
})

const rules: FormRules = {
  contactName: [{ required: true, message: '请输入联系人姓名', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
  province: [{ required: true, message: '请输入省份', trigger: 'blur' }],
  city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
  address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

onMounted(() => {
  loadAddresses()
})

const formatAddress = (addr: Address) => {
  return `${addr.province} ${addr.city} ${addr.district} ${addr.address}`
}

const loadAddresses = async () => {
  loading.value = true
  apiError.value = false;
  try {
    const userInfoStr = localStorage.getItem('userInfo');
    if (!userInfoStr) {
      throw new Error("用户未登录或会话已过期");
    }
    const userInfo = JSON.parse(userInfoStr);
    const userId = userInfo?.id;

    if (!userId) {
      throw new Error("无法获取用户ID");
    }

    const response: any = await userApi.getAddresses(userId)
    if (response.code === 200) {
      addresses.value = response.data
    } else {
      ElMessage.error(response.message || '加载地址列表失败')
      apiError.value = true;
    }
  } catch (error: any) {
    ElMessage.error(error.message || '加载地址列表失败，请检查网络')
    apiError.value = true;
  } finally {
    loading.value = false
  }
}

const openForm = (address?: Address) => {
  isEdit.value = !!address
  if (address) {
    Object.assign(form, address)
  } else {
    addressFormRef.value?.resetFields()
    Object.assign(form, { id: null, contactName: '', contactPhone: '', province: '', city: '', district: '', address: '', isDefault: false })
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!addressFormRef.value) return
  await addressFormRef.value.validate(async (valid) => {
    if (valid) {
      saving.value = true
      try {
        let response: any
        const addressData = { ...form }
        if (isEdit.value && form.id) {
          response = await userApi.updateAddress(form.id, addressData)
        } else {
          response = await userApi.addAddress(addressData)
        }
        
        if (response.code === 200) {
          ElMessage.success(isEdit.value ? '地址更新成功' : '地址添加成功')
          dialogVisible.value = false
          await loadAddresses()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        ElMessage.error('操作失败，请重试')
      } finally {
        saving.value = false
      }
    }
  })
}

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个地址吗？此操作不可恢复。', '警告', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response: any = await userApi.deleteAddress(id)
    if (response.code === 200) {
      ElMessage.success('地址删除成功')
      await loadAddresses()
    } else {
      ElMessage.error(response.message || '地址删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('地址删除失败')
    }
  }
}

const setDefault = async (address: Address) => {
  try {
    const response: any = await userApi.setDefaultAddress(address.id)
    if (response.code === 200) {
      ElMessage.success('默认地址设置成功')
      await loadAddresses()
    } else {
      ElMessage.error(response.message || '设置失败')
    }
  } catch (error) {
    ElMessage.error('设置失败')
  }
}
</script>

<style scoped>
.addresses-page {
  padding: 24px;
  background-color: #f0f2f5;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
}

.address-card {
  margin-bottom: 24px;
  border-radius: 8px;
  transition: all 0.3s;
}

.address-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transform: translateY(-4px);
}

.address-card.is-default {
  border-left: 4px solid #67c23a;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.address-details p {
  margin: 8px 0;
  display: flex;
  align-items: center;
  color: #606266;
}

.address-details .el-icon {
  margin-right: 8px;
}

.card-actions {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.loading-container, .error-container {
  padding: 20px;
  text-align: center;
}
</style> 