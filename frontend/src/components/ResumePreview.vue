<template>
  <div class="resume-paper" :class="'theme-' + theme">
    <template v-for="(s, idx) in visibleSections" :key="s.localId || s.id || idx">
      <!-- 基本信息 -->
      <div v-if="s.type === 'basic'" class="basic-block">
        <h1 class="basic-name">{{ c(s).name || '未填写姓名' }}</h1>
        <p class="basic-job">{{ c(s).job }}</p>
        <p class="basic-contact">
          <span v-if="c(s).phone">📞 {{ c(s).phone }}</span>
          <span v-if="c(s).email">✉️ {{ c(s).email }}</span>
          <span v-if="c(s).city">📍 {{ c(s).city }}</span>
          <span v-if="c(s).website">🔗 {{ c(s).website }}</span>
        </p>
        <p v-if="c(s).summary" class="basic-summary">{{ c(s).summary }}</p>
      </div>

      <!-- 列表型板块：教育 / 工作 / 项目 -->
      <section v-else-if="isListType(s.type)" class="section-block">
        <h2 class="section-title">{{ s.title }}</h2>
        <div v-for="(item, i) in (c(s).list || [])" :key="i" class="entry">
          <div class="entry-head">
            <strong>{{ headText(s.type, item) }}</strong>
            <span class="entry-date">{{ item.start }} {{ item.start || item.end ? '-' : '' }} {{ item.end }}</span>
          </div>
          <div v-if="subText(s.type, item)" class="entry-sub">{{ subText(s.type, item) }}</div>
          <p v-if="item.description" class="entry-desc">{{ item.description }}</p>
        </div>
      </section>

      <!-- 技能 -->
      <section v-else-if="s.type === 'skill'" class="section-block">
        <h2 class="section-title">{{ s.title }}</h2>
        <div class="skill-grid">
          <div v-for="(item, i) in (c(s).list || [])" :key="i" class="skill-item">
            <span class="skill-name">{{ item.name }}</span>
            <span class="skill-dots">
              <i v-for="d in 5" :key="d" class="dot" :class="{ on: d <= (item.level || 0) }" />
            </span>
          </div>
        </div>
      </section>

      <!-- Markdown 自由文本 -->
      <section v-else-if="s.type === 'markdown'" class="section-block">
        <h2 class="section-title">{{ s.title }}</h2>
        <MdPreview :model-value="c(s).markdown || ''" />
      </section>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'
import { parseContent } from '../utils/sectionMeta'

const props = defineProps({
  sections: { type: Array, default: () => [] },
  theme: { type: String, default: 'default' }
})

const visibleSections = computed(() => props.sections.filter((s) => s.visible !== 0 && s.visible !== false))

function c(section) {
  return parseContent(section)
}

function isListType(type) {
  return ['education', 'experience', 'project'].includes(type)
}

function headText(type, item) {
  if (type === 'education') return item.school
  if (type === 'experience') return item.company
  if (type === 'project') return item.name
  return ''
}

function subText(type, item) {
  if (type === 'education') return [item.major, item.degree].filter(Boolean).join(' · ')
  if (type === 'experience') return item.position
  if (type === 'project') return [item.role, item.link].filter(Boolean).join(' · ')
  return ''
}
</script>

<style>
/* 纸张基础样式 + 三套主题 */
.resume-paper {
  --accent: #333;
  background: #fff;
  color: #333;
  padding: 40px 48px;
  font-size: 14px;
  line-height: 1.7;
  min-height: 600px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.resume-paper.theme-blue {
  --accent: #2f5cff;
}
.resume-paper.theme-green {
  --accent: #2e8b57;
}
.resume-paper.theme-blue .basic-name,
.resume-paper.theme-green .basic-name {
  color: var(--accent);
}

.basic-block {
  text-align: center;
  padding-bottom: 14px;
  border-bottom: 2px solid var(--accent);
  margin-bottom: 18px;
}
.basic-name {
  font-size: 28px;
  letter-spacing: 2px;
}
.basic-job {
  color: var(--accent);
  font-weight: 600;
  margin-top: 2px;
}
.basic-contact {
  margin-top: 8px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 14px;
  font-size: 12.5px;
  color: #666;
}
.basic-summary {
  margin-top: 8px;
  text-align: left;
  background: #f7f7f7;
  padding: 8px 12px;
  border-radius: 4px;
  font-size: 13px;
}
.theme-blue .basic-summary,
.theme-green .basic-summary {
  background: color-mix(in srgb, var(--accent) 6%, #fff);
}

.section-block {
  margin-bottom: 18px;
}
.section-title {
  font-size: 16px;
  color: var(--accent);
  border-left: 4px solid var(--accent);
  padding-left: 8px;
  margin-bottom: 10px;
}
.entry {
  margin-bottom: 10px;
}
.entry-head {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
}
.entry-date {
  color: #909399;
  font-size: 12.5px;
  white-space: nowrap;
}
.entry-sub {
  color: #666;
  font-size: 13px;
}
.entry-desc {
  color: #555;
  font-size: 13px;
  white-space: pre-wrap;
}
.skill-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px 24px;
}
.skill-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.skill-dots .dot {
  display: inline-block;
  width: 9px;
  height: 9px;
  border-radius: 50%;
  background: #ddd;
  margin-left: 4px;
}
.skill-dots .dot.on {
  background: var(--accent);
}

/* Markdown 渲染微调 */
.resume-paper .md-editor-preview {
  font-size: 13.5px;
}
.resume-paper .default-theme p {
  margin: 4px 0;
}
</style>
