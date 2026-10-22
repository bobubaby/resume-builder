// 板块类型元数据：拖拽面板、表单、预览共用
export const SECTION_TYPES = [
  { type: 'basic', name: '基本信息', icon: 'User', singleton: true },
  { type: 'education', name: '教育经历', icon: 'School' },
  { type: 'experience', name: '工作经历', icon: 'OfficeBuilding' },
  { type: 'project', name: '项目经历', icon: 'FolderOpened' },
  { type: 'skill', name: '技能特长', icon: 'Star' },
  { type: 'markdown', name: '自定义 Markdown', icon: 'EditPen' }
]

export function typeName(type) {
  const meta = SECTION_TYPES.find((t) => t.type === type)
  return meta ? meta.name : type
}

export function defaultContent(type) {
  switch (type) {
    case 'basic':
      return { name: '', job: '', phone: '', email: '', city: '', website: '', summary: '' }
    case 'education':
      return { list: [{ school: '', major: '', degree: '', start: '', end: '', description: '' }] }
    case 'experience':
      return { list: [{ company: '', position: '', start: '', end: '', description: '' }] }
    case 'project':
      return { list: [{ name: '', role: '', start: '', end: '', link: '', description: '' }] }
    case 'skill':
      return { list: [{ name: '', level: 3 }] }
    case 'markdown':
      return { markdown: '' }
    default:
      return {}
  }
}

let localSeq = -1
export function newSection(type) {
  return {
    localId: localSeq--,
    id: null,
    type,
    title: typeName(type),
    content: defaultContent(type),
    visible: 1
  }
}

export function parseContent(section) {
  if (section.content && typeof section.content === 'string') {
    try {
      return JSON.parse(section.content)
    } catch (e) {
      return {}
    }
  }
  return section.content || {}
}

// 画布上每条板块的摘要文字
export function summarize(section) {
  const c = parseContent(section)
  switch (section.type) {
    case 'basic':
      return [c.name, c.job].filter(Boolean).join(' · ') || '姓名、求职意向、联系方式'
    case 'education':
    case 'experience':
    case 'project':
      return `${(c.list || []).length} 条经历`
    case 'skill':
      return `${(c.list || []).length} 项技能`
    case 'markdown':
      return (c.markdown || '').slice(0, 40) || '支持 Markdown 语法的自由文本'
    default:
      return ''
  }
}
