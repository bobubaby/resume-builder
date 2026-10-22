import { newSection } from './sectionMeta'

// 生成一份填好的示例简历板块，用于“新建示例简历”
export function buildSampleSections() {
  const basic = newSection('basic')
  basic.content = {
    name: '张三',
    job: 'Java 后端工程师',
    phone: '138-0000-0000',
    email: 'zhangsan@example.com',
    city: '上海',
    website: 'github.com/zhangsan',
    summary: '3 年后端开发经验，熟悉 Spring Boot、MyBatis、MySQL，热衷构建高性能、易维护的服务。'
  }

  const education = newSection('education')
  education.content = {
    list: [
      { school: '某某大学', major: '计算机科学与技术', degree: '本科', start: '2015-09', end: '2019-06', description: '主修数据结构、操作系统、数据库原理，连续三年获得校级奖学金。' },
      { school: '某某高中', major: '理科', degree: '', start: '2012-09', end: '2015-06', description: '' }
    ]
  }

  const experience = newSection('experience')
  experience.content = {
    list: [
      { company: '星辰科技有限公司', position: '高级 Java 工程师', start: '2021-07', end: '至今', description: '负责订单中心微服务重构，QPS 提升 3 倍；主导引入 MyBatis-Plus 与统一日志规范。' },
      { company: '启航软件', position: 'Java 工程师', start: '2019-07', end: '2021-06', description: '参与 ERP 系统后端开发，负责权限模块与报表导出功能。' }
    ]
  }

  const project = newSection('project')
  project.content = {
    list: [
      { name: '在线简历编辑器', role: '独立开发', start: '2024-03', end: '2024-06', link: 'github.com/zhangsan/resume', description: 'Vue3 + Spring Boot 全栈项目，支持拖拽排版与 Markdown 编辑。' },
      { name: '数据中台', role: '核心成员', start: '2022-01', end: '2022-12', link: '', description: '面向业务线的数据接入与查询平台，日均处理数据量 2 亿条。' }
    ]
  }

  const skill = newSection('skill')
  skill.content = {
    list: [
      { name: 'Java / Spring Boot', level: 5 },
      { name: 'MyBatis / MySQL', level: 4 },
      { name: 'Redis / RabbitMQ', level: 4 },
      { name: 'Vue / 前端基础', level: 3 },
      { name: 'Docker / K8s', level: 3 }
    ]
  }

  const md = newSection('markdown')
  md.title = '自我评价'
  md.content = {
    markdown: '- 责任心强，乐于钻研技术细节\n- 具备良好的团队协作与沟通能力\n- 英语读写熟练，可查阅英文技术文档'
  }

  return [basic, education, experience, project, skill, md]
}
