import request from './request';

// 用户注册
export const registerApi = (data) => {
  return request({
    url: '/api/auth/register',
    method: 'post',
    data
  });
};

// 用户登录
export const loginApi = (data) => {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  });
};

// 用户登出
export const logoutApi = () => {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  });
};
