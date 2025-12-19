import { reactive } from 'vue';
import { findSitter } from './sitters';

export const orders = reactive([]);

export const createOrder = (payload) => {
  const sitter = findSitter(payload.sitterId);
  const id = `o-${(Math.random() * 100000).toFixed(0)}`;
  const record = {
    id,
    sitterId: payload.sitterId,
    sitterName: sitter ? sitter.name : '未知宠托师',
    serviceType: payload.serviceType,
    serviceTime: payload.serviceTime,
    address: payload.address,
    petId: payload.petId || '',
    remark: payload.remark || '',
    status: payload.status || '待宠托师接单', // 默认等待接单，宠托师接单时直接设为'已接单'
    createdAt: new Date().toLocaleString(),
    walkerGender: payload.walkerGender || '不限',
    completeContent: '', // 完成内容
    completeImage: '', // 完成图片
    completedAt: '' // 完成时间
  };
  orders.unshift(record);
  return id;
};

export const findOrder = (id) => orders.find((item) => item.id === id);

// 宠托师接单
export const acceptOrder = (id) => {
  const item = findOrder(id);
  if (item && item.status === '待宠托师接单') {
    item.status = '已接单';
  }
};

// 完成订单
export const completeOrder = (id, content, image) => {
  const item = findOrder(id);
  if (item) {
    item.status = '已完成';
    item.completeContent = content;
    item.completeImage = image;
    item.completedAt = new Date().toLocaleString();
  }
};
