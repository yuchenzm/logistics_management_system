export const formatDate = (dateString?: string | null): string => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleDateString()
}

export const formatDateTime = (dateString?: string | null): string => {
  if (!dateString) return 'N/A'
  return new Date(dateString).toLocaleString()
}

export const getStatusType = (status: string) => {
  switch (status) {
    case 'COMPLETED': return 'success'
    case 'IN_TRANSIT': return 'primary'
    case 'PENDING': return 'warning'
    case 'CANCELLED': return 'danger'
    default: return 'info'
  }
}

export const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    PENDING: '待处理',
    CONFIRMED: '已确认',
    IN_TRANSIT: '运输中',
    DELIVERED: '已送达',
    COMPLETED: '已完成',
    CANCELLED: '已取消',
  }
  return map[status] || '未知状态'
}

export const getTransportStatusType = (status: string) => {
  switch (status) {
    case 'COMPLETED':
    case 'DELIVERED':
      return 'success'
    case 'IN_TRANSIT':
      return 'primary'
    case 'PENDING':
    case 'SCHEDULED':
      return 'warning'
    case 'CANCELLED':
    case 'DELAYED':
      return 'danger'
    default:
      return 'info'
  }
}

export const getTransportStatusText = (status: string) => {
  const map: Record<string, string> = {
    PENDING: '待调度',
    SCHEDULED: '已调度',
    IN_TRANSIT: '运输中',
    DELAYED: '已延误',
    DELIVERED: '已送达',
    COMPLETED: '已完成',
    CANCELLED: '已取消',
  }
  return map[status] || '未知状态'
} 