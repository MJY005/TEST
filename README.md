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
- 关闭网络后搜索，验证本地词库兜底与提示。
- 验证“记住密码”“自动登录”开关的持久化效果。





>集成 Retrofit+Gson+OkHttp（app/build.gradle），新增 DictionaryApi、RetrofitClient、WordApiResponse，实现 Free Dictionary API 在线查词，SearchActivity 改为后台异步请求+进度提示，失败回退本地词库并优化列表点击与清空逻辑。
>数据模型扩展：WordBean 增加音标/音频字段，InfoListAdapter 支持音标回显，WordDescActivity 与布局展示音标。
>认证与存储：新增 User、UserDatabaseHelper、AuthRepository（SQLite 持久化并内置 demo 账号），实现 RegisterActivity 与注册页布局，loginPage/LoginActivity 使用仓库校验登录、记住密码、跳转主菜单。AndroidManifest.xml 注册新 Activity 并补充网络权限。
>UI/文档：activity_search.xml 增加进度条容器，activity_register.xml/登录页新增注册入口，README.md 补充项目说明、流程与测试建议。
>测试：暂未在本地运行，请在设备上按 README 提示（可用 demo 账号或新注册）验证登录、查词、离线兜底及记住密码。

1. SQLite 数据库集成
数据库名：test.db（与 DBeaver 一致）
表结构：user 表（_id, username, password, age, phone, created_at）
注册功能已写入 SQLite：RegisterActivity 调用 AuthRepository.register() 写入数据库
2. 单例模式
AuthRepository 改为单例，确保数据库访问一致性
所有 Activity 使用 AuthRepository.getInstance(context) 获取实例
3. SQLite CRUD 操作
Create（增加）：UserManagementActivity 中“添加新用户”对话框
Read（查询）：loadUsers() 方法查询所有用户并显示在列表
Update（修改）：每个用户项的“编辑”按钮可修改年龄、手机号、密码
Delete（删除）：每个用户项的“删除”按钮可删除用户
4. 用户管理界面
新增 UserManagementActivity：展示所有用户，支持增删改查
主菜单新增“用户管理 (SQLite CRUD)”按钮，点击进入管理界面
列表显示：ID、用户名、年龄、手机号
每个用户项有“编辑”和“删除”按钮
5. MVC 架构
Model：AuthRepository（数据访问层）、UserDatabaseHelper（数据库操作）
View：布局文件（activity_user_management.xml、item_user.xml、dialog_user_edit.xml）
Controller：UserManagementActivity（业务逻辑控制）
6. 代码规范
关键方法添加注释
使用单例模式
异常处理完善
代码结构清晰
使用说明
注册用户：在登录页点击“注册新账号”，填写信息后数据写入 test.db
查看用户：登录后进入主菜单，点击“用户管理 (SQLite CRUD)”查看所有用户
添加用户：在用户管理界面点击“+ 添加新用户”
编辑用户：点击用户项的“编辑”按钮修改信息
删除用户：点击用户项的“删除”按钮删除用户
所有操作都会实时同步到 SQLite 数据库，可在 DBeaver 中查看 test.db 验证数据变化。
项目已符合要求：SQLite 增删改查、单例模式、MVC 架构、代码规范。