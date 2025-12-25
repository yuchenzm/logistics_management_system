import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const { data } = response
    if (data.code === 200) {
      return data
    } else {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

// 用户相关API
export const userApi = {
  login: (data) => api.post('/users/login', data),
  getUserInfo: (id) => api.get(`/users/${id}`),
  getUsers: (pageData) => api.post('/users/page', pageData),
  saveUser: (data) => api.post('/users', data),
  deleteUser: (id) => api.delete(`/users/${id}`)
}

// 客户相关API
export const customerApi = {
  getCustomers: (pageData) => api.post('/customers/page', pageData),
  getCustomerById: (id) => api.get(`/customers/${id}`),
  getAllActiveCustomers: () => api.get('/customers/active'),
  saveCustomer: (data) => api.post('/customers', data),
  updateCustomer: (id, data) => api.put(`/customers/${id}`, data),
  deleteCustomer: (id) => api.delete(`/customers/${id}`),
  searchCustomers: (keyword) => api.get(`/customers/search?keyword=${keyword}`)
}

// 订单相关API
export const orderApi = {
  getOrders: (pageData) => api.post('/orders/page', pageData),
  getOrderById: (id) => api.get(`/orders/${id}`),
  getOrderByNumber: (orderNumber) => api.get(`/orders/number/${orderNumber}`),
  getOrdersByStatus: (status) => api.get(`/orders/status/${status}`),
  saveOrder: (data) => api.post('/orders', data),
  updateOrderStatus: (id, data) => api.put(`/orders/${id}/status`, data),
  deleteOrder: (id) => api.delete(`/orders/${id}`),
  getOrderStatistics: () => api.get('/orders/statistics')
}

// 司机相关API
export const driverApi = {
  getDrivers: (pageData) => api.post('/drivers/page', pageData),
  getDriverById: (id) => api.get(`/drivers/${id}`),
  getAvailableDrivers: () => api.get('/drivers/available'),
  saveDriver: (data) => api.post('/drivers', data),
  updateDriverStatus: (id, data) => api.put(`/drivers/${id}/status`, data),
  deleteDriver: (id) => api.delete(`/drivers/${id}`)
}

// 车辆相关API
export const vehicleApi = {
  getVehicles: (pageData) => api.post('/vehicles/page', pageData),
  getVehicleById: (id) => api.get(`/vehicles/${id}`),
  getAvailableVehicles: () => api.get('/vehicles/available'),
  getVehiclesByType: (type) => api.get(`/vehicles/type/${type}`),
  saveVehicle: (data) => api.post('/vehicles', data),
  updateVehicleStatus: (id, data) => api.put(`/vehicles/${id}/status`, data),
  assignDriver: (id, data) => api.put(`/vehicles/${id}/driver`, data),
  deleteVehicle: (id) => api.delete(`/vehicles/${id}`)
}

// 运输相关API
export const transportApi = {
  getTransports: (pageData) => api.post('/transports/page', pageData),
  getTransportById: (id) => api.get(`/transports/${id}`),
  getTransportByNumber: (transportNumber) => api.get(`/transports/number/${transportNumber}`),
  getTransportsByStatus: (status) => api.get(`/transports/status/${status}`),
  saveTransport: (data) => api.post('/transports', data),
  updateTransportStatus: (id, data) => api.put(`/transports/${id}/status`, data),
  deleteTransport: (id) => api.delete(`/transports/${id}`),
  getTransportStatistics: () => api.get('/transports/statistics')
}

// 仓库相关API
export const warehouseApi = {
  getWarehouses: (pageData) => api.post('/warehouses/page', pageData),
  getWarehouseById: (id) => api.get(`/warehouses/${id}`),
  getWarehousesByType: (type) => api.get(`/warehouses/type/${type}`),
  getWarehousesByCity: (city) => api.get(`/warehouses/city/${city}`),
  saveWarehouse: (data) => api.post('/warehouses', data),
  updateWarehouseStatus: (id, data) => api.put(`/warehouses/${id}/status`, data),
  deleteWarehouse: (id) => api.delete(`/warehouses/${id}`),
  searchWarehouses: (keyword) => api.get(`/warehouses/search?keyword=${keyword}`)
}

// 货物相关API
export const goodsApi = {
  getGoods: (pageData) => api.post('/goods/page', pageData),
  getGoodsById: (id) => api.get(`/goods/${id}`),
  getGoodsBySku: (sku) => api.get(`/goods/sku/${sku}`),
  getGoodsByCategory: (category) => api.get(`/goods/category/${category}`),
  getGoodsByStatus: (status) => api.get(`/goods/status/${status}`),
  saveGoods: (data) => api.post('/goods', data),
  updateGoodsStatus: (id, data) => api.put(`/goods/${id}/status`, data),
  deleteGoods: (id) => api.delete(`/goods/${id}`),
  searchGoods: (keyword) => api.get(`/goods/search?keyword=${keyword}`)
}

// 库存相关API
export const inventoryApi = {
  getInventory: (pageData) => api.post('/inventory/page', pageData),
  getInventoryById: (id) => api.get(`/inventory/${id}`),
  getInventoryByWarehouse: (warehouseId) => api.get(`/inventory/warehouse/${warehouseId}`),
  getInventoryByGoods: (goodsId) => api.get(`/inventory/goods/${goodsId}`),
  saveInventory: (data) => api.post('/inventory', data),
  inboundInventory: (id, data) => api.post(`/inventory/${id}/inbound`, data),
  outboundInventory: (id, data) => api.post(`/inventory/${id}/outbound`, data),
  adjustInventory: (id, data) => api.post(`/inventory/${id}/adjust`, data),
  deleteInventory: (id) => api.delete(`/inventory/${id}`),
  searchInventory: (keyword) => api.get(`/inventory/search?keyword=${keyword}`)
}

// 供应商相关API
export const supplierApi = {
  getSuppliers: (pageData) => api.post('/suppliers/page', pageData),
  getSupplierById: (id) => api.get(`/suppliers/${id}`),
  getAllActiveSuppliers: () => api.get('/suppliers/active'),
  getSuppliersByStatus: (status) => api.get(`/suppliers/status/${status}`),
  getSuppliersByCity: (city) => api.get(`/suppliers/city/${city}`),
  saveSupplier: (data) => api.post('/suppliers', data),
  updateSupplierStatus: (id, data) => api.put(`/suppliers/${id}/status`, data),
  deleteSupplier: (id) => api.delete(`/suppliers/${id}`),
  searchSuppliers: (keyword) => api.get(`/suppliers/search?keyword=${keyword}`)
}

// 费用相关API
export const expenseApi = {
  getExpenses: (pageData) => api.post('/expenses/page', pageData),
  getExpenseById: (id) => api.get(`/expenses/${id}`),
  getExpensesByType: (type) => api.get(`/expenses/type/${type}`),
  getExpensesByTransport: (transportId) => api.get(`/expenses/transport/${transportId}`),
  getExpensesByStatus: (status) => api.get(`/expenses/status/${status}`),
  saveExpense: (data) => api.post('/expenses', data),
  updateExpenseStatus: (id, data) => api.put(`/expenses/${id}/status`, data),
  deleteExpense: (id) => api.delete(`/expenses/${id}`),
  getExpenseStatistics: () => api.get('/expenses/statistics'),
  searchExpenses: (keyword) => api.get(`/expenses/search?keyword=${keyword}`)
}

// 配送相关API
export const deliveryApi = {
  getDeliveries: (pageData) => api.post('/deliveries/page', pageData),
  getDeliveryById: (id) => api.get(`/deliveries/${id}`),
  getDeliveriesByStatus: (status) => api.get(`/deliveries/status/${status}`),
  getDeliveriesByTransport: (transportId) => api.get(`/deliveries/transport/${transportId}`),
  getDeliveriesByDeliverer: (deliverer) => api.get(`/deliveries/deliverer/${deliverer}`),
  getDeliveryByTransportNumber: (transportNumber) => api.get(`/deliveries/transport-number/${transportNumber}`),
  saveDelivery: (data) => api.post('/deliveries', data),
  updateDeliveryStatus: (id, data) => api.put(`/deliveries/${id}/status`, data),
  signForDelivery: (id, data) => api.post(`/deliveries/${id}/sign`, data),
  deleteDelivery: (id) => api.delete(`/deliveries/${id}`),
  searchDeliveries: (keyword) => api.get(`/deliveries/search?keyword=${keyword}`)
}

// 运费相关API
export const shippingRateApi = {
  getShippingRates: (pageData) => api.post('/shipping-rates/page', pageData),
  getShippingRateById: (id) => api.get(`/shipping-rates/${id}`),
  getShippingRatesByOrigin: (originCity) => api.get(`/shipping-rates/origin/${originCity}`),
  getShippingRatesByDestination: (destinationCity) => api.get(`/shipping-rates/destination/${destinationCity}`),
  getShippingRatesByType: (transportType) => api.get(`/shipping-rates/type/${transportType}`),
  getShippingRatesByRoute: (originCity, destinationCity) => 
    api.get(`/shipping-rates/route?originCity=${originCity}&destinationCity=${destinationCity}`),
  calculateShippingCost: (data) => api.post('/shipping-rates/calculate', data),
  saveShippingRate: (data) => api.post('/shipping-rates', data),
  batchUpdateShippingRates: (data) => api.post('/shipping-rates/batch-update', data),
  deleteShippingRate: (id) => api.delete(`/shipping-rates/${id}`),
  searchShippingRates: (keyword) => api.get(`/shipping-rates/search?keyword=${keyword}`)
}

// 便捷API方法 - 客户
api.getCustomersByPage = (params) => customerApi.getCustomers(params)
api.createCustomer = (data) => customerApi.saveCustomer(data)
api.updateCustomer = (id, data) => customerApi.updateCustomer(id, data)
api.deleteCustomer = (id) => customerApi.deleteCustomer(id)

// 便捷API方法 - 司机
api.getDriversByPage = (params) => driverApi.getDrivers(params)
api.createDriver = (data) => driverApi.saveDriver(data)
api.updateDriver = (id, data) => driverApi.saveDriver({id, ...data})
api.deleteDriver = (id) => driverApi.deleteDriver(id)

// 便捷API方法 - 车辆
api.getVehiclesByPage = (params) => vehicleApi.getVehicles(params)
api.createVehicle = (data) => vehicleApi.saveVehicle(data)
api.updateVehicle = (id, data) => vehicleApi.saveVehicle({id, ...data})
api.deleteVehicle = (id) => vehicleApi.deleteVehicle(id)

// 便捷API方法 - 运输
api.getTransportsByPage = (params) => transportApi.getTransports(params)
api.createTransport = (data) => transportApi.saveTransport(data)
api.updateTransport = (id, data) => transportApi.saveTransport({id, ...data})
api.deleteTransport = (id) => transportApi.deleteTransport(id)

// 便捷API方法 - 订单
api.getOrdersByPage = (params) => orderApi.getOrders(params)
api.createOrder = (data) => orderApi.saveOrder(data)
api.updateOrder = (id, data) => orderApi.saveOrder({id, ...data})
api.deleteOrder = (id) => orderApi.deleteOrder(id)

// 便捷API方法 - 仓库
api.getWarehousesByPage = (params) => warehouseApi.getWarehouses(params)
api.createWarehouse = (data) => warehouseApi.saveWarehouse(data)
api.updateWarehouse = (id, data) => warehouseApi.saveWarehouse({id, ...data})
api.deleteWarehouse = (id) => warehouseApi.deleteWarehouse(id)

// 便捷API方法 - 货物
api.getGoodsByPage = (params) => goodsApi.getGoods(params)
api.createGoods = (data) => goodsApi.saveGoods(data)
api.updateGoods = (id, data) => goodsApi.saveGoods({id, ...data})
api.deleteGoods = (id) => goodsApi.deleteGoods(id)

// 便捷API方法 - 库存
api.getInventoryByPage = (params) => inventoryApi.getInventory(params)
api.createInventory = (data) => inventoryApi.saveInventory(data)
api.updateInventory = (id, data) => inventoryApi.saveInventory({id, ...data})
api.deleteInventory = (id) => inventoryApi.deleteInventory(id)

// 便捷API方法 - 供应商
api.getSuppliersByPage = (params) => supplierApi.getSuppliers(params)
api.createSupplier = (data) => supplierApi.saveSupplier(data)
api.updateSupplier = (id, data) => supplierApi.saveSupplier({id, ...data})
api.deleteSupplier = (id) => supplierApi.deleteSupplier(id)

// 便捷API方法 - 费用
api.getExpensesByPage = (params) => expenseApi.getExpenses(params)
api.createExpense = (data) => expenseApi.saveExpense(data)
api.updateExpense = (id, data) => expenseApi.saveExpense({id, ...data})
api.deleteExpense = (id) => expenseApi.deleteExpense(id)

// 便捷API方法 - 配送
api.getDeliveriesByPage = (params) => deliveryApi.getDeliveries(params)
api.createDelivery = (data) => deliveryApi.saveDelivery(data)
api.updateDelivery = (id, data) => deliveryApi.saveDelivery({id, ...data})
api.deleteDelivery = (id) => deliveryApi.deleteDelivery(id)

// 便捷API方法 - 运费
api.getShippingRatesByPage = (params) => shippingRateApi.getShippingRates(params)
api.createShippingRate = (data) => shippingRateApi.saveShippingRate(data)
api.updateShippingRate = (id, data) => shippingRateApi.saveShippingRate({id, ...data})
api.deleteShippingRate = (id) => shippingRateApi.deleteShippingRate(id)

export default api 