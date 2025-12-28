import request from './request';

// 获取宠托师列表
export const getSittersApi = (params) => {
  return request({
    url: '/api/sitters',
    method: 'get',
    params
  });
};

// 获取宠托师详情
export const getSitterDetailApi = (id) => {
  return request({
    url: `/api/sitters/${id}`,
    method: 'get'
  });
};

// 入驻宠托师
export const joinSitterApi = (data) => {
  return request({
    url: '/api/sitters/join',
    method: 'post',
    data
  });
};

// 获取当前用户的宠托师信息
export const getMySitterInfoApi = () => {
  return request({
    url: '/api/sitters/my-info',
    method: 'get'
  });
};

// 更新宠托师信息
export const updateSitterApi = (data) => {
  return request({
    url: '/api/sitters/update',
    method: 'put',
    data
  });
};
