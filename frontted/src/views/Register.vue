<script setup>
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { register } from '../data/user';

const router = useRouter();
const form = reactive({
  username: '',
  password: '',
  confirmPassword: ''
});

const handleRegister = async () => {
  if (!form.username.trim() || !form.password.trim() || !form.confirmPassword.trim()) {
    alert('请填写所有字段');
    return;
  }

  if (form.password !== form.confirmPassword) {
    alert('两次输入的密码不一致');
    return;
  }

  if (form.password.length < 6) {
    alert('密码长度至少为6位');
    return;
  }

  try {
    const result = await register(form.username, form.password);
    if (result.success) {
      alert('注册成功，请登录');
      router.push({ name: 'Login' });
    } else {
      alert(result.message || '注册失败');
    }
  } catch (error) {
    alert('注册失败，请稍后重试');
  }
};

const goToLogin = () => {
  router.push({ name: 'Login' });
};
</script>

<template>
  <section class="register-page">
    <div class="register-container">
      <div class="register-card">
        <header class="register-header">
          <h1>创建账户</h1>
          <p class="subtitle">加入我们，开始您的宠物喂溜之旅</p>
        </header>

        <form class="register-form" @submit.prevent="handleRegister">
          <label class="field">
            <span>用户名</span>
            <input 
              v-model="form.username" 
              type="text" 
              placeholder="请输入用户名" 
              required 
              minlength="3"
              autocomplete="username"
            />
          </label>

          <label class="field">
            <span>密码</span>
            <input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码（至少6位）" 
              required 
              minlength="6"
              autocomplete="new-password"
            />
          </label>

          <label class="field">
            <span>确认密码</span>
            <input 
              v-model="form.confirmPassword" 
              type="password" 
              placeholder="请再次输入密码" 
              required 
              minlength="6"
              autocomplete="new-password"
            />
          </label>

          <button type="submit" class="cta">注册</button>
        </form>

        <div class="login-link">
          <span>已有账户？</span>
          <button type="button" class="link-btn" @click="goToLogin">立即登录</button>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(255, 248, 225, 0.95), rgba(255, 235, 180, 0.8));
  padding: 20px;
}

.register-container {
  width: 100%;
  max-width: 420px;
}

.register-card {
  background: #fffaf0;
  border-radius: 20px;
  padding: 40px 32px;
  border: 1px solid rgba(255, 214, 102, 0.32);
  box-shadow: 0 20px 50px rgba(88, 70, 0, 0.12);
}

.register-header {
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

.register-form {
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

.login-link {
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
