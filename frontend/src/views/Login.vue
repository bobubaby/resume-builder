<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 class="title">简历工坊</h2>
      <p class="subtitle">在线简历编辑器 · 拖拽 / Markdown / 表单三种方式</p>
      <el-tabs v-model="mode" stretch>
        <el-tab-pane label="登录" name="login" />
        <el-tab-pane label="注册" name="register" />
      </el-tabs>
      <el-form :model="form" label-position="top" @submit.prevent="submit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="至少 3 个字符" />
        </el-form-item>
        <el-form-item v-if="mode === 'register'" label="昵称">
          <el-input v-model="form.nickname" placeholder="选填" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="至少 6 位" @keyup.enter="submit" />
        </el-form-item>
        <el-button type="primary" class="submit" :loading="loading" @click="submit">
          {{ mode === 'login' ? '登 录' : '注 册' }}
        </el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '../api/user'

const router = useRouter()
const mode = ref('login')
const loading = ref(false)
const form = reactive({ username: '', password: '', nickname: '' })

async function submit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const token = mode.value === 'login'
      ? await login(form.username, form.password)
      : await register(form.username, form.password, form.nickname)
    localStorage.setItem('token', token)
    localStorage.setItem('username', form.username)
    ElMessage.success(mode.value === 'login' ? '登录成功' : '注册成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 10px 20px 20px;
}
.title {
  text-align: center;
  font-size: 26px;
  color: #303133;
}
.subtitle {
  text-align: center;
  color: #909399;
  font-size: 13px;
  margin: 6px 0 12px;
}
.submit {
  width: 100%;
  margin-top: 4px;
}
</style>
