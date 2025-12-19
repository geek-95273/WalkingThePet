import request from './request';

// 获取订单列表
export const getOrdersApi = () => {
  return request({
    url: '/api/orders',
    method: 'get'
  });
};

// 获取订单详情
export const getOrderDetailApi = (id) => {
  return request({
    url: `/api/orders/${id}`,
    method: 'get'
  });
};

// 创建订单
export const createOrderApi = (data) => {
  return request({
    url: '/api/orders',
    method: 'post',
    data
  });
};

// 宠托师接单
export const acceptOrderApi = (id) => {
  return request({
    url: `/api/orders/${id}/accept`,
    method: 'post'
  });
};

// 完成订单
export const completeOrderApi = (id, data) => {
  return request({
    url: `/api/orders/${id}/complete`,
    method: 'post',
    data
  });
};
