# AGENTS.md

琼楼寻踪 · QiongLou Explorer —— Android 原生文化探索 App。

## 项目速览

- 技术栈:Java 11、Gradle KTS、AGP 9.5.1、compileSdk / targetSdk 37、minSdk 24
- 数据:Room 2.6.1(annotationProcessor,非 KSP);Gson 2.11.0、zxing-android-embedded 4.3.0
- UI:XML 视图(无 Compose);Material 1.10.0、AppCompat 1.6.1、ConstraintLayout 2.1.4
- 结构:单模块 `:app`,包名 `com.bin.qionglouexplorer`
- 构建:`./gradlew assembleDebug` → `app/build/outputs/apk/debug/app-debug.apk`;测试 `./gradlew test`
- 约束:零外部依赖,不需要地图 Key / 大模型 Key / 后端网络
- 坑:AGP 9 已移除 `compileOptions.annotationProcessorOptions`,注解处理器参数走 `androidComponents.onVariants`

文档:`docs/PRD.md`(需求)、`docs/MVP.md`(V0.1 范围)、`plan.md`(V1.0 完整方案)。

## Agent skills

### Issue tracker

Issues 与规格存放在本仓库的 GitHub Issues(`wangpanbin/QiongLouExplorer`)。详见 `docs/agents/issue-tracker.md`。

### Triage labels

五个默认角色标签:`needs-triage`、`needs-info`、`ready-for-agent`、`ready-for-human`、`wontfix`。详见 `docs/agents/triage-labels.md`。

### Domain docs

单上下文(single-context):根目录 `GLOSSARY.md` + `docs/adr/`。详见 `docs/agents/domain.md`。
