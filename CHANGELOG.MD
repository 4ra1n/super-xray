## 0.1-beta

初始版本

## 0.2-beta

Security:
- 修复潜在RCE漏洞 [GHSA-732j-763p-cvqg](https://github.com/4ra1n/super-xray/security/advisories/GHSA-732j-763p-cvqg)

Bug:
- [important] 复制和删除文件应该选择Java代码实现 #3
- [important] 最终执行命令不应该使用bash/cmd方式 #4
- [important] 使用PoC模式后再使用Plugins不会生效的问题 #5
- [important] Windows中cmd窗口乱码 #16

## 0.3-beta

该版本为**重大更新**

之前强制停止的逻辑并不能真正地停止，使用`kill/taskkill`命令才可以做到，该版本仅在`Mac OS`测试通过，在`Windows`中不确定。开一个新线程循环监听是否强制停止，如果收到消息立即结束。

Security:
- 修复潜在提权漏洞 [GHSA-2g28-xrw6-fq5f](https://github.com/4ra1n/super-xray/security/advisories/GHSA-2g28-xrw6-fq5f)

Bug:
- [important] 被动扫描后无法主动扫描的问题 #23
- [important] PoC参数为空的问题 #21
- [important] 被动扫描是否应该增加一个停止监听的功能 #20
- [important] windows11中高级配置的xss设置无法显示 #19
- [important] windows保存配置编码问题 #18
- [important] 完成真正的停止逻辑 #26（仅在MacOS测试通过）

Others:
- 打印命令应该用一个新的Form更优雅地展示 #9

## 0.4-beta

这个版本主要是提高稳定性，对已有`bug`进行修复，并且加入了两个重要：
- 皮肤选择功能，可以选择七款主题皮肤之一（需要操作系统支持）
- 保存上次xray设置的功能，不用每次打开都重新选择

Bug:
- [important] windows taskkill 命令 bug #29
- [important] 允许界面扩大缩小并重构高级配置的UI #28
- [important] 查看配置文件只改变Panel大小但Area不变 #31
- [important] windows 控制台乱码修复不完善 #27
- [important] 设置从上次配置自动加载xray #17
- [important] 反连平台支持token认证 #32

Others:
- 支持使用本地自定义PoC的功能 #6
- [new feature] 新增一个主题皮肤选择的功能 #11

## 0.5-beta

比较关键的问题是： 运行中无法访问已有的漏洞报告，因为采用的是`start/open`命令，报告被`xray`进程占用无法打开，但是可以读。
所以解决方案是复制一份实时的然后打开。其他方面是一些`UI`的小`bug`以及将日志输出到具体的文件，而不是全打在命令行。
输出的日志文件和所有产生的漏洞报告都可以在关闭软件的时候自动删除。

Bug:
- [important] 进程占导致无法打开被动扫描中的报告 #44
- [important] 换皮肤后一些按钮的背景色不应该是灰色 #42
- [important] 被动扫描可能会由于输出文件存在无法启动 #33
- 没有设置输出的情况下打开输出文件报错 #38

Others:
- [important] 添加一个跳转xray下载地址的按钮 #45
- [important] 添加start.bat和start.sh #41
- [new feature] 是否有必要提供一个下载按钮 #10 (采用 #45 的方案解决)
- 在Mac OS中如果保存了xray配置每次打开都会弹出提示 #36
- 皮肤RadioButton默认不该是空而应是当前皮肤 #37
- 应该将日志输出到临时文件并关闭后删除 #43
- 添加一个是否自动删除扫描报告的按钮 #39
- 增加一个清屏的按钮 #8

## 0.6-beta

支持了英文版本，修复了各种UI的问题

Bug:
- [important] 在1080P下界面无法完全显示 #46
- [important] 切换语言和皮肤时无法正确容纳UI #51
- [important] 关闭应用前应该先停止所有xray进程 #50
- 切换语言时无法正常显示PoC数量 #52
- 解析所有POC的逻辑有问题导致漏掉部分 #53

Others:
- [important] 添加一款现代皮肤IDEA同款 #54
- [new feature] english version #40
- #36问题的修复不完善 #47
- 打开扫描结果的Label与Button的UI不匹配 #48
- 点击底部Label跳转Github #49

## 0.7

该版本是第一个正式版，支持了`JDK8`的版本，当环境是`JDK8`时使用`JNA`技术访问`Native`方法，通过`Windows API`实现功能；
当环境是`JDK9+`时，反射调用方法即可。修复了一处功能`bug`并优先加载当前目录的`config.yaml`文件，不会删除，实现了保存设置功能。

另外提供了两种`exe`的下载：
- 内置`JRE`的`exe`可以一键启动：`super-xray-0.7-jre-exe.zip`
- 调用系统`JRE`的`exe`：`super-xray-0.7-system-jre.exe`

第一种不需要安装任何环境，解压后直接使用；第二种需要本地`JAVA_HOME`或环境变量有配置`Java`

Security:
- 修复SnakeYAML反序列化漏洞 [GHSA-39pv-4vmj-c4fr](https://github.com/4ra1n/super-xray/security/advisories/GHSA-39pv-4vmj-c4fr)

Bug:
- [important] 寻找一种能够支持JDK8的魔法 #61
- [important] 反连平台没有设置remote_server为true #57
- 不能支持JDK8的解决方案建议 #63 (同 #61)
- 使用IDEA皮肤时底部恢复按钮无法展示完全 #55

Others:
- [important] 支持EXE直接运行 #65
- [important] 配置文件无法保存每次都会新建 #58
- 优化启动脚本 #60 (最终没有优化因为支持了`JDK8`)

## 0.8

重要功能更新：可以与`rad`联动；可以为独立反连平台导出配置文件

重要`bug`说明：

之前版本有反馈说CPU消耗过高，是因为之前没有停止的逻辑，意味着每运行一次新任务开两个新线程，一个执行一个监视。
当执行线程结束，监视线程不会停止，任务过多时导致CPU消耗极高。虽然之前版本可以通过点击强制停止使所有监视线程停下，解决问题。
但这种情况需要得到一个合理的解决，这个版本中，主动扫描也有停止扫描的按钮。 
另外xray团队新增了一个在线生成yaml poc的网站，加了一个直接到达该网站的按钮，配合poc模块的加载本地poc功能比较舒服。
最后一个bug是当xray和super-xray在同一个路径的时候，无法保存config.yaml，因此不能加载上次的配置。

Bugs:
- [important] [公告] CPU消耗过高问题的解决 #69
- [important] 当xray和super-xray同目录时不能保存配置 #68
- [bug] 查看所有poc界面使用新字体后可能有意外行为 #70

Others:
- [new feature] 添加rad爬虫联动 #22
- 为反连平台添加导出配置文件的功能 #59
- 控制台使用更好看的字体 #67
- 加入跳转在线生成poc的按钮 #66

## 0.9

一个`bug`修复版本，目前功能基本完善

Bugs:
- [important] [bug] showMessageDialog多屏情况下的位置问题 #76
- [important] [bug] 反连平台应该自动配置client属性否则会失效 #72
- [important] [bug] 当reverse配置为空但确认配置后无法正常启动 #71
- [bug] 高级配置的某个ui绑定错误 #79
- [bug] 在线生成的翻译问题 #75 (另外修复多处翻译问题)

Others:
- [feat] 不输入端口情况下不应该允许开启被动扫描 #77
- 构建的System JRE EXE应该对JRE版本进行限制 #73
- 打开扫描结果为空时应该给一个提示 #78
- 添加一个按钮允许用户清除PoC的设置 #80

## 1.0

介绍视频：https://www.bilibili.com/video/BV1Zd4y1h7Rf

增加了下载面板，可以一键下载最新版xray和rad并加载；增加了子域名扫描的模块，但仅高级版xray可用。

- New Features:
- [important] [feat] 自动下载解压最新版xray并加载 #86
- [important] [feat] 自动下载解压最新版rad并加载 #87
- [important] [feat] 添加子域名扫描的功能 #85
- [important] [feat] 指定多个poc扫描的实现方式 #82
- [important] 用户可选关闭后保留日志用于调试 #83
- [feat] 不配置目标的时候应该禁止主动扫描 #81
- [feat] 添加一个rad下载站点按钮 #84
- 应该提示用户先输入reverse-http url然后配置服务端 #88

## 1.1

2023年1月10日`xray`更新了`1.9.4`版本。

添加了多个`yaml`编写的`poc`以及`xstream`扫描模块。

更新内容：
- [important] 支持xray 1.9.4版本 #91
- [important] 指定多个POC #92
- [bug] 从下载面板得到的xray无法保存设置 #89
- [bug] 取消选择文件的提示位置有误 #94

## 1.2

升级UI库，添加菜单，简单重构UI并修复了一些BUG

更新内容：
- [important] [feat] 允许设置并发数量避免发包过多 #107
- [important] [feat] 添加简单的菜单功能 #111
- [feat] 下载面板显示当前的操作系统 #108
- [bug] 高级配置恢复后文件设置没有清除 #110
- [bug] 被动扫描的输出配置逻辑有问题 #105
- [bug] 子域名扫描停止扫描后应该清屏 #106
- [improve] 动态保存设置的并发数量 #109
- [improve] PoC模块应该给出一个简单的介绍 #99
- [improve] 插件模块的UI简单重构 #100
- [improve] 完善页面上的提示信息 #101
- 更新`UI`库`flatlaf`到`3.0`版本

## 1.3

重大更新版本：
- 修复安全漏洞
- AJP服务扫描面板
- 新增多个菜单选项
- 内置六款小游戏

修复安全漏洞：[GHSA-vgxx-2crp-32vv 特殊情况下的完全拒绝服务](https://github.com/4ra1n/super-xray/security/advisories/GHSA-vgxx-2crp-32vv)

使用`exe`版本加载`super-xray`自身将会无限启动，导致耗尽操作系统`CPU`和内存。无限启动原因是启动时需要执行之前保存的`xray`路径，如果选择了`super-xray.exe`自身保存后显然会导致死循环。由于用户输入的可执行文件是什么应该由用户负责，一般情况下不应该是安全漏洞，但是用户输入`super-xray`自身导致的问题应该被处理。二进制可执行文件无法判断具体是什么，所以只能限制文件名结合文件大小二者来防止加载自身，但是用户加载其他可执行文件导致的安全问题与本项目无关。

更新内容：
- [important] [security] 加载自身将会耗尽操作系统资源 #112
- [important] [feat] 增加Tomcat AJP服务扫描面板 #123
- [important] [feat] 新菜单显示当前版本和支持xray最低版本 #113
- [important] [feat] 验证super-xray最新版本 #114
- [important] [doc] 更新README中所有的图片 #121
- [feat] 加入六款小游戏用于摸鱼 #124
- [feat] 子域名扫描加入关闭删除选项 #118
- [feat] 应该提供一个按钮允许用户复制当前命令 #127
- [improve] 查看命令按钮显示内容过长问题 #126
- [improve] 并发设置应该有一个上限和下限 #125
- [improve] 某些情况同步PoC会卡住较长时间 #122
- [improve] 加载rad也应该对输入文件进行限制 #120
- [improve] 支持关闭时删除被动扫描生成的ca文件 #117
- [improve] 子域名扫描的输入过滤 #116
- [improve] 禁用子域名扫描的ip-only选项 #119

## 1.4

春节快乐，从该版本以后使用更帅的**黑暗主题**，并且简单重构了UI

解决了重要的问题：CPU占用过高。允许QPS和最大HOST连接数等参数的设置，优化了一些功能

更新内容：
- [important] [improve] CPU占用较高需要进行优化 #128
- [important] [feat] 1.4版本以后将全面适配黑暗主题（更帅一些） #135
- [important] [feat] 每秒最大http请求数max_qps参数设置 #137
- [bug] 提示文字的翻译有误 #133
- [feat] 被动扫描允许自由设置（目前仅支持127） #131
- [feat] 最大HOST允许的连接数（降低对服务的影响） #138
- [improve] 查看搜索PoC时应该排序以提高效率 #134
- [improve] 不常用的配置应该都移到高级配置中 #132
- [improve] 避免直接使用其他项目的图标 #130
- [improve] 删除没有必要的动态皮肤并简单重构UI #136
- 使用更好的exe图标
- 删除扫雷和弹球（不好玩且与黑暗主题不适配）

## 1.5

更新内容：
- [important] [improve] 更简洁更好用的新UI #144
- [important] [feat] 支持拖拽xray文件加载 #140
- [important] [feat] 允许设置日志等级 #146
- [bug] CPU占用较高问题RAD修复不完善 #139
- [feat] 增加一键修复/还原的功能 #143
- [feat] 自动检查版本更新并提示 #142
- [improve] 反连HTTP URL输入的验证 #145

下载：
- super-xray-1.5.jar 版本通过`java -jar super-xray-1.5.jar`启动
- super-xray-1.5-jre-exe.zip 是内置了JRE的exe版本
- super-xray-1.5-system-jre.exe 是使用系统JRE的exe版本
- Super-Xray.app.zip 是 Mac OS 的 app (测试版可能有bug)

## 1.6

更新内容：
- [bug] 备份xray目录原来的config.yaml文件 #152
- [feat] RAD 联动支持拖拽功能 #151
- [feat] request 支持自定义 user-agent #149
- [feat] 允许设置请求Cookie内容 #150

下载：
- super-xray-1.6.jar 版本通过`java -jar super-xray-1.6.jar`启动
- super-xray-1.6-jre-exe.zip 是内置了JRE的exe版本
- super-xray-1.6-system-jre.exe 是使用系统JRE的exe版本