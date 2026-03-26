# 项目功能分析思维导图 (Mind Map)

该项目是一个基于 Android 平台的食品安全/环境检测系统，采用 MVP 架构，集成了硬件通讯、图像识别、数据分析及可视化功能。

## 1. 核心架构 (Core Architecture)
- **设计模式**: MVP (Model-View-Presenter) 架构。
- **框架集成**: 基于 JessYan's Arms 框架 (集成了 Dagger2, Retrofit, OkHttp, RxJava, RxAndroid, ButterKnife, EventBus, Gson)。
- **数据持久化**: 使用 GreenDAO 数据库进行本地存储，涉及用户、检测项、检测记录及标准库。
- **UI 适配**: 使用今日头条适配方案 (design_width_in_dp/design_height_in_dp)。

## 2. 身份认证与入口 (Authentication & Entry)
- **SplashActivity**: 启动页，可能包含初始化逻辑。
- **LoginActivity**: 登录页，对接平台服务 (Platform_Service) 进行认证。
- **HomeActivity**: 主页，作为功能导航的核心入口。

## 3. 检测核心模块 (Testing Core Modules)
- **FGGD (分光光度检测)**
    - **FGGD_TestActivity**: 核心检测流程，包含样品选择、项目匹配、结果计算与判定。
    - **FGGDAdjustingActivity**: 仪器校准与调整功能。
- **JTJ (胶体金检测)**
    - **JTJ_TestActivity**: 图像识别检测流程。
    - **图像识别 (OpenCV)**: 利用 CardDetector 进行色卡检测与类型识别。
    - **数据拟合**: 对 JTJPoint 数据进行曲线拟合与分析。

## 4. 业务流程支持 (Business Process Support)
- **样品/项目选择 (Selection Flow)**
    - **ChoseSampleActivity / ChoseSampleTypeActivity**: 选择检测样品及类型。
    - **ChoseProjectActivity**: 选择检测项目 (FGGDTestItem / JTJTestItem)。
    - **ChoseUnitActivity**: 选择受检单位/检测单位。
    - **EdtorSampleActivity**: 样品信息的录入与编辑。
- **检测结果处理 (Result Handling)**
    - **结果计算**: 根据检测值对比 FoodItemAndStandard 进行判定。
    - **结果打印**: 通过语音播报 (VoicePlayService) 或串口 (SerialDataService) 输出。

## 5. 记录管理与统计 (Records & Statistics)
- **检测记录 (Test Records)**
    - **TestRecordFragment**: 记录列表展示，支持筛选 (模块、结果、时间) 及搜索/删除。
    - **TestRecordMessageActivity**: 记录详情页，支持查看：
        - 二维码 (QRCodeUtils)。
        - 检测曲线 (LineChart)。
        - 检测详情 (样品、项目、判定标准等)。
- **统计分析 (Statistics)**
    - **CountActivity**: 对检测数据进行汇总与统计分析。

## 6. 系统配置与底层服务 (System & Services)
- **硬件通讯**:
    - **SerialDataService**: 串口数据收发，用于外接传感器或打印机。
    - **UvcCameraService**: UVC 摄像头驱动支持，用于图像采集。
- **网络服务**:
    - **MyMqttService**: 基于 MQTT 的实时数据上报与下发。
    - **Platform_Service**: 与云端平台进行 API 交互。
- **系统工具**:
    - **MultiScreenService**: 多屏异显支持。
    - **VoicePlayService**: 语音提示服务。

## 7. 技术栈总结 (Technical Stack Summary)
- **视觉处理**: OpenCV (图像检测、色值分析)。
- **数据可视化**: MPAndroidChart (检测曲线图表)。
- **通讯协议**: MQTT (长连接), Serial (串口), HTTP/REST (API)。
- **工具类**: BitmapUtils, FileUtils, QRCodeUtils, StringUtils。
- **权限管理**: RxPermissions (动态权限申请)。
