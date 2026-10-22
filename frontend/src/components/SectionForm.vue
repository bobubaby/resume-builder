<template>
  <div v-if="section">
    <el-form label-position="top">
      <el-form-item label="板块标题">
        <el-input v-model="section.title" maxlength="20" show-word-limit />
      </el-form-item>

      <!-- 基本信息 -->
      <template v-if="section.type === 'basic'">
        <el-row :gutter="12">
          <el-col :span="12"><el-form-item label="姓名"><el-input v-model="content.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="求职意向"><el-input v-model="content.job" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="电话"><el-input v-model="content.phone" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="content.email" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="城市"><el-input v-model="content.city" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="个人网站 / GitHub"><el-input v-model="content.website" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="个人简介">
          <el-input v-model="content.summary" type="textarea" :rows="3" />
        </el-form-item>
      </template>

      <!-- Markdown -->
      <template v-else-if="section.type === 'markdown'">
        <MdEditor v-model="content.markdown" :preview="false" style="height: 420px" />
      </template>

      <!-- 技能 -->
      <template v-else-if="section.type === 'skill'">
        <div v-for="(item, i) in content.list" :key="i" class="entry-row">
          <el-input v-model="item.name" placeholder="技能名称" class="entry-main" />
          <el-rate v-model="item.level" :max="5" />
          <el-button text type="danger" :icon="Delete" circle @click="removeEntry(i)" />
        </div>
        <el-button text type="primary" :icon="Plus" @click="addEntry">添加技能</el-button>
      </template>

      <!-- 教育 / 工作 / 项目：动态条数表单 -->
      <template v-else>
        <div v-for="(item, i) in content.list" :key="i" class="entry-card">
          <div class="entry-card-head">
            <span>第 {{ i + 1 }} 条</span>
            <el-button text type="danger" size="small" :icon="Delete" @click="removeEntry(i)">删除</el-button>
          </div>
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item :label="fieldLabels.head">
                <el-input v-model="item[fieldKeys.head]" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="fieldLabels.sub">
                <el-input v-model="item[fieldKeys.sub]" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开始时间">
                <el-input v-model="item.start" placeholder="如 2021-07" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间">
                <el-input v-model="item.end" placeholder="如 至今" />
              </el-form-item>
            </el-col>
            <el-col v-if="section.type === 'project'" :span="12">
              <el-form-item label="项目链接">
                <el-input v-model="item.link" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="描述">
                <el-input v-model="item.description" type="textarea" :rows="3" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-button text type="primary" :icon="Plus" @click="addEntry">添加一条</el-button>
      </template>
    </el-form>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { Delete, Plus } from '@element-plus/icons-vue'
import { defaultContent } from '../utils/sectionMeta'

const props = defineProps({
  section: { type: Object, default: null }
})

// 板块内容统一为对象（提交时由父组件序列化）
const content = computed(() => props.section.content)

const FIELD_MAP = {
  education: { head: 'school', sub: 'major', labels: { head: '学校', sub: '专业 / 学历' } },
  experience: { head: 'company', sub: 'position', labels: { head: '公司', sub: '职位' } },
  project: { head: 'name', sub: 'role', labels: { head: '项目名称', sub: '担任角色' } }
}

const meta = computed(() => FIELD_MAP[props.section?.type] || { head: '', sub: '', labels: { head: '', sub: '' } })
const fieldKeys = computed(() => ({ head: meta.value.head, sub: meta.value.sub }))
const fieldLabels = computed(() => meta.value.labels)

function addEntry() {
  if (!content.value.list) {
    content.value.list = []
  }
  const tpl = defaultContent(props.section.type)
  content.value.list.push(tpl.list ? tpl.list[0] : { name: '', level: 3 })
}

function removeEntry(i) {
  content.value.list.splice(i, 1)
}
</script>

<style scoped>
.entry-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}
.entry-main {
  flex: 1;
}
.entry-card {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 12px 12px 0;
  margin-bottom: 14px;
}
.entry-card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #909399;
  font-size: 13px;
  margin-bottom: 6px;
}
</style>
