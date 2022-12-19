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

Bugs:
- null

Others:
- [important] [feat] 指定多个poc扫描的实现方式 #82
- [important] 用户可选关闭后保留日志用于调试 #83
- [feat] 不配置目标的时候应该禁止主动扫描 #81
- [feat] 添加一个rad下载站点按钮 #84
- [important] 代码重构 #12 (关闭该问题，没必要重构)
- 添加代码注释并补充文档 #14
- 完善日志输出 #30