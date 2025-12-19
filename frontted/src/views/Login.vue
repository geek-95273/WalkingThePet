<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '../data/user';

const router = useRouter();
const form = reactive({
  username: '',
  password: ''
});

const handleLogin = async () => {
  if (!form.username.trim() || !form.password.trim()) {
    alert('请填写用户名和密码');
    return;
  }

  try {
    const result = await login(form.username, form.password);
    if (result.success) {
      alert('登录成功');
      router.push({ name: 'Home' });
    } else {
      alert(result.message || '登录失败，请检查用户名和密码');
    }
  } catch (error) {
    alert('登录失败，请稍后重试');
  }
};

const goToRegister = () => {
  router.push({ name: 'Register' });
};
</script>

<template>
  <section class="login-page">
    <div class="login-container">
      <div class="login-card">
        <header class="login-header">
          <h1>欢迎回来</h1>
          <p class="subtitle">登录您的账户，继续使用宠物喂溜服务</p>
        </header>

        <form class="login-form" @submit.prevent="handleLogin">
          <label class="field">
            <span>用户名</span>
            <input 
              v-model="form.username" 
              type="text" 
              placeholder="请输入用户名" 
              required 
              autocomplete="username"
            />
          </label>

          <label class="field">
            <span>密码</span>
            <input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码" 
              required 
              autocomplete="current-password"
            />
          </label>

          <button type="submit" class="cta">登录</button>
        </form>

        <div class="register-link">
          <span>还没有账户？</span>
          <button type="button" class="link-btn" @click="goToRegister">立即注册</button>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(255, 248, 225, 0.95), rgba(255, 235, 180, 0.8));
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 420px;
}

.login-card {
  background: #fffaf0;
  border-radius: 20px;
  padding: 40px 32px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: 0 20px 50px rgba(88, 70, 0, 0.12);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

h1 {
  margin: 0 0 8px;
  color: #2e2200;
  font-size: 28px;
}

.subtitle {
  margin: 0;
  color: rgba(46, 34, 0, 0.68);
  font-size: 14px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field span {
  color: #4a3900;
  font-weight: 700;
  font-size: 14px;
}

input {
  padding: 14px 16px;
  border-radius: 12px;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: #fffef8;
  outline: none;
  color: #2e2200;
  font-size: 15px;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

input:focus {
  border-color: rgba(255, 214, 102, 0.8);
  box-shadow: 0 0 0 3px rgba(255, 214, 102, 0.35);
}

.cta {
  margin-top: 8px;
  padding: 14px 20px;
  border-radius: 12px;
  font-weight: 800;
  font-size: 16px;
  cursor: pointer;
  border: 1px solid rgba(255, 214, 102, 0.4);
  background: linear-gradient(145deg, #ffc857, #ffb347);
  color: #2e2200;
  box-shadow: 0 12px 20px rgba(255, 200, 87, 0.32);
  transition: transform 0.12s ease, box-shadow 0.15s ease;
}

.cta:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 24px rgba(255, 200, 87, 0.4);
}

.cta:active {
  transform: translateY(0);
}

.register-link {
  margin-top: 24px;
  text-align: center;
  font-size: 14px;
  color: rgba(46, 34, 0, 0.7);
}

.link-btn {
  border: none;
  background: none;
  color: #d68900;
  font-weight: 700;
  cursor: pointer;
  padding: 0;
  margin-left: 4px;
  text-decoration: underline;
  transition: color 0.15s ease;
}

.link-btn:hover {
  color: #b17400;
}
</style>
