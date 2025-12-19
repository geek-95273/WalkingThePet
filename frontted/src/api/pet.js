import request from './request';

// 获取宠物列表
export const getPetsApi = () => {
  return request({
    url: '/api/pets',
    method: 'get'
  });
};

// 获取宠物详情
export const getPetDetailApi = (id) => {
  return request({
    url: `/api/pets/${id}`,
    method: 'get'
  });
};

// 创建宠物档案
export const createPetApi = (data) => {
  return request({
    url: '/api/pets',
    method: 'post',
    data
  });
};

// 更新宠物档案
export const updatePetApi = (id, data) => {
  return request({
    url: `/api/pets/${id}`,
    method: 'put',
    data
  });
};

// 删除宠物档案
export const deletePetApi = (id) => {
  return request({
    url: `/api/pets/${id}`,
    method: 'delete'
  });
};
