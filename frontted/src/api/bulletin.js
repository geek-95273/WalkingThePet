import request from './request';

// 获取公告列表
export const getBulletinsApi = (params) => {
  return request({
    url: '/api/bulletins',
    method: 'get',
    params
  });
};

// 获取公告详情
export const getBulletinDetailApi = (id) => {
  return request({
    url: `/api/bulletins/${id}`,
    method: 'get'
  });
};

// 创建公告
export const createBulletinApi = (data) => {
  return request({
    url: '/api/bulletins',
    method: 'post',
    data
  });
};

// 接受公告（宠托师接单）
export const acceptBulletinApi = (id, data) => {
  return request({
    url: `/api/bulletins/${id}/accept`,
    method: 'post',
    data
  });
};

// 删除公告
export const deleteBulletinApi = (id) => {
  return request({
    url: `/api/bulletins/${id}`,
    method: 'delete'
  });
};
