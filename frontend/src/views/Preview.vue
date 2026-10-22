<template>
  <div class="preview-page">
    <header class="toolbar">
      <div class="left">
        <el-button :icon="Back" text @click="$router.push('/')">首页</el-button>
        <el-button :icon="Edit" text @click="$router.push(`/editor/${resumeId}`)">返回编辑</el-button>
        <span class="title">{{ resume.title }}</span>
      </div>
      <div class="right">
        <el-button :icon="Printer" @click="doPrint">打印</el-button>
        <el-button type="primary" :loading="exporting" :icon="Download" @click="exportPdf">导出 PDF</el-button>
      </div>
    </header>

    <div class="paper-wrap">
      <div ref="paperRef" class="paper-a4">
        <ResumePreview v-if="sections.length" :sections="sections" :theme="resume.theme" />
        <el-skeleton v-else :rows="10" animated style="padding: 40px" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Back, Download, Edit, Printer } from '@element-plus/icons-vue'
import html2pdf from 'html2pdf.js'
import { getResume } from '../api/resume'
import { parseContent } from '../utils/sectionMeta'
import ResumePreview from '../components/ResumePreview.vue'

const route = useRoute()
const resumeId = route.params.id
const resume = ref({ title: '', theme: 'default' })
const sections = ref([])
const paperRef = ref(null)
const exporting = ref(false)

function doPrint() {
  window.print()
}

async function exportPdf() {
  exporting.value = true
  try {
    await html2pdf()
      .set({
        margin: 0,
        filename: `${resume.value.title || '简历'}.pdf`,
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2, useCORS: true, backgroundColor: '#ffffff' },
        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
      })
      .from(paperRef.value)
      .save()
    ElMessage.success('PDF 已开始下载')
  } finally {
    exporting.value = false
  }
}

onMounted(async () => {
  const data = await getResume(resumeId)
  resume.value = data.resume
  document.title = `${data.resume.title} - 简历工坊`
  sections.value = data.sections.map((s) => ({
    id: s.id,
    type: s.type,
    title: s.title,
    content: parseContent(s),
    visible: s.visible
  }))
})
</script>

<style scoped>
.preview-page {
  min-height: 100%;
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
.title {
  font-weight: 600;
}
.paper-wrap {
  flex: 1;
  display: flex;
  justify-content: center;
  padding: 24px 16px;
}
.paper-a4 {
  width: 794px;
  min-height: 1123px;
  background: #fff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}
/* 打印时只保留纸张 */
@media print {
  .toolbar {
    display: none;
  }
  .paper-wrap {
    padding: 0;
  }
  .paper-a4 {
    width: 100%;
    box-shadow: none;
  }
}
</style>
