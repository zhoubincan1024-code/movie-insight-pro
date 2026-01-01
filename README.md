# Movie Insight Pro 🎬

一个基于 Vue 3 + Spring Boot 的电影舆情数据分析系统，提供数据采集、存储、分析和可视化功能。

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Vue](https://img.shields.io/badge/Vue-3.5.24-4FC08D?logo=vue.js)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=spring)](https://spring.io/projects/spring-boot)
[![Python](https://img.shields.io/badge/Python-3.x-3776AB?logo=python)](https://www.python.org/)

## 📋 目录

- [项目简介](#项目简介)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
  - [环境要求](#环境要求)
  - [数据库配置](#数据库配置)
  - [后端启动](#后端启动)
  - [前端启动](#前端启动)
  - [数据采集](#数据采集)
- [API 文档](#api-文档)
- [项目截图](#项目截图)
- [开发计划](#开发计划)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

## 🎯 项目简介

Movie Insight Pro 是一个全栈电影数据分析平台，通过爬取豆瓣电影 Top 250 数据，结合情感分析和数据可视化技术，为用户提供直观的电影舆情洞察。系统采用前后端分离架构，支持实时数据采集、智能搜索和深度分析。

## ✨ 功能特性

### 数据采集
- 🕷️ 自动化爬取豆瓣 Top 250 电影数据
- 📝 采集电影基本信息（标题、评分、标签、短评）
- 🔄 自动情感倾向判断（正面/负面/中性）
- 🛡️ 反爬虫机制防护（随机 User-Agent、请求延时）

### 数据管理
- 💾 MySQL 数据库持久化存储
- 🔍 多维度搜索（按标题、标签搜索）
- 📊 数据清洗与校验
- 🗄️ RESTful API 接口

### 数据分析
- 📈 评分分布统计
- 💭 情感分析可视化
- 🏷️ 标签分类统计
- 📉 电影舆情趋势分析

### 数据可视化
- 📊 ECharts 交互式图表
- ☁️ 词云图展示
- 🎨 现代化 UI 设计（Tailwind CSS）
- 📱 响应式布局

## 🛠️ 技术栈

### 后端
- **Java 17** - 编程语言
- **Spring Boot 3.3.4** - 应用框架
- **Spring Data JPA** - 数据访问层
- **MySQL** - 关系型数据库
- **Lombok** - 代码简化工具

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - 类型安全的 JavaScript 超集
- **Pinia** - 状态管理
- **Tailwind CSS** - 实用优先的 CSS 框架
- **ECharts** - 数据可视化图表库
- **Vite** - 下一代前端构建工具
- **Axios** - HTTP 客户端

### 数据采集
- **Python 3** - 爬虫脚本语言
- **BeautifulSoup4** - HTML 解析库
- **Requests** - HTTP 请求库

## 📁 项目结构

```
movie-insight-pro/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/demo/
│   │   │   │   ├── controller/    # 控制器层
│   │   │   │   ├── service/       # 业务逻辑层
│   │   │   │   ├── repository/    # 数据访问层
│   │   │   │   ├── entity/        # 实体类
│   │   │   │   └── DemoApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                 # 测试代码
│   └── pom.xml                  # Maven 依赖配置
├── frontend/                    # 前端项目
│   ├── src/
│   │   ├── components/          # Vue 组件
│   │   │   ├── charts/          # 图表组件
│   │   │   ├── layout/          # 布局组件
│   │   │   └── modals/          # 弹窗组件
│   │   ├── stores/              # Pinia 状态管理
│   │   ├── styles/              # 样式文件
│   │   ├── App.vue
│   │   └── main.ts
│   ├── package.json
│   └── vite.config.js
├── spider.py                   # Python 爬虫脚本
├── .gitignore
└── README.md
```

## 🚀 快速开始

### 环境要求

- **JDK 17+**
- **Maven 3.6+**
- **Node.js 16+**
- **Python 3.8+**
- **MySQL 8.0+**

### 数据库配置

1. 创建数据库（可选，系统会自动创建）：

```sql
CREATE DATABASE IF NOT EXISTS movie_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改后端配置文件 `backend/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/movie_db?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
server.port=8081
```

### 后端启动

1. 进入后端目录：

```bash
cd backend
```

2. 使用 Maven 编译运行：

```bash
mvn clean install
mvn spring-boot:run
```

或者使用 IDE 直接运行 `DemoApplication.java`

3. 后端服务将在 `http://localhost:8081` 启动

### 前端启动

1. 进入前端目录：

```bash
cd frontend
```

2. 安装依赖：

```bash
npm install
```

3. 启动开发服务器：

```bash
npm run dev
```

4. 前端应用将在 `http://localhost:5173` 启动（或 Vite 显示的端口）

### 数据采集

1. 确保后端服务已启动

2. 安装 Python 依赖：

```bash
pip install requests beautifulsoup4
```

3. 运行爬虫脚本：

```bash
python spider.py
```

4. 爬虫会自动将数据同步到后端数据库

> ⚠️ **注意**：爬虫脚本包含请求延时和反爬虫机制，全量采集 250 部电影可能需要较长时间。请遵守网站使用规范，避免频繁请求。

## 📚 API 文档

### 获取所有电影

```http
GET /api/movies
```

**响应示例：**
```json
[
  {
    "id": 1,
    "title": "肖申克的救赎",
    "rating": 9.7,
    "sentiment": "正面",
    "tags": "剧情,犯罪",
    "review": "希望让人自由。",
    "createTime": "2024-01-01T12:00:00"
  }
]
```

### 搜索电影

```http
GET /api/movies/search?keyword=科幻
```

**参数：**
- `keyword` (string): 搜索关键词（标题或标签）

### 电影情感分析

```http
GET /api/movies/analysis?title=流浪地球2
```

**参数：**
- `title` (string): 电影标题

**响应示例：**
```json
{
  "title": "流浪地球2",
  "totalCount": 1,
  "positiveCount": 1,
  "negativeCount": 0,
  "neutralCount": 0,
  "positiveRatio": 100.0,
  "negativeRatio": 0.0,
  "neutralRatio": 0.0
}
```

### 保存电影数据

```http
POST /api/movies
Content-Type: application/json

{
  "title": "电影名称",
  "rating": 9.5,
  "sentiment": "正面",
  "tags": "科幻,动作",
  "review": "电影评论内容"
}
```

## 📸 项目截图

> 提示：项目包含以下可视化界面
> - 响应式仪表盘
> - 评分分布图表
> - 情感分析饼图
> - 词云图展示
> - 电影数据表格

## 🔮 开发计划

- [ ] 用户认证与授权
- [ ] 评论情感分析优化（引入 NLP 模型）
- [ ] 数据导出功能（Excel/PDF）
- [ ] 定时任务自动采集
- [ ] 电影推荐算法
- [ ] 多数据源支持
- [ ] 国际化支持

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 👤 作者

**zhoubincan1024-code**

- GitHub: [@zhoubincan1024-code](https://github.com/zhoubincan1024-code)

## 🙏 致谢

- [豆瓣电影](https://movie.douban.com/) - 数据来源
- [Vue.js](https://vuejs.org/) - 前端框架
- [Spring Boot](https://spring.io/projects/spring-boot) - 后端框架
- [ECharts](https://echarts.apache.org/) - 数据可视化

---

⭐ 如果这个项目对你有帮助，请给个 Star！

