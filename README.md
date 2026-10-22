# 简历工坊 Resume Builder

在线简历编辑器：通过 **拖拽组件 + 表单填充 + Markdown** 三种方式制作简历，实时预览，一键导出 PDF。

前后端单仓库（Monorepo）：

- `frontend/` — Vue 3 + Element Plus + Axios + Vite
- `backend/` — Spring Boot 3 + MyBatis + MySQL 8
- `sql/` — 建库脚本

## 功能特性

- **拖拽排版**：左侧组件库拖入中间画布自由排序，支持隐藏 / 删除板块
- **表单填充**：基本信息、教育 / 工作 / 项目经历（多条动态增删）、技能等级评分
- **Markdown 板块**：自由文本区块，md-editor-v3 编辑
- **实时预览**：编辑器右侧随改随变
- **多主题皮肤**：经典黑白 / 商务蓝 / 清新绿
- **PDF 导出**：A4 版式导出 PDF，或直接浏览器打印
- **登录注册**：BCrypt 密码加密 + Token 鉴权，简历数据按用户隔离
- **示例简历**：一键生成带演示数据的简历，快速上手

## 技术栈

| 端 | 技术 |
| --- | --- |
| 前端 | Vue 3、Element Plus、Vue Router、Axios、vuedraggable、md-editor-v3、html2pdf.js、Vite |
| 后端 | Spring Boot 3.3、MyBatis、MySQL 8、Spring Security Crypto（BCrypt）、Lombok |

## 项目结构

```text
├── sql/schema.sql          # 建库建表脚本
├── backend/                # Spring Boot 后端（端口 8080）
│   └── src/main/java/com/example/resume/
│       ├── common/         # Result 统一返回、全局异常、UserContext
│       ├── config/         # CORS、Token 鉴权拦截器
│       ├── controller/     # /api/user、/api/resume
│       ├── service/        # 用户、简历业务逻辑
│       ├── mapper/         # MyBatis 注解式 Mapper
│       └── entity/ vo/
└── frontend/               # Vue 3 前端（端口 5173）
    └── src/
        ├── api/            # axios 封装（Token 注入、401 拦截）
        ├── views/          # Login / ResumeList / Editor / Preview
        ├── components/     # ResumePreview（渲染）、SectionForm（表单）
        └── utils/          # 板块元数据、示例数据
```

## 快速开始

### 环境要求

- JDK 17+、Maven 3.6+
- Node.js 16+（建议 18+）
- MySQL 8.x

### 1. 初始化数据库

```bash
mysql -u root -p < sql/schema.sql
```

自动创建 `resume_builder` 库及 `user` / `resume` / `resume_section` 三张表。

### 2. 启动后端（端口 8080）

```bash
cd backend
# 数据库默认连接本机 root/123456，可修改 application.yml
# 或用环境变量覆盖：DB_USERNAME / DB_PASSWORD
mvn spring-boot:run
```

### 3. 启动前端（端口 5173）

```bash
cd frontend
npm install
npm run dev
```

打开 http://localhost:5173 ，注册账号即可开始使用。

## 使用说明

1. 注册 / 登录后进入「我的简历」
2. 点「新建简历」选择主题，或「新建示例简历」快速体验
3. 编辑器左侧组件库**拖入中间画布**排序；点「编辑」在抽屉中用表单 / Markdown 填充内容
4. 右侧实时预览，顶部工具栏可切换主题皮肤
5. 「预览 / 导出」页面可导出 A4 PDF 或直接打印
