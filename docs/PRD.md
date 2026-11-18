# 琼楼寻踪 · 产品需求文档（PRD）

| 项 | 内容 |
|---|---|
| 产品名称 | 琼楼寻踪 |
| 英文名 | QiongLou Explorer |
| 版本 | V0.1（MVP 迭代基线） |
| 文档版本 | PRD V1.0 |
| 定位 | 基于 Android 的海口骑楼历史文化探索与数字导览平台 |
| 载体 | Android App（Java + XML） |
| 日期 | 2026 |

> 本文档基于 `plan.md`（V1.0 完整方案）裁剪而来：保留完整产品的产品逻辑与数据模型，把交付范围收敛到 V0.1 可在本机零外部依赖跑通的一条完整用户旅程。范围裁剪明细见 `docs/MVP.md`。

---

## 1. 项目背景

海口骑楼老街是海南具有代表性的历史文化街区，其建筑风貌融合南洋建筑特色与本地城市文化。传统旅游信息展示存在以下问题：

1. 景点信息分散，游客需要在多个平台之间切换。
2. 历史建筑的文化背景难以通过普通观光直观了解。
3. 线下导览与线上文化资料缺乏关联。
4. 游客缺少参与式文化探索体验。
5. 传统文化传播形式相对单一，对年轻群体吸引力有限。

因此本项目以海口骑楼文化为核心，将线下文化场景与线上数字内容连接起来。

## 2. 产品定位

| 项目 | 定义 |
|---|---|
| 产品类型 | 地方文化探索与数字导览平台 |
| 目标地区 | 海南省海口市骑楼历史文化街区 |
| 主要用户 | 游客、本地居民、文化爱好者 |
| 核心载体 | Android App |
| 后续载体 | Web 管理后台（V0.2）、Spring Boot 业务服务（V0.2） |

**核心理念：让每一座骑楼都有故事，让每一次探索都能留下文化记忆。**

## 3. 产品目标

**用户目标**
- 方便游客了解海口骑楼文化。
- 帮助用户发现附近的历史文化点位。
- 提供更有趣的参与式文化探索体验。
- 降低获取文化知识的门槛（V0.1 用本地知识库问答验证交互，V0.2 换 RAG）。

**业务目标**
- 建立统一的海口地方文化资源库（V0.1 以种子数据集形式落地）。
- 实现文化点位数字化管理。
- 建立线上线下联动的文化传播体系（二维码导览）。

**技术目标**
- 掌握 Android 原生开发流程与 MVVM 分层。
- 掌握 Room 本地数据持久化与离线优先设计。
- 为后续前后端分离、RAG 知识库预留可替换接口。

## 4. 目标用户分析

| 用户 | 画像 | 核心需求 | V0.1 覆盖 |
|---|---|---|---|
| A 外地游客 | 停留 1–2 天，时间紧 | 快速了解景点、规划路线、获取讲解、拍照打卡 | 首页发现、地图选点、图文导览、扫码打卡、系统导航 |
| B 本地居民 | 熟悉街区，想了解历史 | 了解本地历史、发现特色地点 | 详情页文化解读、周边点位推荐 |
| C 文化爱好者 | 对建筑史、民俗有深度兴趣 | 深入了解建筑年代、风格、人物故事 | 详情页「简介」Tab 的结构化字段 + 助手问答 |
| D 平台管理员 | 内容运营 | 维护文化资料、点位、任务 | 暂不覆盖（V0.2 Web 后台） |

### 关键用户旅程（用户旅程即产品灵魂）

> 发现文化地点 → 查看历史介绍 → 地图导航 → 扫描二维码 → 深度讲解 → 完成探索任务 → 获得文化勋章 → 分享探索经历

V0.1 完整跑通前 7 步；「分享」留空态入口。

## 5. 功能矩阵与优先级

优先级定义：**P0 = V0.1 必须交付**，P1 = V0.2 交付，P2 = 有余力再做。

| 模块 | 功能点 | 优先级 | V0.1 状态 |
|---|---|---|---|
| 启动与品牌 | 启动页、品牌标语、开始探索 | P0 | 交付 |
| 首页发现 | 搜索、Banner 专题、四宫格入口、热门推荐、附近列表 | P0 | 交付 |
| 文化地图 | 点位展示、分类筛选、选中点位卡、距离计算、唤起系统导航 | P0 | 交付（自绘地图） |
| 数字导览 | 图文详情、结构化字段、收藏、关联点位 | P0 | 交付 |
| 数字导览 | 语音讲解音频播放 | P1 | 不交付（留按钮占位） |
| 二维码导览 | 扫 `QIONGLOU:POINT:<id>` → 详情 + 打卡、手动编号降级 | P0 | 交付 |
| 文化探索 | 任务列表、进度、提交打卡、经验/积分 | P0 | 交付 |
| 文化探索 | 勋章定义体系与授勋动画 | P1 | 仅统计数字 |
| AI 文化助手 | 本地知识库规则问答、快捷问题 | P0 | 交付（规则引擎） |
| AI 文化助手 | RAG + 大模型生成、流式回答 | P1 | 不交付（接口已预留） |
| 消息通知 | 系统通知 / 互动消息列表、未读红点 | P0 | 交付（只读） |
| 消息通知 | WebSocket 实时推送 | P2 | 不交付 |
| 个人中心 | 经验/打卡/勋章统计、我的任务、我的收藏、离线资源、设置 | P0 | 交付 |
| 离线模式 | 详情预缓存、断网可读、打卡待同步队列、手动同步 | P0 | 交付 |
| 用户社区 | 游记发布、评论、点赞 | P2 | 不交付（详情页评论 Tab 空态） |
| 用户认证 | 注册登录、Token | P1 | 不交付（本地默认档案，单用户） |
| Web 管理后台 | 点位/任务/用户/数据管理 | P1 | 不交付 |
| 后端服务 | Spring Boot + MySQL + REST | P1 | 不交付（Repository 层预留） |

## 6. 信息架构

```
启动页 SplashActivity
└── 主框架 MainActivity（BottomNavigationView 5 栏）
    ├── Tab1 首页 HomeFragment
    │   ├── 搜索 → 本地点位检索
    │   ├── 四宫格 → 景点导览 / 任务挑战 / 地图探索 / 我的
    │   ├── 热门推荐 → DetailActivity
    │   ├── 附近列表 → DetailActivity
    │   └── 右上角消息 → MessageActivity（未读红点）
    ├── Tab2 地图 MapFragment
    │   └── 选中点位卡 → DetailActivity / 系统导航
    ├── Tab3 任务 TaskFragment
    │   └── 立即挑战 → DetailActivity / ScanActivity
    ├── Tab4 助手 AssistantFragment
    ├── Tab5 我的 ProfileFragment
    │   ├── 我的任务 → MyTaskActivity
    │   ├── 我的收藏 → FavoriteActivity
    │   ├── 离线地图 → OfflineActivity
    │   └── 设置
    ├── DetailActivity（简介 / 任务 / 评论 三 Tab）
    └── ScanActivity（扫码 / 手动编号）
```

### 与高保真原型的两处有意偏离

| 偏离 | 原型 | V0.1 实现 | 理由 |
|---|---|---|---|
| 底部导航 | 4 栏：首页/地图/消息/我的 | **5 栏：首页/地图/任务/助手/我的** | `plan.md` 要求五栏；任务与助手是答辩核心亮点，放二级入口会被忽略；消息改为首页右上角入口 + 未读红点，信息可达性不降低 |
| 景点配图 | 真实建筑照片 | 矢量渐变占位图 + 地标图标 | 规避图片版权风险、控制 APK 体积；字段 `coverRes` 已可平滑替换为网络图 URL |

## 7. 核心页面用例

### 7.1 启动页
- 展示品牌插画式渐变背景、产品名「琼楼寻踪」、标语「— 探寻古韵，遇见历史 —」、主按钮「开始探索」。
- 点击进入主框架首页；首次启动前完成种子数据注入。

### 7.2 首页
- 搜索框：按点位名称 / 标签 / 简介模糊匹配，结果以内联列表展示。
- Banner「探秘古城 · 开启你的寻踪之旅」：点击进入骑楼老街详情（id 最小点位）。
- 四宫格：景点导览（列表）、任务挑战（切 Tab3）、地图探索（切 Tab2）、我的（切 Tab5）。
- 热门推荐：横向滑动卡片（名称、分类、评分、访问量）。
- 附近推荐：纵向列表，按与用户当前位置距离升序。
- 消息入口：右上图标，存在未读时显示红点。

### 7.3 地图页
- 自绘街区底图：路网网格 + 街区色块 + 水系示意，标注「骑楼历史文化街区」。
- 点位气泡：选中态放大并高亮，其余为小圆点，容错半径 48dp。
- 用户定位点：以街区中心为默认锚点（V0.1 不申请定位权限），用于计算距离。
- 底部点位卡：名称、分类、评分、距你 xxx 米、「开始导航」（`Intent.ACTION_VIEW` + `geo:` 唤起系统地图）。
- 筛选芯片：全部 / 建筑 / 历史 / 民俗 / 美食。

### 7.4 景点详情
- 顶部封面图 + 返回、收藏、更多。
- 标题区：名称、评分、访问量、一句话摘要。
- Tab：**简介**（年代、风格、地址、结构化介绍段落、标签）、**任务**（关联任务 + 立即挑战）、**评论**（V0.1 空态「评论功能将在 V0.2 开放」）。
- 底部操作条：扫码打卡、导航到这里。
- 离线提示条：网络不可用时顶部展示「当前为离线模式，内容来自本地缓存」。
- 关联点位：同分类的其他点位横滑推荐。

### 7.5 扫码寻宝
- 相机扫码，解析 `QIONGLOU:POINT:<id>`。
- 解析成功 → 写入打卡记录（`checkinType=CAMERA_SCAN`）→ 跳转详情并提示「打卡成功，经验 +N」。
- 解析失败/非本站二维码 → 提示「这不是琼楼寻踪的文化点位二维码」，停留在扫码页。
- 手动编号降级入口：输入纯数字或完整码，供无相机权限/模拟器演示使用。
- 相机权限被拒绝时，直接降级到手动输入，不阻塞流程。

### 7.6 任务挑战
- 顶部进度条「已完成 x / 总任务 y」。
- 任务卡：目标点位、任务标题、任务说明、奖励（经验 / 积分）、状态标签（可挑战 / 已完成）。
- 「立即挑战」→ 该点位详情或扫码页；「提交任务」→ 写打卡 + 加经验积分 + 生成系统通知。

### 7.7 AI 文化助手（V0.1 规则引擎）
- 支持三类意图：**点位事实问答**（年代/风格/地址/介绍）、**点位推荐**（「推荐几个必看的地方」）、**路线建议**（「半天怎么逛」）。
- 关键词命中 → 从本地知识库组装答案；未命中 → 兜底话术 + 引导到热门点位。
- 快捷问题芯片：3–4 个可点击提问。
- 语音按钮为占位（点击提示「语音问答将在 V0.2 开放」）。
- 回答必须引用库内字段，**不得编造**；每条答案末尾标注「内容来自本地文化知识库」。

### 7.8 个人中心
- 头像、昵称、等级（由经验值换算）、个性签名。
- 三统计：经验值 / 打卡地点数 / 获得的勋章数。
- 入口：我的任务、我的收藏、离线地图、设置。
- 设置项：清除本地数据（重置为种子数据）、关于。

### 7.9 消息通知
- 系统通知 / 好友消息 两段式列表，按时间倒序。
- 未读消息带圆点，进入页面后自动置为已读，首页红点同步消失。
- 消息来源：种子数据（欢迎、活动预告）+ 运行时生成（任务完成、打卡成功、同步完成）。

### 7.10 离线资源（二级页）
- 已缓存点位数、离线可读内容体积（估算）、待同步打卡数。
- 「立即同步」→ 模拟服务端校验，将 `PENDING` 打卡批量置为 `SYNCED`，并生成一条系统通知。

## 8. 数据模型

V0.1 使用 Room（SQLite）。字段命名与 `plan.md` 4.3 的 MySQL 表保持一一映射，V0.2 可直接迁移到 MySQL。

### 8.1 ER 关系

```
profile(1) ──< checkin >── (1)point
profile(1) ──< favorite >── (1)point
profile(1) ──< message
point(1) ──< task        （一个点位一个挑战任务）
```

### 8.2 表结构

**culture_point**

| 字段 | 类型 | 说明 | MySQL 映射 |
|---|---|---|---|
| id | INTEGER PK | 点位 ID | BIGINT PK AUTO_INCREMENT |
| name | TEXT | 名称 | VARCHAR(100) |
| category | TEXT | 分类 code | category_id → culture_category |
| era | TEXT | 建筑年代 | VARCHAR(50) |
| style | TEXT | 建筑风格 | VARCHAR(50) |
| address | TEXT | 地址 | VARCHAR(255) |
| summary | TEXT | 一句话摘要 | VARCHAR(500) |
| description | TEXT | 详细介绍 | TEXT |
| tags | TEXT | 逗号分隔标签 | VARCHAR(255) |
| latitude | REAL | 纬度（GCJ-02 预留） | DECIMAL(10,7) |
| longitude | REAL | 经度 | DECIMAL(11,7) |
| rating | REAL | 评分 | DECIMAL(2,1) |
| visitCount | INTEGER | 访问量 | INT |
| coverRes | TEXT | 封面资源标识 | cover_image VARCHAR(255) |
| sourceNote | TEXT | 资料出处/待核实说明 | VARCHAR(255) |
| cached | INTEGER | 是否已离线缓存 | TINYINT |
| status | INTEGER | 发布状态 | TINYINT |

**exploration_task**

| 字段 | 类型 | 说明 |
|---|---|---|
| id | INTEGER PK | 任务 ID |
| pointId | INTEGER | 关联点位 |
| title | TEXT | 任务标题 |
| description | TEXT | 任务说明 |
| rewardExp | INTEGER | 奖励经验 |
| rewardPoints | INTEGER | 奖励积分 |

**checkin_record**

| 字段 | 类型 | 说明 |
|---|---|---|
| id | INTEGER PK AI | 记录 ID |
| pointId | INTEGER | 点位 ID |
| taskId | INTEGER | 任务 ID（可为 0） |
| checkinType | TEXT | `CAMERA_SCAN` / `MANUAL_CODE` / `TASK_SUBMIT` |
| checkinTime | INTEGER | 时间戳 |
| syncStatus | TEXT | `PENDING` / `SYNCED` |
| rewardExp | INTEGER | 本次奖励经验 |
| rewardPoints | INTEGER | 本次奖励积分 |

> 去重规则：同一 `(pointId, taskId)` 只允许一条有效记录，重复提交走业务提示而非新增。

**favorite**

| 字段 | 类型 | 说明 |
|---|---|---|
| pointId | INTEGER PK | 点位 ID |
| createdAt | INTEGER | 收藏时间 |

**user_message**

| 字段 | 类型 | 说明 |
|---|---|---|
| id | INTEGER PK AI | 消息 ID |
| type | TEXT | `SYSTEM` / `FRIEND` |
| title | TEXT | 标题 |
| content | TEXT | 正文 |
| timeLabel | TEXT | 展示用相对时间 |
| isRead | INTEGER | 0/1 |

**user_profile**（单行）

| 字段 | 类型 | 说明 |
|---|---|---|
| id | INTEGER PK | 固定为 1 |
| nickname | TEXT | 昵称 |
| signature | TEXT | 个性签名 |
| avatarRes | TEXT | 头像资源标识 |
| exp | INTEGER | 累计经验 |
| points | INTEGER | 累计积分 |
| badgeCount | INTEGER | 勋章数 |

### 8.3 索引

- `favorite`：`pointId` 主键天然唯一。
- `checkin_record`：`(pointId, taskId)` 唯一索引（防重复打卡）。
- `user_message`：`(isRead)` 索引（未读计数）。

## 9. 接口预留清单（V0.2 后端）

V0.1 全部由 `data/repo` 下的 Repository 以 Room 实现，接口签名即未来 REST 契约。

| 方法 | 路径 | Repository 方法 |
|---|---|---|
| GET | /api/v1/culture/points | `CultureRepository.observePoints()` |
| GET | /api/v1/culture/points/{id} | `CultureRepository.observePoint(id)` |
| GET | /api/v1/culture/points/{id}/related | `CultureRepository.observeRelated(id)` |
| GET | /api/v1/culture/categories | `CultureRepository.categories()` |
| GET | /api/v1/exploration/tasks | `TaskRepository.observeTasks()` |
| POST | /api/v1/exploration/checkin | `TaskRepository.submitTask(pointId, taskId, type)` |
| GET | /api/v1/users/me | `ProfileRepository.observeProfile()` |
| POST | /api/v1/users/me/favorites | `CultureRepository.toggleFavorite(pointId)` |
| GET | /api/v1/messages | `MessageRepository.observeMessages()` |
| POST | /api/v1/ai/chat | `AssistantRepository.ask(question)` |

统一响应结构沿用 `plan.md` 第六部分：`{ code, message, data, timestamp }`。

## 10. 非功能需求

| 类别 | 要求 |
|---|---|
| 性能 | 冷启动到首页 ≤ 2s（中端机）；列表滚动 60fps；Room 查询均在 IO 线程 |
| 兼容性 | minSdk 24（Android 7.0）、targetSdk 37；竖屏为主，无横屏适配要求 |
| 离线 | 无网络时首页/详情/收藏/离线页全部可读，不出现空白页或崩溃 |
| 数据安全 | 不采集定位与个人信息；V0.1 无账号体系，数据仅存本地 |
| 可访问性 | 全部可点元素 ≥ 48dp；文字对比度满足 WCAG AA；关键图标带 contentDescription |
| 国际化 | V0.1 仅简体中文，文案集中在 `strings.xml` |
| 可维护性 | 分层：ui / data（entity, dao, repo）/ adapter / util；业务逻辑不入 Activity |

## 11. 验收标准

| # | 场景 | 通过标准 |
|---|---|---|
| 1 | 首次启动 | 启动页 → 首页，种子数据自动注入，无 ANR/崩溃 |
| 2 | 首页发现 | 搜索「中山路」能命中；四宫格跳转正确；热门/附近列表非空 |
| 3 | 地图 | 点位全部绘制；点击点位底部卡更新并显示正确距离；开始导航唤起系统地图 |
| 4 | 详情 | 三 Tab 可切换；收藏后 FAB 状态变化；关联点位可点击 |
| 5 | 扫码 | 手动编号输入 `1001` 或 `QIONGLOU:POINT:1001` 能进入详情并写入打卡 |
| 6 | 任务 | 提交任务后经验/积分增加，任务变「已完成」，消息红点出现 |
| 7 | 助手 | 问「文昌阁建于什么时候」返回该点位年代；问无关内容返回兜底话术 |
| 8 | 个人中心 | 三统计与操作结果一致；我的任务/我的收藏列表正确 |
| 9 | 离线 | 飞行模式下详情/收藏/离线页正常；离线页显示待同步数，立即同步后归零 |
| 10 | 工程质量 | `gradlew assembleDebug` 与 `gradlew test` 全绿 |

## 12. 风险与对策

| 风险 | 说明 | 对策 |
|---|---|---|
| 文化资料准确性 | 历史信息若有误将影响项目严谨性 | 种子数据每条带 `sourceNote`；PRD 与 README 明确「演示数据，正式上线前须经海口地方文献核实」 |
| 图片版权 | 直接使用网络建筑照片存在授权风险 | V0.1 全部使用自绘矢量占位图，字段预留可替换 |
| 高级功能挤占时间 | AI/实时通信/社区易导致核心功能半成品 | 严格按 P0/P1/P2 排期；AI 在 V0.1 只做规则验证交互 |
| 自绘地图能力边界 | 无法做真实路径规划 | 导航交由系统地图 App 承担；地图仅承担「发现与定位感知」职责 |
| 单机数据不可迁移 | V0.1 数据仅在本机 | 数据结构与 MySQL 表一一映射，V0.2 加同步接口即可 |

## 13. 版本演进

- **V0.1（本次交付）**：本地化的完整文化探索闭环，9 屏 + 3 二级页，Room 持久化，离线可读。
- **V0.2**：Spring Boot + MySQL + REST，账号体系，RAG 知识库，图片改为服务端下发。
- **V0.3**：Web 管理后台、社区游记与评论、勋章体系、真实地图 SDK 与路线规划。
