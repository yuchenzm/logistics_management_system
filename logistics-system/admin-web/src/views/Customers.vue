<template>
  <div class="customers-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">
        <el-icon><User /></el-icon>
        客户管理
      </h1>
      <p class="page-description">管理系统中的所有客户信息，包括个人客户和企业客户</p>
    </div>

    <!-- 操作栏 -->
    <div class="action-bar">
      <div class="search-section">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索客户名称、联系人或电话"
          @keyup.enter="handleSearch"
          clearable
          style="width: 300px;"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select
          v-model="searchForm.city"
          placeholder="选择城市"
          clearable
          style="width: 150px; margin-left: 10px;"
        >
          <el-option
            v-for="city in cityOptions"
            :key="city"
            :label="city"
            :value="city"
          />
        </el-select>
        <el-select
          v-model="searchForm.creditRating"
          placeholder="信用等级"
          clearable
          style="width: 120px; margin-left: 10px;"
        >
          <el-option label="A级" value="A" />
          <el-option label="B级" value="B" />
          <el-option label="C级" value="C" />
          <el-option label="D级" value="D" />
        </el-select>
        <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
        <el-button @click="resetSearch" :icon="Refresh">重置</el-button>
      </div>
      
      <div class="button-section">
        <el-button type="primary" @click="handleAdd" :icon="Plus">
          新增客户
        </el-button>
        <el-button @click="exportData" :icon="Download">
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        height="600"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="客户名称" min-width="150">
          <template #default="{ row }">
            <div class="customer-name">
              <el-tag
                :type="row.customerType === 'company' ? 'primary' : 'success'"
                size="small"
                style="margin-right: 8px;"
              >
                {{ row.customerType === 'company' ? '企业' : '个人' }}
              </el-tag>
              {{ row.name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" width="160" />
        <el-table-column prop="city" label="城市" width="100" />
        <el-table-column prop="creditRating" label="信用等级" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getCreditRatingType(row.creditRating)"
              size="small"
            >
              {{ row.creditRating }}级
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag
              :type="row.status === 'active' ? 'success' : 'danger'"
              size="small"
            >
              {{ row.status === 'active' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
              :icon="Edit"
            >
              编辑
            </el-button>
            <el-dropdown @command="(command) => handleAction(command, row)">
              <el-button size="small" type="info">
                更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="view">查看详情</el-dropdown-item>
                  <el-dropdown-item command="orders">订单历史</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户类型" prop="customerType">
              <el-radio-group v-model="formData.customerType">
                <el-radio value="individual">个人客户</el-radio>
                <el-radio value="company">企业客户</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信用等级" prop="creditRating">
              <el-select v-model="formData.creditRating" placeholder="请选择">
                <el-option label="A级" value="A" />
                <el-option label="B级" value="B" />
                <el-option label="C级" value="C" />
                <el-option label="D级" value="D" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="客户名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入客户名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="formData.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱地址" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input
            v-model="formData.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input v-model="formData.city" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio value="active">正常</el-radio>
                <el-radio value="inactive">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  User, Search, Refresh, Plus, Download, Edit, ArrowDown
} from '@element-plus/icons-vue'
import { customerApi } from '@/utils/api'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const selectedRows = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  city: '',
  creditRating: ''
})

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 表单数据
const formData = reactive({
  id: null,
  name: '',
  contactPerson: '',
  phone: '',
  email: '',
  address: '',
  city: '',
  customerType: 'individual',
  creditRating: 'B',
  status: 'active'
})

// 城市选项
const cityOptions = ref(['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '西安'])

// 表单引用
const formRef = ref()

// 表单验证规则
const formRules = {
  name: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 页面加载时获取数据
onMounted(() => {
  fetchData()
})

// 获取客户数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      keyword: searchForm.keyword,
      city: searchForm.city,
      creditRating: searchForm.creditRating
    }
    const response = await customerApi.getCustomers(params)
    tableData.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    console.error('获取客户数据失败:', error)
    ElMessage.error('获取客户数据失败: ' + (error.message || '网络错误'))
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    keyword: '',
    city: '',
    creditRating: ''
  })
  handleSearch()
}

// 新增客户
const handleAdd = () => {
  dialogTitle.value = '新增客户'
  resetFormData()
  dialogVisible.value = true
}

// 编辑客户
const handleEdit = (row) => {
  dialogTitle.value = '编辑客户'
  // 正确映射后端驼峰字段到前端下划线字段
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    contactPerson: row.contactPerson,
    phone: row.phone,
    email: row.email,
    address: row.address,
    city: row.city,
    customerType: row.customerType,
    creditRating: row.creditRating,
    status: row.status
  })
  dialogVisible.value = true
}

// 处理操作
const handleAction = async (command, row) => {
  switch (command) {
    case 'view':
      // 查看详情逻辑
      ElMessage.info('查看详情功能开发中')
      break
    case 'orders':
      // 订单历史逻辑
      ElMessage.info('订单历史功能开发中')
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

// 删除客户
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除客户"${row.name}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await customerApi.deleteCustomer(row.id)
    ElMessage.success('删除成功')
    await fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 表单提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // 将前端下划线字段映射到后端驼峰字段
    const customerData = {
      id: formData.id,
      name: formData.name,
      contactPerson: formData.contactPerson,
      phone: formData.phone,
      email: formData.email,
      address: formData.address,
      city: formData.city,
      customerType: formData.customerType,
      creditRating: formData.creditRating,
      status: formData.status
    }
    
    await customerApi.saveCustomer(customerData)
    ElMessage.success(formData.id ? '更新成功' : '创建成功')
    
    dialogVisible.value = false
    await fetchData()
  } catch (error) {
    console.error('保存客户失败:', error)
    ElMessage.error('操作失败: ' + (error.message || '网络错误'))
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleClose = (done) => {
  ElMessageBox.confirm('确定关闭？未保存的数据将丢失')
    .then(() => done())
    .catch(() => {})
}

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    contactPerson: '',
    phone: '',
    email: '',
    address: '',
    city: '',
    customerType: 'individual',
    creditRating: 'B',
    status: 'active'
  })
}

// 分页相关
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.currentPage = 1
  fetchData()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  fetchData()
}

// 选择变化
const handleSelectionChange = (val) => {
  selectedRows.value = val
}

// 导出数据
const exportData = () => {
  ElMessage.info('导出功能开发中')
}

// 工具函数
const getCreditRatingType = (rating) => {
  const types = { A: 'success', B: 'primary', C: 'warning', D: 'danger' }
  return types[rating] || 'info'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN')
}
</script>

<style scoped>
.customers-container {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.page-title {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-description {
  margin: 10px 0 0 0;
  font-size: 16px;
  opacity: 0.9;
}

.action-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-section {
  display: flex;
  align-items: center;
}

.button-section {
  display: flex;
  gap: 10px;
}

.table-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.customer-name {
  display: flex;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: white;
  margin-top: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  color: #333;
  font-weight: 600;
}

:deep(.el-button--small) {
  padding: 5px 8px;
  font-size: 12px;
}
</style> 