## 项目简介
LukeDict 是一款面向英语学习的 Android 应用，提供在线单词查询、阅读、客服与关于等模块。本次改造完成了注册/登录流程、SQLite 本地用户存储，以及基于 Free Dictionary API 的在线查词功能。

## 主要功能
- 启动页→登录/注册→主菜单跳转。
- 注册/登录：本地 SQLite 持久化账户，记住密码与自动登录。
- 在线查词：Retrofit 调用 Free Dictionary API，展示音标与释义，支持本地词库兜底。
- 单词详情：显示音标、释义与例句。
- 阅读、客服、反馈等原有功能保留。

## 技术栈
- 语言：Java（Android SDK 31，minSdk 21）
- 架构/模式：Activity + 简单 Repository（AuthRepository 单例化 DB 访问）
- 网络：Retrofit2 + OkHttp 日志 + Gson
- 数据库：SQLite（UserDatabaseHelper）
- UI：ConstraintLayout/LinearLayout/ListView，ViewBinding 已开启

## 快速开始
1. 打开工程目录，使用 Android Studio 连接 Gradle，同步依赖（已添加 Retrofit/Gson/OkHttp）。
2. 运行应用，启动页 5s 后进入登录页，可使用内置账号：
   - 用户名：`demo`
   - 密码：`123456`
3. 点击“注册新账号”创建新用户，注册成功后返回登录并输入新账号。
4. 登录后进入主菜单，可进入“单词查询”体验在线查词。

## 网络查词说明
- API：Free Dictionary API（`https://api.dictionaryapi.dev/api/v2/entries/en/{word}`）
- 解析模型：`WordApiResponse`→`WordBean`，包含音标/音频链接/释义/例句。
- 线程：`AsyncTask` 在后台请求并更新 UI。
- 容错：网络异常时回退到本地词库 `SearchUtils.searchLocal`；无结果时 Toast 提示。
- 权限：`AndroidManifest.xml` 已声明 `INTERNET` 与 `ACCESS_NETWORK_STATE`。

## 数据库设计
- 数据库：`lukedict.db`
- 表：`users(id INTEGER PRIMARY KEY, username TEXT UNIQUE, password TEXT, email TEXT, created_at INTEGER)`
- 初始种子：demo 账号便于测试。

## 目录速览
- `app/src/main/java/com/example/lukedict/`：业务代码
  - `SearchActivity`：在线查词 + 结果列表
  - `WordDescActivity`：单词详情
  - `RetrofitClient`、`DictionaryApi`、`WordApiResponse`：网络层
  - `AuthRepository`、`UserDatabaseHelper`、`RegisterActivity`、`loginPage`/`LoginActivity`：认证与存储
- `app/src/main/res/layout/`：界面布局（含登录、注册、查词、详情等）

## 测试建议
- 使用 demo 账号登录，或注册新账号后登录。
- 在“单词查询”输入常见单词（如 `hello`、`apple`）验证在线结果与音标显示。
- 关闭网络后
搜索，验证本地词库兜底与提示。
- 验证“记住密码”“自动登录”开关的持久化效果。

