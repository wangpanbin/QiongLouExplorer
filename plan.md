# 琼楼寻踪 V0.1 · PRD + MVP 迭代计划

> 依据：`plan.md`（V1.0 完整方案，1532 行）+ `项目原型/琼楼寻踪智慧文旅应用界面集锦.png`（11 张高保真稿）
> 已确认决策：① 8 屏全做，AI 走本地知识库规则问答，社区/Web 后台留 V0.2 ② 地图自绘可替换 ③ 我造 8–12 条真实点位种子数据 ④ 6 个语义提交

---

## Context

仓库当前状态是 Android Studio 新建工程空壳：

- `app/src/main/java/com/bin/qionglouexplorer/MainActivity.java` — 只有 edge-to-edge 样板代码
- `app/build.gradle.kts` — AGP 9.5.1，compileSdk 37，minSdk 24，Java 11；无任何业务依赖
- **没有 git 仓库**，没有 `docs/`，没有 PRD
- 本机环境已就绪：JDK 21、`D:\dev\sdk`（platform android-37.0、emulator、system-image android-37.0 google_apis_playstore、platform-tools）

要解决的问题：
`plan.md` 是 5 人 8 周、Android + Spring Boot + MySQL + 高德 + AI + Vue 后台的完整课程设计蓝图，**直接照做会做不完且每个模块都是半成品**（plan.md 第十三部分自己也点了这个风险）。本次要做的是：把它压缩成一条能在本机零外部依赖跑通的完整用户旅程，先把"产品骨架 + 文化数据 + 探索闭环"立起来，后端/AI/后台都留好接口缝隙。

预期结果：
1. 仓库有正式 `docs/PRD.md`（可作课程设计报告第一部分）
2. 一个可编译、可安装、可完整演示的 APK（V0.1）
3. 规范化 git 历史（6 个语义提交）

---

## 核心主線（PRD 灵魂，也是答辩演示脚本）

> 启动页 → 首页发现 → 地图选点 → 景点详情（图文导览）→ 扫码寻宝打卡 → 完成任务得经验/积分 → 个人中心看足迹收藏 → 断网仍可读

---

## MVP 边界

### V0.1 纳入（9 屏 + 3 个二级页）

| # | 屏 | 对应原型 | V0.1 实现 | 数据 |
|---|----|----------|-----------|------|
| 1 | 启动页 | 启动页 | 渐变背景 + 品牌 + 「开始探索」 | 本地 drawable |
| 2 | 首页 | 首页 | 搜索框、Banner「探秘古城」、四宫格（景点导览/任务挑战/地图探索/我的）、热门推荐横滑、附近列表、右上消息入口 | Room |
| 3 | 地图 | 地图导航 | **自绘 `MapCanvasView`**：街区底图 + 点位气泡 + 选中高亮 + 用户定位点 + 示意路径线；底部点位卡「距你 xxx 米 / 开始导航」 | Room + Haversine |
| 4 | 景点详情 | 景点详情 | 封面、评分/访问量、简介/任务/评论三 Tab、收藏、扫码打卡按钮、关联点位 | Room |
| 5 | 扫码寻宝 | 扫码寻宝 | `zxing-android-embedded` 扫 `QIONGLOU:POINT:<id>` → 直达详情 + 写打卡；提供「手动输入编号」降级入口 | Room |
| 6 | 任务挑战 | 任务挑战 | 任务列表 + 进度 x/y + 奖励 + 「立即挑战」→ 详情/扫码 | Room |
| 7 | AI 导览 | AI 导览 | 本地知识库规则问答：关键词匹配种子点位 + 兜底话术 + 快捷问题芯片 + 语音按钮（示意）；不接大模型 | Room 的 description/era 字段 |
| 8 | 个人中心 | 个人中心 | 头像昵称、经验/打卡/勋章三统计、我的任务、我的收藏、离线地图、设置 | Room |
| 9 | 消息 | 消息通知 | 系统通知 / 好友消息 两段式列表（只读，红点未读） | Room 种子 |
| A | 离线资源（二级） | 离线使用 | 已缓存点位数、待同步打卡数、「立即同步」模拟联网校验 | Room |
| B | 我的收藏（二级） | — | 收藏点位列表 → 详情 | Room |
| C | 我的任务（二级） | — | 已完成/未完成分组 | Room |

### V0.2+ 推迟（在 PRD 与 README 中显式写明）

- 真实 AI / RAG 知识库（`AssistantRepository` 已按接口预留，换实现即可）
- Spring Boot 后端 + MySQL + REST（`data/repo` 全部面向接口，加 `remote/` 即可）
- Web 管理后台（Vue3 + Element Plus）
- 社区游记/评论/点赞（详情页评论 Tab 留空态）
- 勋章定义体系（V0.1 只显示统计数字）
- WebSocket 实时消息、真实高德路线规划（V0.1 用 `Intent geo:` 唤起系统导航）

### 与原型的两处有意偏离（请在评审时确认可接受）

1. **底部导航改为 5 栏**：首页 / 地图 / 任务 / 助手 / 我的（原型是 4 栏：首页/地图/消息/我的）。理由：`plan.md` 明确要求五栏，且任务与助手是答辩亮点，放二级入口会被忽略。消息改为首页右上角图标 + 未读红点。
2. **景点配图用矢量渐变占位**（`bg_point_*.xml` + 地标图标），不引入真实照片。理由：规避版权风险，也避免 APK 体积。PRD 里记为「素材待替换」。

---

## Approach

单 Activity + Fragment + BottomNavigationView，MVVM 分层（Java + XML，沿用 plan.md 5.3 的目录约定）：

```
com.bin.qionglouexplorer
├── MainActivity.java                  // 承载 5 个 Tab Fragment
├── ui/
│   ├── splash/SplashActivity.java
│   ├── home/HomeFragment.java
│   ├── map/MapFragment.java  (+ MapCanvasView.java 自绘)
│   ├── detail/DetailActivity.java
│   ├── scan/ScanActivity.java
│   ├── task/TaskFragment.java
│   ├── assistant/AssistantFragment.java  (+ AssistantEngine.java 规则问答)
│   └── profile/ProfileFragment.java  (+ OfflineActivity / FavoriteActivity / MyTaskActivity)
│       └── message/MessageActivity.java
├── data/
│   ├── local/AppDatabase.java、PointDao、TaskDao、CheckinDao、FavoriteDao、MessageDao、ProfileDao
│   ├── local/entity/{PointEntity,TaskEntity,CheckinEntity,FavoriteEntity,MessageEntity,ProfileEntity}
│   ├── model/SeedBundle.java（Gson 反序列化 assets/seed_points.json）
│   ├── repo/{CultureRepository,TaskRepository,ProfileRepository,MessageRepository,AssistantRepository(接口)}
│   └── seed/SeedImporter.java
├── adapter/{PointAdapter,TaskAdapter,MessageAdapter,ChatAdapter}.java
└── util/{GeoUtils,PrefsManager,UiUtils}.java
```

**关键可替换点**（为 V0.2 埋缝，答辩时可讲）：

- `AssistantRepository` 是接口，V0.1 = `LocalRuleAssistant`；V0.2 = `RemoteAiAssistant`（RAG）
- `CultureRepository` 只暴露 `LiveData`，V0.2 在内部换成 Retrofit + Room 双源
- 地图若换高德：只替换 `MapFragment` 里的 `MapCanvasView`，其余接口（点位/距离/导航回调）不变

**配色与视觉令牌**（对齐原型）：
- primary `#7C5CFF`（紫），primary variant `#5B3FD9`，背景顶部渐变 `#F3EEFF → #FFF7F2`
- 卡片白底 + 16dp 圆角 + 2dp 高度；正文 `#1F1B2E`，次要文字 `#8A849B`
- 底部导航：选中填充紫、未选中灰；无深色模式覆写（DayNight 保留但只调浅色）

---

## Reuse（复用已有代码/约定，不新造轮子）

| 已有 | 路径 | 用途 |
|------|------|------|
| Material3 DayNight 主题 | `app/src/main/res/values/themes.xml` | 直接在 `Base.Theme.QiongLouExplorer` 上填 `colorPrimary` 等令牌，不新建主题 |
| edge-to-edge 样板 | `MainActivity.java` 的 `WindowCompat.enableEdgeToEdge` + `ViewCompat.setOnApplyWindowInsetsListener` | 保留，移入 BaseActivity 复用 |
| 版本目录 | `gradle/libs.versions.toml` | 所有新依赖按 `[versions]/[libraries]` 登记，禁止硬编码版本 |
| 全局仓库配置 | `settings.gradle.kts` | `FAIL_ON_PROJECT_REPOS` 已开，保持只在 settings 层声明仓库 |
| Gradle 配置缓存 | `gradle.properties` 的 `org.gradle.configuration-cache=true` | 新增插件须兼容配置缓存（Room annotationProcessor 兼容） |
| 现有测试骨架 | `app/src/test/.../ExampleUnitTest.java` | 替换为 `GeoUtilsTest`（Haversine 距离）+ `AssistantEngineTest`（关键词命中），保住 `gradlew test` 绿灯 |

**新增依赖**（全部登记到 `libs.versions.toml`）：`androidx.room:room-runtime:2.6.1` + `annotationProcessor room-compiler:2.6.1`、`androidx.recyclerview`、`androidx.fragment`、`androidx.lifecycle:lifecycle-viewmodel/livedata`、`com.google.android.material`（已有）、`com.journeyapps:zxing-android-embedded:4.3.0`、`com.google.code.gson:gson`。

> Room 固定 2.6.1（Java `annotationProcessor` 路径最稳）；若 AGP 9 报 KSP 相关告警，退化方案是 `androidx.room:room-runtime:2.7.1` + `room-compiler` 走 `annotationProcessor` 并加 `room.schemaLocation`。

---

## Files to modify / create

**改**
- `gradle/libs.versions.toml` — 新增依赖与版本
- `app/build.gradle.kts` — dependencies、`buildConfig`、`javaCompileOptions { annotationProcessorOptions { arguments += mapOf("room.schemaLocation" to ...) } }`
- `app/src/main/AndroidManifest.xml` — 注册 SplashActivity（LAUNCHER）、DetailActivity、ScanActivity、MessageActivity、OfflineActivity、FavoriteActivity、MyTaskActivity；`INTERNET`/`CAMERA` 权限
- `app/src/main/res/values/{colors,strings,themes}.xml` — 设计令牌与全部文案（中文）
- `app/src/main/res/layout/activity_main.xml` — 换成 `FragmentContainerView` + `BottomNavigationView`
- `app/src/main/java/.../MainActivity.java` — 改为 Tab 容器
- `.gitignore` — 补 `build/`、`*.apk`、`*.keystore`、`.kotlin/`
- `app/src/test/java/.../ExampleUnitTest.java` — 替换为真实单测

**建**
- `docs/PRD.md`（详见下节）、`docs/MVP.md`、`README.md`
- `app/src/main/assets/seed_points.json`（8–12 条点位 + 任务 + 消息 + 默认档案）
- `app/src/main/java/com/bin/qionglouexplorer/**`（按上面的目录骨架）
- `app/src/main/res/layout/`：`activity_splash`、`fragment_home`、`fragment_map`、`fragment_task`、`fragment_assistant`、`fragment_profile`、`activity_detail`、`activity_scan`、`activity_message`、`activity_offline`、`activity_favorite`、`activity_mytask`、`item_point`、`item_task`、`item_message`、`item_chat_assistant`、`item_chat_user`
- `app/src/main/res/drawable/`：`bg_gradient_top`、`bg_card_white`、`bg_chip`、`bg_splash`、`bg_point_1..6`、`ic_tab_*.xml`（6 个）、`ic_*.xml`（搜索/消息/扫码/收藏/导航/星星/勋章/音量等）
- `app/src/main/res/menu/menu_bottom_nav.xml`
- `app/src/main/res/values/arrays.xml`（四宫格、快捷提问芯片）

**种子数据（8–12 条真实海口骑楼点位，字段与 `plan.md` 4.3 `culture_point` 对齐，每条带 `sourceNote` 标注「资料待核实」）**
海口骑楼老街、中山路骑楼街区、水巷口、得胜沙路、天后宫、文昌阁、西天庙、五层楼（海口大厦旧址）、南洋骑楼建筑群、博爱路、新华北路、钟楼 → 取 10 条，每条含 `name/category/era/summary/description/tags/lat/lng/address/coverRes/task{title,desc,rewardExp,rewardPoints}`。

---

## Steps

- [x] **1. 文档先行**：写 `docs/PRD.md`（定位/用户画像/功能矩阵与优先级/9 屏用例说明/信息架构/数据模型含 Room 表结构与未来 MySQL 映射/接口预留清单/非功能需求/验收标准/风险）、`docs/MVP.md`（范围表 + 不做清单 + 里程碑）、`README.md`（构建运行、项目结构、决策记录）
- [x] **2. 依赖与主题**：`libs.versions.toml` + `app/build.gradle.kts` + `colors/strings/themes` 设计令牌 + 全部 drawable/icon
- [x] **3. 数据层**：6 个 Entity + Dao + `AppDatabase` + `SeedImporter`（首次启动注入 `assets/seed_points.json`）+ Repository 5 个 + `GeoUtils`/`PrefsManager`
- [x] **4. 导航骨架**：`MainActivity` 换 BottomNavigationView + 5 个空 Fragment 可切换
- [x] **5. 启动页 + 首页**：`SplashActivity` → `HomeFragment`（Banner/四宫格/热门横滑/附近列表/搜索）
- [x] **6. 地图页**：`MapCanvasView`（底图网格 + 街区色块 + Marker 绘制 + 点击命中测试）+ 底部点位卡 + 距离 + `geo:` 导航
- [x] **7. 详情页**：`DetailActivity`（封面/标题/评分/三 Tab/收藏 FAB/打卡按钮/关联点位）
- [x] **8. 扫码页**：`ScanActivity`（ZXing 内嵌 + 手动编号降级 + 成功/失败反馈）
- [x] **9. 任务页 + 建档**：`TaskFragment`（进度、立即挑战、提交后写 `checkin` + 加经验积分 + 生成消息）
- [x] **10. AI 导览**：`AssistantEngine` 本地规则（关键词→点位/年代/推荐路线三类模板）+ 聊天 UI + 快捷芯片
- [x] **11. 个人中心 + 消息**：三统计、四个入口、`MessageActivity`（红点未读）、`FavoriteActivity`、`MyTaskActivity`
- [x] **12. 离线**：`OfflineActivity`（缓存数/待同步数/立即同步模拟服务端校验），断网时详情页走 Room 兜底 + 顶部离线提示条
- [x] **13. 单测**：`GeoUtilsTest` + `AssistantEngineTest`，删掉 `ExampleUnitTest`
- [x] **14. 构建与验收**：`./gradlew assembleDebug`；`./gradlew test`；模拟器安装走完主线 7 步；断网复测详情/收藏/离线页
- [x] **15. Git 分步提交**（`git init` 后 6 次）：
  1. `chore(init): 初始化工程基建与依赖`（.gitignore、libs.versions.toml、build.gradle.kts、theme/color/string、drawable 基础）
  2. `docs(prd): 新增 V1.0 PRD 与 MVP 范围说明`（docs/PRD.md、docs/MVP.md、README.md）
  3. `feat(data): 搭建 Room 数据层与文化点位种子数据`（entity/dao/db/repo/seed/assets）
  4. `feat(ui): 实现 9 屏核心页面与探索闭环`（ui/、adapter/、layout、menu、MainActivity、Manifest）
  5. `feat(offline): 支持离线阅读与打卡待同步队列`
  6. `test: 补充地理与问答引擎单测并更新 README 验收章节`
- [ ] **16. 收尾**：`git log --oneline` 检查、确认 `local.properties` 未被跟踪、可选打 tag `v0.1.0`

---

## Verification

| 项 | 命令/操作 | 通过标准 |
|----|-----------|----------|
| 编译 | `./gradlew assembleDebug` | BUILD SUCCESSFUL，产出 `app/build/outputs/apk/debug/app-debug.apk` |
| 单测 | `./gradlew test` | 2 个测试类全绿 |
| 主线 | 模拟器安装后启动 | 启动页 → 首页 → 地图选点 → 详情 → 扫码（用手动编号降级演示）→ 任务提交 → 个人中心统计 +1 |
| 收藏 | 详情页收藏 → 个人中心我的收藏 | 列表出现该点位 |
| AI | 助手页问「文昌阁建于什么时候」 | 返回该点位年代，不崩溃、不空转 |
| 消息 | 提交任务后回首页 | 消息红点 +1，进入可见新通知 |
| 离线 | 设置页开飞行模式 → 打开已缓存点位 | 详情正常显示 + 顶部离线提示条；离线页显示待同步打卡 |
| Git | `git log --oneline`（6 条）、`git status` | 提交语义清晰，`local.properties`/`build/` 未入库 |

---

## 风险与对策

| 风险 | 对策 |
|------|------|
| AGP 9.5.1 + Room 2.6.1 annotationProcessor 兼容性 | 第 3 步做完立即 `assembleDebug`；失败则升 Room 2.7.1 或改用 `kapt`/`ksp`（仅 Room 注解处理器） |
| compileSdk 37 为预览级，某些 androidx 库要求 35 | 已核对：material 1.10 / appcompat 1.6.1 均兼容；新增库锁同类版本 |
| zxing-android-embedded 4.3.0 在 AGP 9 下 manifest merger 冲突 | 冲突时用 `tools:replace` 或改 `zxing:core` + 自建 CameraX 扫码（备选，工作量 +0.5 天） |
| 自绘地图点击命中不准 | Marker 半径 48dp 容错 + 底部也提供列表入口作为兜底 |
| 文化资料准确性 | 种子数据每条带 `sourceNote`，PRD 写明「演示数据，正式上线前需经海口地方文献核实」 |
