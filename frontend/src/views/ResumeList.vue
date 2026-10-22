<template>
  <div class="list-page">
    <header class="header">
      <div class="brand">
        <el-icon :size="26" color="#409eff"><Document /></el-icon>
        <span class="brand-name">简历工坊</span>
      </div>
      <div class="user-box">
        <span class="hello">你好，{{ nickname }}</span>
        <el-button text @click="doLogout">退出登录</el-button>
      </div>
    </header>

    <main class="main">
      <div class="toolbar">
        <h3>我的简历</h3>
        <div>
          <el-button @click="createSample">新建示例简历</el-button>
          <el-button type="primary" @click="dialogVisible = true">新建简历</el-button>
        </div>
      </div>

      <el-empty v-if="!resumes.length" description="还没有简历，点击右上角新建一份吧" />

      <div v-else class="cards">
        <el-card v-for="r in resumes" :key="r.id" class="resume-card" shadow="hover">
          <div class="card-body">
            <el-icon :size="34" :color="themeColor(r.theme)"><Document /></el-icon>
            <div class="card-title" :title="r.title">{{ r.title }}</div>
            <el-tag size="small" effect="plain">{{ themeName(r.theme) }}</el-tag>
            <div class="card-time">更新于 {{ formatTime(r.updateTime) }}</div>
          </div>
          <div class="card-actions">
            <el-button type="primary" size="small" @click="$router.push(`/editor/${r.id}`)">编辑</el-button>
            <el-button size="small" @click="$router.push(`/preview/${r.id}`)">预览</el-button>
            <el-button size="small" @click="doCopy(r)">复制</el-button>
            <el-popconfirm title="确定删除这份简历吗？" @confirm="doDelete(r)">
              <template #reference>
                <el-button type="danger" size="small" plain>删除</el-button>
              </template>
            </el-popconfirm>
          </div>
        </el-card>
      </div>
    </main>

    <el-dialog v-model="dialogVisible" title="新建简历" width="420px">
      <el-form label-position="top">
        <el-form-item label="简历标题">
          <el-input v-model="newTitle" placeholder="例如：张三-Java后端-2026" />
        </el-form-item>
        <el-form-item label="主题皮肤">
          <el-select v-model="newTheme" style="width: 100%">
            <el-option v-for="t in THEMES" :key="t.value" :label="t.label" :value="t.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="create">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listResumes, createResume, deleteResume, copyResume, saveSections } from '../api/resume'
import { logout as apiLogout, getInfo } from '../api/user'
import { buildSampleSections } from '../utils/sample'

const router = useRouter()
const resumes = ref([])
const nickname = ref(localStorage.getItem('username') || '')
const dialogVisible = ref(false)
const creating = ref(false)
const newTitle = ref('')
const newTheme = ref('default')

const THEMES = [
  { value: 'default', label: '经典黑白', color: '#333333' },
  { value: 'blue', label: '商务蓝', color: '#2f5cff' },
  { value: 'green', label: '清新绿', color: '#2e8b57' }
]

function themeName(value) {
  const t = THEMES.find((x) => x.value === value)
  return t ? t.label : value
}

function themeColor(value) {
  const t = THEMES.find((x) => x.value === value)
  return t ? t.color : '#333'
}

function formatTime(t) {
  return t ? t.replace('T', ' ').slice(0, 16) : ''
}

async function load() {
  resumes.value = await listResumes()
}

async function create() {
  creating.value = true
  try {
    const id = await createResume({ title: newTitle.value || '未命名简历', theme: newTheme.value })
    dialogVisible.value = false
    newTitle.value = ''
    router.push(`/editor/${id}`)
  } finally {
    creating.value = false
  }
}

async function createSample() {
  const id = await createResume({ title: '示例简历', theme: 'blue' })
  await saveSections(id, buildSampleSections())
  ElMessage.success('示例简历已创建')
  router.push(`/editor/${id}`)
}

async function doCopy(r) {
  await copyResume(r.id)
  ElMessage.success('已复制')
  load()
}

async function doDelete(r) {
  await deleteResume(r.id)
  ElMessage.success('已删除')
  load()
}

async function doLogout() {
  try {
    await apiLogout()
  } catch (e) { /* token 失效也无妨 */ }
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}

onMounted(async () => {
  load()
  try {
    const info = await getInfo()
    nickname.value = info.nickname || info.username
  } catch (e) { /* 忽略 */ }
})
</script>

<style scoped>
.header {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.brand {
  display: flex;
  align-items: center;
  gap: 8px;
}
.brand-name {
  font-size: 20px;
  font-weight: 600;
}
.user-box {
  display: flex;
  align-items: center;
  gap: 8px;
}
.hello {
  color: #606266;
  font-size: 14px;
}
.main {
  max-width: 1100px;
  margin: 24px auto;
  padding: 0 24px;
}
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}
.resume-card :deep(.el-card__body) {
  padding: 16px;
}
.card-body {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
}
.card-title {
  font-size: 16px;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}
.card-time {
  font-size: 12px;
  color: #909399;
}
.card-actions {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}
</style>
