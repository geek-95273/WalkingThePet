import { reactive } from 'vue';
import { registerApi, loginApi, logoutApi } from '../api/user';

// 用户状态
export const user = reactive({
  sitterId: null,
  userId: null,
  username: null,
  isLoggedIn: false
});

// 初始化用户状态（从localStorage恢复）
const initUserState = () => {
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  const username = localStorage.getItem('username');
  const sitterId = localStorage.getItem('sitterId');
  
  if (token && userId && username) {
    user.userId = userId;
    user.username = username;
    user.isLoggedIn = true;
    user.sitterId = sitterId;
  }
};

// 页面加载时初始化
initUserState();

// 注册功能
export const register = async (username, password) => {
  try {
    const response = await registerApi({
      username,
      password,
      confirmPassword: password
    });
    
    if (response.success) {
      return { success: true, message: response.message || '注册成功' };
    } else {
      return { success: false, message: response.message || '注册失败' };
    }
  } catch (error) {
    console.error('注册失败:', error);
    return { success: false, message: error.message || '注册失败，请稍后重试' };
  }
};

// 登录功能
export const login = async (username, password) => {
  try {
    const response = await loginApi({
      username,
      password
    });
    
    if (response.success && response.body) {
      // 保存token和用户信息
      localStorage.setItem('token', response.body.token);
      localStorage.setItem('userId', response.body.userId);
      localStorage.setItem('username', response.body.username);
      
      // 更新用户状态
      user.userId = response.body.userId;
      user.username = response.body.username;
      user.isLoggedIn = true;
      
      return { success: true, message: response.message || '登录成功' };
    } else {
      return { success: false, message: response.message || '登录失败' };
    }
  } catch (error) {
    console.error('登录失败:', error);
    return { success: false, message: error.message || '用户名或密码错误' };
  }
};

// 登出功能
export const logout = async () => {
  try {
    await logoutApi();
  } catch (error) {
    console.error('登出失败:', error);
  } finally {
    // 清除本地存储
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('sitterId');
    
    // 重置用户状态
    user.userId = null;
    user.username = null;
    user.isLoggedIn = false;
    user.sitterId = null;
  }
};

export const setSitterId = (id) => {
  user.sitterId = id;
  if (id) {
    localStorage.setItem('sitterId', id);
  } else {
    localStorage.removeItem('sitterId');
  }
};

export const isSitter = () => Boolean(user.sitterId);

export const isLoggedIn = () => user.isLoggedIn;
