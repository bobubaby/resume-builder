<template>
  <div class="editor-page">
    <!-- 顶部工具栏 -->
    <header class="toolbar">
      <div class="left">
        <el-button :icon="Back" text @click="$router.push('/')">返回</el-button>
        <el-input v-model="resume.title" class="title-input" placeholder="简历标题" />
        <el-tag type="info" effect="plain">{{ saving ? '保存中…' : '已加载' }}</el-tag>
      </div>
      <div class="right">
        <span class="theme-label">主题</span>
        <el-select v-model="resume.theme" class="theme-select">
          <el-option label="经典黑白" value="default" />
          <el-option label="商务蓝" value="blue" />
          <el-option label="清新绿" value="green" />
        </el-select>
        <el-button :loading="saving" @click="save">保存</el-button>
        <el-button type="primary" @click="saveAndPreview">预览 / 导出</el-button>
      </div>
    </header>

    <div class="body">
      <!-- 左侧：可拖拽的组件面板 -->
      <aside class="palette">
        <h4 class="palette-title">组件库</h4>
        <p class="palette-tip">拖到中间画布，或点击添加</p>
        <draggable
          :list="palette"
          :group="{ name: 'sections', pull: 'clone', put: false }"
          :clone="cloneSection"
          :sort="false"
          item-key="type"
        >
          <template #item="{ element }">
            <div class="palette-item" @click="addSection(element.type)">
              <el-icon><component :is="element.icon" /></el-icon>
              <span>{{ element.name }}</span>
              <el-icon class="drag-hint"><Rank /></el-icon>
            </div>
          </template>
        </draggable>
      </aside>

      <!-- 中间：拖拽画布 -->
      <main class="canvas">
        <draggable
          v-model="sections"
          group="sections"
          item-key="key"
          handle=".drag-handle"
          animation="200"
          ghost-class="ghost"
          class="canvas-list"
        >
          <template #item="{ element }">
            <div class="section-card" :class="{ hidden: element.visible === 0 }">
              <div class="section-head">
                <el-icon class="drag-handle"><Rank /></el-icon>
                <span class="section-name">{{ element.title }}</span>
                <el-tag size="small" effect="plain" type="info">{{ typeName(element.type) }}</el-tag>
                <div class="section-actions">
                  <el-button text size="small" type="primary" :icon="Edit" @click="openEditor(element)">编辑</el-button>
                  <el-button text size="small" :icon="element.visible === 0 ? View : Hide" @click="toggleVisible(element)">
                    {{ element.visible === 0 ? '显示' : '隐藏' }}
                  </el-button>
                  <el-button text size="small" type="danger" :icon="Delete" @click="removeSection(element)" />
                </div>
              </div>
              <div class="section-summary">{{ summarize(element) }}</div>
            </div>
          </template>
        </draggable>
        <el-empty v-if="!sections.length" description="从左侧拖入组件开始制作简历" />
      </main>

      <!-- 右侧：实时预览 -->
      <aside class="preview">
        <h4 class="palette-title">实时预览</h4>
        <div class="preview-scroll">
          <ResumePreview :sections="sections" :theme="resume.theme" />
        </div>
      </aside>
    </div>

    <!-- 编辑抽屉：表单填充 / Markdown 编辑 -->
    <el-drawer
      v-model="drawerVisible"
      :title="`编辑：${editing ? editing.title : ''}`"
      size="560px"
      destroy-on-close
    >
      <SectionForm v-if="editing" :section="editing" />
      <template #footer>
        <el-button type="primary" @click="drawerVisible = false">完成</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Back, Delete, Edit, Hide, Rank, View } from '@element-plus/icons-vue'
import draggable from 'vuedraggable'
import { getResume, saveSections, updateResumeMeta } from '../api/resume'
import { SECTION_TYPES, newSection, parseContent, summarize, typeName } from '../utils/sectionMeta'
import SectionForm from '../components/SectionForm.vue'
import ResumePreview from '../components/ResumePreview.vue'

const route = useRoute()
const router = useRouter()
const resumeId = route.params.id

const palette = SECTION_TYPES
const resume = ref({ title: '', theme: 'default' })
const sections = ref([])
const saving = ref(false)
const drawerVisible = ref(false)
const editing = ref(null)

// vuedraggable 需要稳定 key；数据库未保存的用 localId，已保存的用 id
function withKey(s) {
  s.key = s.localId || s.id || Math.random()
  return s
}

function cloneSection(meta) {
  return withKey(newSection(meta.type))
}

function addSection(type) {
  sections.value.push(withKey(newSection(type)))
}

function openEditor(section) {
  editing.value = section
  drawerVisible.value = true
}

function toggleVisible(section) {
  section.visible = section.visible === 0 ? 1 : 0
}

function removeSection(section) {
  const i = sections.value.indexOf(section)
  if (i >= 0) {
    sections.value.splice(i, 1)
  }
}

function serialize() {
  return sections.value.map((s) => ({
    type: s.type,
    title: s.title,
    content: JSON.stringify(s.content || {}),
    visible: s.visible === 0 ? 0 : 1
  }))
}

async function save() {
  saving.value = true
  try {
    await updateResumeMeta(resumeId, { title: resume.value.title, theme: resume.value.theme })
    await saveSections(resumeId, serialize())
    ElMessage.success('已保存')
  } finally {
    saving.value = false
  }
}

async function saveAndPreview() {
  await save()
  router.push(`/preview/${resumeId}`)
}

onMounted(async () => {
  const data = await getResume(resumeId)
  resume.value = data.resume
  sections.value = data.sections.map((s) =>
    withKey({
      id: s.id,
      type: s.type,
      title: s.title,
      content: parseContent(s),
      visible: s.visible
    })
  )
})
</script>

<style scoped>
.editor-page {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.toolbar {
  height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.left,
.right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.title-input {
  width: 220px;
}
.theme-label {
  font-size: 13px;
  color: #606266;
}
.theme-select {
  width: 130px;
}
.body {
  flex: 1;
  display: flex;
  min-height: 0;
}
.palette {
  width: 220px;
  background: #fff;
  border-right: 1px solid #e4e7ed;
  padding: 14px;
  overflow-y: auto;
}
.palette-title {
  font-size: 14px;
  color: #303133;
}
.palette-tip {
  font-size: 12px;
  color: #909399;
  margin: 4px 0 12px;
}
.palette-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  margin-bottom: 8px;
  border: 1px dashed #c0c4cc;
  border-radius: 6px;
  cursor: grab;
  font-size: 13px;
  color: #606266;
  background: #fafafa;
}
.palette-item:hover {
  border-color: #409eff;
  color: #409eff;
}
.drag-hint {
  margin-left: auto;
  color: #c0c4cc;
}
.canvas {
  flex: 1;
  padding: 18px;
  overflow-y: auto;
}
.canvas-list {
  min-height: 200px;
}
.section-card {
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  margin-bottom: 10px;
}
.section-card.hidden {
  opacity: 0.45;
}
.section-card.ghost,
.ghost {
  opacity: 0.4;
  border: 1px dashed #409eff;
}
.section-head {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  border-bottom: 1px solid #f0f0f0;
}
.section-head .drag-handle {
  cursor: grab;
  color: #909399;
}
.section-name {
  font-weight: 600;
  font-size: 14px;
}
.section-actions {
  margin-left: auto;
  display: flex;
  gap: 2px;
}
.section-summary {
  padding: 10px 12px;
  font-size: 12.5px;
  color: #909399;
}
.preview {
  width: 480px;
  background: #fff;
  border-left: 1px solid #e4e7ed;
  padding: 14px;
  display: flex;
  flex-direction: column;
  min-height: 0;
}
.preview-scroll {
  flex: 1;
  overflow-y: auto;
  margin-top: 10px;
  padding-right: 4px;
}
</style>
