import axios from 'axios';

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8000',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器 - 自动添加Token
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器 - 统一处理响应
request.interceptors.response.use(
  response => {
    const res = response.data;
    
    // 后端返回格式: { code: 200, success: true, message: "成功", body: {...} }
    if (res.success) {
      return res;
    } else {
      console.error('业务错误:', res.message);
      return Promise.reject(new Error(res.message || '请求失败'));
    }
  },
  error => {
    console.error('响应错误:', error);
    
    // 处理401未授权
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('username');
      // 跳转到登录页
      if (window.location.pathname !== '/') {
        window.location.href = '/';
      }
    }
    
    return Promise.reject(error);
  }
);

export default request;
