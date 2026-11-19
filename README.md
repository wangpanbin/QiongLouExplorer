# 琼楼寻踪 · QiongLou Explorer

> 基于 Android 的海口骑楼历史文化探索与数字导览平台
> **探寻古韵，遇见历史**

[![Platform](https://img.shields.io/badge/platform-Android%2024%2B-7C5CFF)](#)
[![Language](https://img.shields.io/badge/language-Java%2011-5B3FD9)](#)
[![Version](https://img.shields.io/badge/version-v0.1.0-8A849B)](#)

---

## 这是什么

琼楼寻踪是一个 Android 原生文化探索 App，围绕海口骑楼历史文化街区，把**线下场景**与**线上文化内容**连接起来：

**发现文化地点 → 查看历史介绍 → 地图导航 → 扫描二维码 → 深度讲解 → 完成探索任务 → 获得文化勋章**

## V0.1 已实现

| 模块 | 能力 |
|---|---|
| 启动页 | 品牌视觉与入口 |
| 首页 | 搜索、Banner 专题、四宫格入口、热门推荐、附近列表、消息红点 |
| 文化地图 | 自绘街区底图、点位气泡、选中卡、距离计算、唤起系统导航 |
| 景点详情 | 封面、评分、简介/任务/评论三 Tab、收藏、关联点位 |
| 扫码寻宝 | ZXing 扫码 `QIONGLOU:POINT:<id>`，含手动编号降级 |
| 任务挑战 | 任务列表、进度、提交发奖（经验 / 积分） |
| AI 导览 | 本地文化知识库规则问答（事实 / 推荐 / 路线） |
| 个人中心 | 经验、打卡、勋章统计；我的任务、我的收藏、离线资源、设置 |
| 消息通知 | 系统 / 好友两段式列表，未读红点 |
| 离线模式 | 详情缓存、断网可读、打卡待同步队列、手动同步 |

**零外部依赖**：不需要地图 Key、不需要大模型 Key、不需要后端与网络。

## 快速开始

```bash
# 环境：JDK 17+、Android SDK（compileSdk 37）

# 1. 配置 SDK 路径
echo "sdk.dir=D\:\\\\dev\\\\sdk" > local.properties   # 改成你本机的 SDK 路径

# 2. 编译
./gradlew assembleDebug

# 3. 单测
./gradlew test

# 4. 安装（连真机或启动模拟器后）
./gradlew installDebug
```

产物：`app/build/outputs/apk/debug/app-debug.apk`

## 项目结构

```
QiongLouExplorer/
├── app/src/main/
│   ├── java/com/bin/qionglouexplorer/
│   │   ├── MainActivity.java           # 5 Tab 容器
│   │   ├── ui/
│   │   │   ├── splash/                 # 启动页
│   │   │   ├── home/                   # 首页
│   │   │   ├── map/                    # 地图 + MapCanvasView 自绘
│   │   │   ├── detail/                 # 景点详情
│   │   │   ├── scan/                   # 扫码寻宝
│   │   │   ├── task/                   # 任务挑战
│   │   │   ├── assistant/              # AI 导览 + AssistantEngine
│   │   │   ├── message/                # 消息通知
│   │   │   └── profile/                # 个人中心 / 收藏 / 离线
│   │   ├── data/
│   │   │   ├── local/                  # Room：entity / dao / AppDatabase
│   │   │   ├── repo/                   # Repository 层（面向接口，可换 Remote）
│   │   │   └── seed/                    # 种子数据导入
│   │   ├── adapter/
│   │   └── util/
│   ├── assets/seed_points.json         # 文化点位种子数据
│   └── res/                            # layout / drawable / values
├── docs/
│   ├── PRD.md                          # 产品需求文档
│   └── MVP.md                          # V0.1 范围与里程碑
└── plan.md                             # V1.0 完整产品方案（原始蓝图）
```

## 架构决策记录

### 1. 为什么 V0.1 不做后端

`plan.md` 定义的是 Android + Spring Boot + MySQL + Web 后台的完整体系。MVP 阶段的判断是：**先证明用户旅程成立，再补基础设施**。
所有数据访问都收敛在 `data/repo/*Repository`，V0.2 加 `data/remote/` + Retrofit 即可替换数据源，UI 层零改动。

### 2. 为什么地图自绘

高德 SDK 需要 Key + 包名/SHA1 绑定 + 隐私合规配置，任一环节缺失都会导致首次运行黑屏。
V0.1 用 `MapCanvasView` 自绘街区示意图：零 Key、零网络、可离线、编译即跑。
导航职责交给系统地图 App（`Intent geo:`），这本来也是更符合用户习惯的做法。

### 3. 为什么 AI 用本地规则引擎

大模型回答历史事实存在编造风险，而本项目的底线是**文化资料不能编**。
V0.1 的 `LocalRuleAssistant` 只从库内字段组装答案，永不生成库外内容；`AssistantRepository` 是接口，V0.2 换 RAG 实现时 UI 与调用方不变。

### 4. 底部导航 5 栏 vs 原型 4 栏

原型是「首页/地图/消息/我的」。V0.1 改为「首页/地图/任务/助手/我的」：任务与助手是答辩核心亮点，藏进二级入口会被忽略；消息移到首页右上角并保留未读红点，可达性不降低。

### 5. 配图用矢量占位

所有景点封面为自绘渐变 + 地标图标，规避版权风险并控制包体。字段 `coverRes` 已设计为可切换到网络图片 URL。

## 数据说明

`app/src/main/assets/seed_points.json` 中的文化点位基于海口骑楼老街公开地标整理（骑楼老街、中山路、水巷口、得胜沙路、海口天后宫、西天庙、五层楼、博爱路、新华北路、海口钟楼），**用于课程设计与功能演示，未经地方文献逐条核校**。每条记录带 `sourceNote` 字段标注待核实项。正式上线前必须完成资料核实与图片授权。

## 测试

| 测试类型 | 内容 | 命令 |
|---|---|---|
| 单元测试 | `GeoUtils` Haversine 距离、`AssistantEngine` 关键词意图命中 | `./gradlew test` |
| 手工验收 | PRD 第 11 节 10 条验收场景 | 见 `docs/PRD.md` |

## 版本规划

- **v0.1.0（当前）** — 本地完整探索闭环，Room 持久化，离线可读
- **v0.2.0** — Spring Boot + MySQL + REST、账号体系、RAG 知识库
- **v0.3.0** — Web 管理后台、社区、勋章体系、真实地图 SDK

## 文档

- [产品需求文档 docs/PRD.md](docs/PRD.md)
- [MVP 范围与里程碑 docs/MVP.md](docs/MVP.md)
- [V1.0 完整方案 plan.md](plan.md)

## 许可

课程设计项目，仅供学习与评审使用。
